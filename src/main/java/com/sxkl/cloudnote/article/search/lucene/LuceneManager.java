package com.sxkl.cloudnote.article.search.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.service.ArticleService;

/**
 * @author: wangyao
 * @date: 2018年1月29日 下午1:04:34
 * @description:
 */
@Service
public class LuceneManager {
	
	private static final String INDEXPATH = "c:\\lucene";
	private static RAMDirectory ramDirectory;
	private static IndexWriter ramWriter;

	@Autowired
	private ArticleService articleService;

	static {
		try {
			FSDirectory fsDirectory = FSDirectory.open(Paths.get(INDEXPATH));
			ramDirectory = new RAMDirectory(fsDirectory, IOContext.READONCE);
			fsDirectory.close();
//			Analyzer analyzer = new StandardAnalyzer();
			Analyzer analyzer = new IKAnalyzer(true);// 中文分词器
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);// 中文分词器
			// new SmartChineseAnalyzer());//中文分词器
			indexWriterConfig.setIndexDeletionPolicy(new SnapshotDeletionPolicy(new KeepOnlyLastCommitDeletionPolicy()));
			ramWriter = new IndexWriter(ramDirectory, indexWriterConfig);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@PostConstruct
	public void init() throws IOException, ParseException, InvalidTokenOffsetsException{
//		reCreatIndex();
//		List<Article> list = search("mysql 排序 分页");
//		for(Article article : list){
//			System.out.println(article.getTitle());
//		}
	}

	// 于磁盘创建索引
	public void reCreatIndex() {
		try {
			Path path = Paths.get(INDEXPATH);
			// 删除原有索引文件
			for (File file : path.toFile().listFiles()) {
				file.delete();
			}
			FSDirectory fsDirectory = FSDirectory.open(path);
			Analyzer analyzer = new IKAnalyzer(true);// 中文分词器
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
			IndexWriter writer = new IndexWriter(fsDirectory, indexWriterConfig);
			List<Article> articles = articleService.getAllArticles("sdfweqsd");
			for (Article article : articles) {
				writer.addDocument(toDocument(article));
			}
			writer.close();
			System.out.println("-----创建索引成功---");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 实体article对象转document索引对象
	public Document toDocument(Article article) {
		Document doc = new Document();
		doc.add(new StringField("id", String.valueOf(article.getId()), Field.Store.YES));
		doc.add(new Field("title", article.getTitle(), TextField.TYPE_STORED));
		doc.add(new Field("content", article.getContent(), TextField.TYPE_STORED));
		return doc;
	}

	// 添加索引
	public synchronized void addDocument(Article article) throws IOException {
		ramWriter.addDocument(toDocument(article));
		ramWriter.commit();
	}

	// 删除索引
	public synchronized void deleteDocument(Long id) throws IOException {
		Term term = new Term("id", String.valueOf(id));
		ramWriter.deleteDocuments(term);
		ramWriter.commit();
	}

	// 搜索
	public List<Article> search(String keyword) throws IOException, ParseException, InvalidTokenOffsetsException {
		List<Article> list = new ArrayList<Article>();
		IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(ramDirectory));
		String[] fields = { "title", "content" };
//		Analyzer analyzer = new StandardAnalyzer();
		Analyzer analyzer = new IKAnalyzer(true);// 中文分词器
		QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
		Query query = queryParser.parse(keyword);
		// BooleanClause.Occur[] clauses = {BooleanClause.Occur.SHOULD,
		// BooleanClause.Occur.SHOULD};
		// Query query = MultiFieldQueryParser.parse(keyword, fields, clauses,
		// analyzer);
		TopDocs hits = indexSearcher.search(query, 20);

		// 高亮
		SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
		Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Article article = new Article();
			Document doc = indexSearcher.doc(scoreDoc.doc);
			article.setId(doc.get("id"));
			String title = doc.get("title");
			String content = doc.get("content");
			article.setTitle(
					highlighter.getBestFragment(analyzer.tokenStream("title", new StringReader(title)), title));
			article.setContent(
					highlighter.getBestFragment(analyzer.tokenStream("content", new StringReader(content)), content));
			list.add(article);
		}
		return list;
	}

	// 更新索引
	public void updateDocument(Article article) throws IOException {
		Term term = new Term("id", String.valueOf(article.getId()));
		ramWriter.updateDocument(term, toDocument(article));
		ramWriter.commit();
	}

	// 同步索引至磁盘
	public void indexSync() {
		IndexWriterConfig config = null;
		SnapshotDeletionPolicy snapshotDeletionPolicy = null;
		IndexCommit indexCommit = null;
		try {
			config = (IndexWriterConfig) ramWriter.getConfig();
			snapshotDeletionPolicy = (SnapshotDeletionPolicy) config.getIndexDeletionPolicy();
			indexCommit = snapshotDeletionPolicy.snapshot();
			config.setIndexCommit(indexCommit);
			Collection<String> fileNames = indexCommit.getFileNames();
			Path toPath = Paths.get(INDEXPATH);
			Directory toDir = FSDirectory.open(toPath);
			// 删除所有原有索引文件
			for (File file : toPath.toFile().listFiles()) {
				file.delete();
			}
			// 从ramdir复制新索引文件至磁盘
			for (String fileName : fileNames) {
				toDir.copyFrom(ramDirectory, fileName, fileName, IOContext.DEFAULT);
			}
			toDir.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("-----索引同步完成------");
	}
}
