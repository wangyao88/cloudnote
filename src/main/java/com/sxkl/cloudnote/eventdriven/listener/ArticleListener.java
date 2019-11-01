package com.sxkl.cloudnote.eventdriven.listener;

import com.sxkl.cloudnote.article.dao.ArticleDao;
import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.article.search.lucene.LuceneManager;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherEvent;
import com.sxkl.cloudnote.image.service.ImageService;
import com.sxkl.cloudnote.log.annotation.Logger;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;


@Slf4j
@Service
public class ArticleListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
    private ImageService imageService;
    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private LuceneManager luceneManager;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (isNotDuty(event)) {
            return;
        }
        ArticlePublisherBean article = (ArticlePublisherBean) event.getSource();
        switch (article.getDutype()) {
            case LINK_ARTICLE_IMAGE:
                linkArticleImage(article);
                break;
            case INCREASE_HITNUM:
                increaseHitNum(article);
                break;
            case UPDATE_INDEX_BY_ADD_OPERATION:
//	        	indexManager.updateIndexByAdd(convertArticle(article),article.getUserId());
                luceneManager.addDocument(convertArticle(article), article.getUserId());
                break;
            case UPDATE_INDEX_BY_UPDATE_OPERATION:
//	        	indexManager.updateIndexByUpdate(convertArticle(article),article.getUserId());
                luceneManager.updateDocument(convertArticle(article), article.getUserId());
                break;
            case UPDATE_INDEX_BY_DELETE_OPERATION:
//	        	indexManager.updateIndexByDelete(convertArticle(article),article.getUserId());
                luceneManager.deleteDocument(convertArticle(article), article.getUserId());
                break;
            default:
                log.error("不支持的操作类型");
                break;
        }
    }

    @Logger(message = "增加笔记点击量")
    public void increaseHitNum(ArticlePublisherBean bean) {
        articleDao.increaseHitNum(bean.getArticleId());
    }

    @Logger(message = "建立笔记和图片的关系")
    public void linkArticleImage(ArticlePublisherBean article) {
        Document doc = Jsoup.parse(article.getArticleContent());
        Elements imgs = doc.getElementsByTag("img");
        String regex = "^(http|https|ftp)+://.*$";
        for (int i = 0; i < imgs.size(); i++) {
            String imgUrl = imgs.get(i).attr("src");
            if ((Pattern.matches(regex, imgUrl)) && (imgUrl.startsWith(article.getDomain()))) {
                String imageName = imgUrl.substring(imgUrl.lastIndexOf("=") + 1);
                imageService.establishLinkagesBetweenArticleAndImage(article.getArticleId(), imageName);
            }
        }
    }

    private boolean isNotDuty(ApplicationEvent event) {
        return !event.getClass().toString().equals(ArticlePublisherEvent.class.toString());
    }

    private Article convertArticle(ArticlePublisherBean articlePublisherBean) {
        Article article = new Article();
        article.setId(articlePublisherBean.getArticleId());
        article.setTitle(articlePublisherBean.getArticleTitle());
        article.setContent(articlePublisherBean.getArticleContent());
        article.setHitNum(articlePublisherBean.getHitNum());
        article.setCreateTime(articlePublisherBean.getCreateTime());
        User user = new User();
        user.setId(articlePublisherBean.getUserId());
        article.setUser(user);
        Note note = new Note();
        note.setId(articlePublisherBean.getNoteId());
        article.setNote(note);
        return article;
    }

    public void cacheAddArticleTreeMenu(HttpServletRequest request) {

    }
}
