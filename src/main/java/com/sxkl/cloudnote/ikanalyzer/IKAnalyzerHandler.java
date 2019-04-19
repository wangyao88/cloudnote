package com.sxkl.cloudnote.ikanalyzer;

import com.google.common.collect.Lists;
import com.sxkl.cloudnote.ikanalyzer.lucene.IKAnalyzer;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

@Slf4j
public class IKAnalyzerHandler {

    public static List<String> handle(String words) {
        // 构建IK分词器，使用smart分词模式
        Analyzer analyzer = new IKAnalyzer(true);
        // 获取Lucene的TokenStream对象
        List<String> result = Lists.newArrayList();
        try {
            @Cleanup
            TokenStream ts = analyzer.tokenStream("myfield", new StringReader(words));
            // 获取词元位置属性
            OffsetAttribute offset = ts.addAttribute(OffsetAttribute.class);
            // 获取词元文本属性
            CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
            // 获取词元文本属性
            TypeAttribute type = ts.addAttribute(TypeAttribute.class);
            // 重置TokenStream（重置StringReader）
            ts.reset();
            // 迭代获取分词结果
            while (ts.incrementToken()) {
                result.add(term.toString());
            }
            // 关闭TokenStream（关闭StringReader）
            ts.end(); // Perform end-of-stream operations, e.g. set the final
        } catch (IOException e) {
            log.error("执行中文分词失败！错误信息：", e);
        }
        return result;
    }

    public static void main(String[] args) {
        String words = "这是一个中文分词的例子，你可以直接运行它！IKAnalyer can analysis english text too";
        List<String> result = IKAnalyzerHandler.handle(words);
        result.forEach(word -> System.out.println(word));
    }
}
