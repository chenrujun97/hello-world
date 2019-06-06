package com.crj.hello.service;

import com.crj.hello.enums.CommentTypeEnum;
import com.crj.hello.exception.CustomizeErrorCode;
import com.crj.hello.exception.CustomizeException;
import com.crj.hello.mapper.CommentMapper;
import com.crj.hello.mapper.QuestionExtMapper;
import com.crj.hello.mapper.QuestionMapper;
import com.crj.hello.model.Comment;
import com.crj.hello.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    @Autowired
    @SuppressWarnings("all")
    private CommentMapper commentMapper;

    @Autowired
    @SuppressWarnings("all")
    private QuestionMapper questionMapper;

    @Autowired
    @SuppressWarnings("all")
    private QuestionExtMapper questionExtMapper;

    @Transactional
    public void insert(Comment comment) {

        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }

        if (comment.getType() == null || ! CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }

        if (comment.getType() == CommentTypeEnum.QUESTION.getType()){
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            incCommentCount(question.getId());
        } else {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }
    }

    public void incCommentCount(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setCommentCount(1);
        questionExtMapper.incCommentCount(question);
    }
}
