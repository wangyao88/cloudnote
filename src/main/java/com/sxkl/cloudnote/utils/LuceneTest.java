package com.sxkl.cloudnote.utils;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.sxkl.cloudnote.ikanalyzer.core.IKSegmenter;
import com.sxkl.cloudnote.ikanalyzer.core.Lexeme;

/**
 *  * @author wangyao
 *  * @date 2018年1月10日 下午9:37:33
 *  * @description:
 *  
 */
public class LuceneTest {

    public static void main(String[] args) throws Exception {
//	        //查看MMesj分词器
//	        String dicPath = getDicPath();
//	        // 获取到中文分词了
//	        MMSegAnalyzer mmseg = new MMSegAnalyzer(new File(dicPath));
//	        String content = "我是中国人";
//	        System.out.println("----------MMSeg分词器--------------");
//	        dispalyToken(mmseg, content);
//
//	        //定义IkAnalyzer分词器
//	        Analyzer analyzer = new IKAnalyzer(true);
//	        //查看分词效果
//	        System.out.println("------------IkAnalyzer分词器-----------");
//	        dispalyToken(analyzer, content);

        // 单独使用
        // 检索内容
        String text = "KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);";

        // 创建分词对象
        StringReader reader = new StringReader(text);

        IKSegmenter ik = new IKSegmenter(reader, true);// 当为true时，分词器进行最大词长切分
        Lexeme lexeme = null;
        try {
            while ((lexeme = ik.next()) != null)
                System.out.println(lexeme.getLexemeText());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }
    }

    /**
     * 创建用户:狂飙的yellowcong<br/>
     * 创建日期:2017年12月3日<br/>
     * 创建时间:下午9:20:13<br/>
     * 机能概要: 查看分词信息
     *
     * @param analyzer
     * @param content  需要分词的类容
     * @throws Exception
     */
    public static void dispalyToken(Analyzer analyzer, String content) throws Exception {
        TokenStream stream = analyzer.tokenStream("content", new StringReader(content));

        // 获取分词信息
        CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
        stream.reset();
        while (stream.incrementToken()) {
            System.out.println("[" + cta.toString() + "]");
        }
    }

    /**
     * 创建用户:狂飙的yellowcong<br/>
     * 创建日期:2017年12月3日<br/>
     * 创建时间:下午4:50:16<br/>
     * 机能概要:获取字典的存放地址
     *
     * @return
     */
    public static String getDicPath() {
        String dicPath = LuceneTest.class.getClassLoader().getResource("lucene").getFile();
        return dicPath;
    }
}
