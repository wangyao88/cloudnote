package com.sxkl.cloudnote.article.service;

import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.KeyValue;
import com.sxkl.cloudnote.common.service.OperateResultService;
import com.sxkl.cloudnote.flag.dao.FlagDao;
import com.sxkl.cloudnote.flag.entity.Flag;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.utils.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BlogService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private FlagDao flagDao;

    private static final String BLOG = "如影随形博客";
    private static final String NAVIGATION = "导航栏";
    private static final String BASE_INFO = "个人基本信息";
    private static final String LINK = "友情链接";
    private static final String SKILL = "关于我";

    @Logger(message = "获取导航栏")
    public String getNavigation(HttpServletRequest request) {
        return getResult(NAVIGATION);
    }

    @Logger(message = "获取个人基本信息")
    public String getBaseInfo(HttpServletRequest request) {
        return getResult(BASE_INFO);
    }

    @Logger(message = "获取友情链接")
    public String getLink(HttpServletRequest request) {
        return getResult(LINK);
    }

    @Logger(message = "获取职业技能")
    public String getSkill(HttpServletRequest request) {
        Function<String, String> handler = value -> {
            Document doc = Jsoup.parse(value);
            String text = doc.text();
            if(StringUtils.isBlank(text)) {
                return StringUtils.EMPTY;
            }
            return Arrays.stream(text.split(";")).map(str -> StringUtils.appendJoinEmpty("<p>", str, "</p>")).collect(Collectors.joining("", "<ul>", "</ul>"));
        };
        return getResult(SKILL, handler);
    }

    private String getResult(String flagName) {
        return getResult(flagName, null);
    }

    private String getResult(String flagName, Function<String, String> handler) {
        List<Flag> flags = flagDao.getFlagsByName(flagName, BLOG);
        List<KeyValue> results = flags.stream()
                .flatMap(flag -> articleDao.selectArticlesByFlagIdOrderByCreateTime(flag.getId()).stream())
                .map(result -> new KeyValue(String.valueOf(result[0]), String.valueOf(result[1]), handler))
                .collect(Collectors.toList());
        return OperateResultService.configurateSuccessResult(results);
    }
}
