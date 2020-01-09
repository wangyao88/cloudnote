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
import java.util.Comparator;
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
    private static final String PROCESS = "心路历程";

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
            if (StringUtils.isBlank(text)) {
                return StringUtils.EMPTY;
            }
            return Arrays.stream(text.split(";")).map(str -> StringUtils.appendJoinEmpty("<p>", str, "</p>")).collect(Collectors.joining("", "<ul>", "</ul>"));
        };
        return getResult(SKILL, handler);
    }

    @Logger(message = "获取心路历程")
    public String getProcess(HttpServletRequest request) {
        List<KeyValue> keyValues = getKeyValues(PROCESS, null).stream().sorted(Comparator.comparing(KeyValue::getKey).reversed()).collect(Collectors.toList());
        List<KeyValue> result = keyValues.stream().map(keyValue -> {
            String value = keyValue.getValue();
            StringBuilder resultValue = new StringBuilder();
            List<String> monthItemList = Arrays.stream(value.split("@@")).sorted(Comparator.reverseOrder()).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            for (String monthItem : monthItemList) {
                String[] monthItemArray = monthItem.split("##");
                String month = monthItemArray[0];
                String item = Arrays.stream(monthItemArray[1].split(";")).filter(StringUtils::isNotBlank).collect(Collectors.joining("<br>", "", ""));
                resultValue.append("<li><span>")
                        .append(month)
                        .append("</span><p><span>")
                        .append(item)
                        .append("</span></p></li>");

            }
            keyValue.setValue(resultValue.toString());
            return keyValue;
        }).collect(Collectors.toList());
        return OperateResultService.configurateSuccessResult(result);
    }

    private String getResult(String flagName) {
        return getResult(flagName, null);
    }

    private String getResult(String flagName, Function<String, String> handler) {
        return OperateResultService.configurateSuccessResult(getKeyValues(flagName, handler));
    }

    private List<KeyValue> getKeyValues(String flagName, Function<String, String> handler) {
        List<Flag> flags = flagDao.getFlagsByName(flagName, BLOG);
        return flags.stream()
                .flatMap(flag -> articleDao.selectArticlesByFlagIdOrderByCreateTime(flag.getId()).stream())
                .map(result -> new KeyValue(String.valueOf(result[0]), String.valueOf(result[1]), handler))
                .collect(Collectors.toList());
    }
}
