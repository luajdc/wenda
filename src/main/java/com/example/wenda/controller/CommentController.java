package com.example.wenda.controller;

import com.example.wenda.model.Comment;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.HostHolder;
import com.example.wenda.service.CommentService;
import com.example.wenda.service.QuestionService;
import com.example.wenda.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/addComment"},method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,
                             @RequestParam("content") String content){
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null) {
                comment.setUserId(hostHolder.getUser().getId());
            } else {
                //匿名的用户
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            }
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);

            System.out.println(comment.getEntityId()+":"+comment.getEntityType());
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),count);

        }catch (Exception e){
            logger.error("增加评论失败:"+e.getMessage());
        }
        return "redirect:/question/"+questionId;
    }
}
