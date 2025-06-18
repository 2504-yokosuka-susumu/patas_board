package com.example.patas_board.service;

import com.example.patas_board.controller.form.CommentForm;
import com.example.patas_board.controller.form.UserCommentForm;
import com.example.patas_board.controller.form.UserMessageForm;
import com.example.patas_board.repository.CommentRepository;
import com.example.patas_board.repository.UserCommentRepository;
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

    @Autowired
    UserCommentRepository userCommentRepository;
    /*
     * DBからコメントを全件取得処理
     */
    public List<UserCommentForm> findAllComment() {
        //Comment型でDBから全件取得
        List<Comment> results = commentRepository.findAll();
        //UserComment型に変換
        List<UserCommentForm> comments = setUserCommentForm(results);
        return comments;
    }

    public List<UserCommentForm> findAllUserPost(int id) {
        List<Comment> results = userCommentRepository.findAllByUserId(id);
        List<UserCommentForm> comments = setUserCommentForm(results);
        return comments;
    }

//    public List<UserCommentForm> findAllBranchComment(int id) {
//        List<Comment> results = userCommentRepository.findAllByBranchId(id);
//        List<UserCommentForm> comments = setUserCommentForm(results);
//        return comments;
//    }

    //Comment型をUserComment型に変換
    private List<UserCommentForm> setUserCommentForm(List<Comment> results) {
        //空のリストを作成
        List<UserCommentForm> comments = new ArrayList<>();

        //Listからひとつずつ取り出す
        for (int i = 0; i < results.size(); i++) {
            UserCommentForm comment = new UserCommentForm();
            Comment result = results.get(i);
            //MessageのuserIdのユーザーが存在しない時スキップ
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
     * DBにコメントを登録
     */
    public void saveComment(CommentForm reqComment) {
        //CommentForm型をComment型に変換
        Comment saveComment = setCommentEntity(reqComment);
        //Comment型のものをDBに保存・更新
        commentRepository.save(saveComment);
    }
    /*
     * リクエストから取得した情報をComment型に設定
     */
    private Comment setCommentEntity(CommentForm reqComment) {
        //空のComment型を定義
        Comment comment = new Comment();
        //要素を格納していく
        comment.setId(reqComment.getId());
        comment.setText(reqComment.getText());
        comment.setUserId(reqComment.getUserId());
        comment.setMessageId(reqComment.getMessageId());
        return comment;
    }

    /*
     * DBからコメントを削除
     */
    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }
}
