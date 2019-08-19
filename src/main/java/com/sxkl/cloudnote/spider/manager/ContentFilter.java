package com.sxkl.cloudnote.spider.manager;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.sxkl.cloudnote.utils.StringAppendUtils;

@Component
public class ContentFilter {

    private static Set<String> unValidateContents;
    private static String endContent;
    private static final String END_TAG = "</article>";

    @PostConstruct
    private void init() {
        StringBuilder endContentTemp = new StringBuilder();
        endContentTemp.append("<div class=\"article_copyright\">")
                .append("版权声明：本文为博主原创文章，未经博主允许不得转载。")
                .append("</div>")
                .append("</body>")
                .append("</html>");
        endContent = endContentTemp.toString();
        unValidateContents = new HashSet<String>();
        unValidateContents.add("<script id=\"toolbar-tpl-scriptId\" prod=\"download\" skin=\"black\" src=\"http://c.csdnimg.cn/public/common/toolbar/js/content_toolbar.js\" type=\"text/javascript\" domain=\"http://blog.csdn.net/\"></script>");
    }


    public String filter(String content) {
        for (String unValidateContent : unValidateContents) {
            content = content.replaceAll(unValidateContent, "");
        }
        int endIndex = content.lastIndexOf(END_TAG);
        if (endIndex > 0) {
            content = content.substring(0, endIndex + END_TAG.length());
            content = StringAppendUtils.append(content, endContent);
        }
        Document document = Jsoup.parse(content);
        Elements scripts = document.getElementsByTag("script");
        for (Element script : scripts) {
            script.remove();
        }
//		Elements imgs = document.getElementsByAttributeValue("src", StringUtils.EMPTY);
        Elements imgs = document.getElementsByTag("img");
        for (Element img : imgs) {
            if (StringUtils.EMPTY.equals(img.attr("src"))) {
                img.remove();
            }
        }
        return document.html();
    }

}
