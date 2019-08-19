package com.sxkl.cloudnote.eventdriven.publisher;

import com.google.common.base.Throwables;
import com.sxkl.cloudnote.note.entity.Note;
import com.sxkl.cloudnote.utils.ObjectUtils;
import com.sxkl.cloudnote.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sxkl.cloudnote.article.entity.Article;
import com.sxkl.cloudnote.common.entity.Constant;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherBean;
import com.sxkl.cloudnote.eventdriven.entity.ArticlePublisherEvent;
import com.sxkl.cloudnote.eventdriven.entity.DutyType;
import com.sxkl.cloudnote.log.annotation.Logger;

@Slf4j
@Service
public class ArticlePublisher {

    @Autowired
    private ApplicationContext applicationContext;

    @Logger(message = "建立笔记和图片的关系")
    public void establishLinkagesBetweenArticleAndImage(Article article) {
        ArticlePublisherBean bean = new ArticlePublisherBean();
        bean.setArticleId(article.getId());
        bean.setArticleContent(article.getContent());
        bean.setDomain(Constant.DOMAIN);
        bean.setDutype(DutyType.LINK_ARTICLE_IMAGE);
        applicationContext.publishEvent(new ArticlePublisherEvent(bean));
    }

    @Logger(message = "增加笔记点击量")
    public void increaseArticleHitNum(String id) {
        ArticlePublisherBean bean = new ArticlePublisherBean();
        bean.setArticleId(id);
        bean.setDutype(DutyType.INCREASE_HITNUM);
        applicationContext.publishEvent(new ArticlePublisherEvent(bean));
    }

    @Logger(message = "增加笔记，更新笔记索引")
    public void updateIndexByAdd(Article article, String userId) {
        updateIndex(article, userId, DutyType.UPDATE_INDEX_BY_ADD_OPERATION);
    }

    @Logger(message = "更新笔记，更新笔记索引")
    public void updateIndexByUpdate(Article article, String userId) {
        updateIndex(article, userId, DutyType.UPDATE_INDEX_BY_UPDATE_OPERATION);
    }

    @Logger(message = "删除笔记，更新笔记索引")
    public void updateIndexByDelete(Article article, String userId) {
        updateIndex(article, userId, DutyType.UPDATE_INDEX_BY_DELETE_OPERATION);
    }

    private void updateIndex(Article article, String userId, DutyType dutyType) {
        try {
            ArticlePublisherBean bean = new ArticlePublisherBean();
            bean.setArticleId(article.getId());
            bean.setArticleTitle(article.getTitle());
            bean.setArticleContent(article.getContent());
            bean.setHitNum(article.getHitNum());
            bean.setCreateTime(article.getCreateTime());
            bean.setUserId(userId);
            Note note = article.getNote();
            String noteId = ObjectUtils.isNull(note) ? StringUtils.EMPTY : note.getId();
            bean.setNoteId(noteId);
            bean.setDutype(dutyType);
            applicationContext.publishEvent(new ArticlePublisherEvent(bean));
        } catch (Exception e) {
            log.error("发布更新索引事件失败！错误信息：" + Throwables.getStackTraceAsString(e));
        }
    }
}
