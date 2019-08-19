package com.sxkl.cloudnote.article.search.lucene;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import com.sxkl.cloudnote.ikanalyzer.core.IKSegmenter;
import com.sxkl.cloudnote.ikanalyzer.core.Lexeme;

import lombok.Cleanup;

/**
 *  * @author wangyao
 *  * @date 2018年1月10日 下午10:02:34
 *  * @description: 分词器
 *  
 */
public class WordAnalyzer {

    public static Map<String, Integer> analysis(String content) {
        Map<String, Integer> result = new HashMap<String, Integer>();
        // 创建分词对象
        @Cleanup
        StringReader reader = new StringReader(content);
        IKSegmenter ik = new IKSegmenter(reader, true);// 当为true时，分词器进行最大词长切分
        Lexeme lexeme = null;
        try {
            String word;
            while ((lexeme = ik.next()) != null) {
                word = lexeme.getLexemeText();
                if (NumberUtils.isNumber(word)) {
                    continue;
                }
                int count = 1;
                if (result.containsKey(word)) {
                    count = result.get(word) + 1;
                }
                result.put(word, count);
            }
        } catch (IOException e) {

        }
        return result;
    }

    public static void main(String[] args) {
        String text = "微信 跳一跳 外挂";
        System.out.println(WordAnalyzer.analysis(text));
    }
}
