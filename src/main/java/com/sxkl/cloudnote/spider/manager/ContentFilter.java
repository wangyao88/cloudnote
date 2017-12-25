package com.sxkl.cloudnote.spider.manager;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.sxkl.cloudnote.utils.StringAppendUtils;

@Component
public class ContentFilter {
	
    private static Set<String> unValidateContents;
    private static String endContent;
    private static final String END_TAG = "</article>";
	
	@PostConstruct
	private void init(){
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
		for(String unValidateContent : unValidateContents){
			content = content.replaceAll(unValidateContent, "");
		}
		int endIndex = content.lastIndexOf(END_TAG);
		if(endIndex > 0){
			content = content.substring(0, endIndex+END_TAG.length());
			content = StringAppendUtils.append(content,endContent);
		}
		return content;
	}

}
