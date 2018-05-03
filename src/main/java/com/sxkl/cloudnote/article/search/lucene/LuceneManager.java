package com.sxkl.cloudnote.article.search.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexCommit;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.KeepOnlyLastCommitDeletionPolicy;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.service.ArticleRecursiveTask;
import com.sxkl.cloudnote.article.service.ArticleService;
import com.sxkl.cloudnote.ikanalyzer.lucene.IKAnalyzer;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.user.entity.User;
import com.sxkl.cloudnote.user.service.UserService;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: wangyao
 * @date: 2018年1月29日 下午1:04:34
 * @description:
 */
@Slf4j
@Service
public class LuceneManager {
	
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserService userService;
	private static final int PAGE_SIZE = 50;
	
	@Logger(message="于磁盘创建所有用户笔记索引")
	public void initAllUserArticleIndex() {
		try {
			List<User> users = userService.getAllUsers();
			for(User user : users){
				creatIndexOnDisk(user.getId());
			}
		} catch (Exception e) {
			log.error("于磁盘创建所有用户笔记索引失败！错误信息：{}",e.getMessage());
		}
	}

	@Logger(message="于磁盘创建笔记索引")
	public void creatIndexOnDisk(String userId) {
		try {
			Path path = Paths.get(getPath(userId));
			File temp = path.toFile();
			if(!temp.exists()){
				temp.mkdirs();
			}
			for (File file : temp.listFiles()) {
				file.delete();
			}
			FSDirectory fsDirectory = FSDirectory.open(path);
			Analyzer analyzer = new IKAnalyzer(true);
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			@Cleanup
			IndexWriter writer = new IndexWriter(fsDirectory, indexWriterConfig);
			
			
			List<Article> articles = articleService.getAllArticles(userId);
			
//			int articleTotal = articleService.getArticleTotal(userId);
//			List<Article> articles = new ArrayList<Article>(articleTotal);
//			ForkJoinPool forkJoinPool = new ForkJoinPool();  
//			int start = 0;
//			int end = articleTotal/PAGE_SIZE;
//			ArticleRecursiveTask task = new ArticleRecursiveTask(start,end,userId,PAGE_SIZE,articleService);
//	        Future<List<Article>> result = forkJoinPool.submit(task);  
//	        try {  
//	            articles.addAll(result.get());
//	        } catch (Exception e) { 
//	        	e.printStackTrace();
//	        }  
			
//			int currentPage = 0;
//			while(currentPage*PAGE_SIZE < articleTotal){
//				articles.addAll(articleService.findPage(currentPage,PAGE_SIZE,userId));
//				currentPage++;
//			}
			for (Article article : articles) {
				writer.addDocument(toDocument(article));
			}
		} catch (IOException e) {
			log.error("于磁盘创建笔记索引失败！错误信息：{}",e.getMessage());
		}
	}

	@Logger(message="实体article对象转document笔记索引对象")
	public Document toDocument(Article article) {
		Document doc = new Document();
		doc.add(new StringField("id", String.valueOf(article.getId()), Field.Store.YES));
		doc.add(new Field("title", article.getTitle(), TextField.TYPE_STORED));
		doc.add(new Field("hitNum", String.valueOf(article.getHitNum()), TextField.TYPE_STORED));
		doc.add(new Field("content", article.getContent(), TextField.TYPE_STORED));
		return doc;
	}

	@Logger(message="添加笔记索引")
	public synchronized void addDocument(Article article, String userId){
		try {
			@Cleanup
			IndexWriter ramWriter = getFSIndexWriter(userId);
			ramWriter.addDocument(toDocument(article));
			ramWriter.commit();
		} catch (IOException e) {
			log.error("添加笔记索引失败！错误信息：{}",e.getMessage());
		}
	}

	@Logger(message="删除笔记索引")
	public synchronized void deleteDocument(String articleId, String userId){
		try {
			Term term = new Term("id", articleId);
			@Cleanup
			IndexWriter ramWriter = getFSIndexWriter(userId);
			ramWriter.deleteDocuments(term);
			ramWriter.flush();
			ramWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("删除笔记索引失败！错误信息：{}",e.getMessage());
		}
	}

