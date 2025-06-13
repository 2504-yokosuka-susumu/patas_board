package com.example.patas_board.service;

import com.example.patas_board.controller.form.CommentForm;
import com.example.patas_board.controller.form.UserCommentForm;
import com.example.patas_board.controller.form.UserMessageForm;
import com.example.patas_board.repository.CommentRepository;
import com.example.patas_board.repository.entity.Comment;
import com.example.patas_board.repository.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    /*
     * レコード全件取得処理
     */
    public List<UserCommentForm> findAllComment() {
        List<Comment> results = commentRepository.findAll();
        List<UserCommentForm> comments = setUserCommentForm(results);
        return comments;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<CommentForm> setCommentForm(List<Comment> results) {
        List<CommentForm> comments = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            CommentForm comment = new CommentForm();
            Comment result = results.get(i);
            comment.setId(result.getId());
            comment.setText(result.getText());
            comment.setUserId(result.getUserId());
            comment.setMessageId(result.getMessageId());
            comment.setCreatedDate(result.getCreatedDate());
            comment.setUpdatedDate(result.getUpdatedDate());
            comments.add(comment);
        }
        return comments;
    }

    private List<UserCommentForm> setUserCommentForm(List<Comment> results) {
        List<UserCommentForm> comments = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            UserCommentForm comment = new UserCommentForm();
            Comment result = results.get(i);
            if (result.getUser() == null) {
                continue;
            }
            comment.setId(result.getId());
            comment.setText(result.getText());
            comment.setUserId(result.getUserId());
            comment.setMessageId(result.getMessageId());
            comment.setName(result.getUser().getName());
            comment.setAccount(result.getUser().getAccount());
            comment.setCreatedDate(result.getCreatedDate());

            comments.add(comment);
        }
        return comments;
    }

    /*
     * レコード追加
     */
    public void saveComment(CommentForm reqComment) {
        Comment saveComment = setCommentEntity(reqComment);
        commentRepository.save(saveComment);
    }
    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Comment setCommentEntity(CommentForm reqComment) {
        Comment comment = new Comment();
        comment.setId(reqComment.getId());
        comment.setText(reqComment.getText());
        comment.setUserId(reqComment.getUserId());
        comment.setMessageId(reqComment.getMessageId());
        return comment;
    }

    /*
     * 投稿削除
     */
    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }
}