	@Logger(message="更新笔记索引")
	public void updateDocument(Article article, String userId){
		try {
			Term term = new Term("id", String.valueOf(article.getId()));
			@Cleanup
			IndexWriter ramWriter = getFSIndexWriter(userId);
			ramWriter.updateDocument(term, toDocument(article));
			ramWriter.flush();
			ramWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
			log.error("更新笔记索引失败！错误信息：{}",e.getMessage());
		}
	}
	
	@Logger(message="搜索笔记")
	public List<Article> search(String keyword, String userId, int pageSize){
		List<Article> list = Lists.newArrayList();
		try {
			IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(getRAMDirectory(userId)));
			String[] fields = { "title", "content" };
			Analyzer analyzer = new IKAnalyzer(true);
			QueryParser queryParser = new MultiFieldQueryParser(fields, analyzer);
			Query query = queryParser.parse(keyword);
			TopDocs hits = indexSearcher.search(query, pageSize);
			// 高亮
			SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
			Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));

			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Article article = new Article();
				Document doc = indexSearcher.doc(scoreDoc.doc);
				article.setId(doc.get("id"));
				String title = doc.get("title");
				String content = doc.get("content");
				String hitNum = doc.get("hitNum");
				article.setTitle(highlighter.getBestFragment(analyzer.tokenStream("title", new StringReader(title)), title));
				article.setContent(highlighter.getBestFragment(analyzer.tokenStream("content", new StringReader(content)), content));
				article.setHitNum(Integer.valueOf(hitNum));
				list.add(article);
			}
		} catch (Exception e) {
			log.error("搜索笔记失败！错误信息：{}",e.getMessage());
		}
		return list;
	}

	@Logger(message="同步笔记索引至磁盘")
	public void indexSync(String userId) {
		try {
			IndexWriterConfig config = null;
			SnapshotDeletionPolicy snapshotDeletionPolicy = null;
			IndexCommit indexCommit = null;
			IndexWriter ramWriter = getRAMIndexWriter(userId);
			config = (IndexWriterConfig) ramWriter.getConfig();
			snapshotDeletionPolicy = (SnapshotDeletionPolicy) config.getIndexDeletionPolicy();
			indexCommit = snapshotDeletionPolicy.snapshot();
			config.setIndexCommit(indexCommit);
			Collection<String> fileNames = indexCommit.getFileNames();
			Path toPath = Paths.get(getPath(userId));
			@Cleanup
			Directory toDir = FSDirectory.open(toPath);
			// 删除所有原有笔记索引文件
			for (File file : toPath.toFile().listFiles()) {
				file.delete();
			}
			// 从ramdir复制新笔记索引文件至磁盘
			for (String fileName : fileNames) {
				toDir.copyFrom(getRAMDirectory(userId), fileName, fileName, IOContext.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("同步笔记索引至磁盘失败！错误信息：{}",e.getMessage());
		}
	}
	
	private String getPath(String userId){
		String path = LuceneManager.class.getClassLoader().getResource("").getPath();
		path = Joiner.on("").join(Arrays.asList(path,"index/",userId));
		if(path.contains(":")){
			path = path.substring(1);
		}
		return path;
	}
	
	private RAMDirectory getRAMDirectory(String userId) throws IOException{
		@Cleanup
		FSDirectory fsDirectory = FSDirectory.open(Paths.get(getPath(userId)));
		return new RAMDirectory(fsDirectory, IOContext.READONCE);
	}
	
	private IndexWriter getRAMIndexWriter(String userId) throws IOException{
		Analyzer analyzer = new IKAnalyzer(true);
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		indexWriterConfig.setIndexDeletionPolicy(new SnapshotDeletionPolicy(new KeepOnlyLastCommitDeletionPolicy()));
		return new IndexWriter(getRAMDirectory(userId), indexWriterConfig);
	}
	
	private IndexWriter getFSIndexWriter(String userId) throws IOException{
		Path path = Paths.get(getPath(userId));
		FSDirectory fsDirectory = FSDirectory.open(path);
		Analyzer analyzer = new IKAnalyzer(true);
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		return new IndexWriter(fsDirectory, indexWriterConfig);
	}
}
