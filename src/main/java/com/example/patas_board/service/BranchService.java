package com.example.patas_board.service;

import com.example.patas_board.controller.form.BranchForm;
import com.example.patas_board.controller.form.UserCommentForm;
import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.controller.form.UserMessageForm;
import com.example.patas_board.repository.BranchRepository;
import com.example.patas_board.repository.entity.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class BranchService {
    @Autowired
    BranchRepository branchRepository;

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    CommentService commentService;
    /*
     * Branch全件取得処理
     */
    public HashMap<Integer,String> findAllBranchesMap() {
        List<Branch> results = branchRepository.findAll();
        HashMap<Integer,String> branchChoices = new HashMap<>();
        //キー値をid、値をnameのハッシュマップを作製
        for(int i=0;i<results.size();i++){
            Branch result = results.get(i);
            branchChoices.put(result.getId(), result.getName());
        }
        return branchChoices;
    }

    /*
     * 支社情報の取得
     */
    public List<BranchForm> findAllBranch() {

        List<Branch> results = branchRepository.findAll();
        List<BranchForm> branches = setBranchForm(results);
        return branches;
    }

    /*
     * Formへの詰め替え（支社ごとの投稿数とコメント数もここでセットする）
     */
    private List<BranchForm> setBranchForm(List<Branch> results) {
        List<BranchForm> branches = new ArrayList<>();

        //Listのひとつずつ取り出し
        for (int i = 0; i < results.size(); i++) {
            // 支社と投稿・コメントを紐づけるためにユーザー情報取得
            List<UserForm> users = userService.findAllUser();
            // トータルの数を代入するための変数を定義
            int totalMessage = 0;
            int totalComment = 0;
            // ユーザーの分だけユーザーIDを回して投稿とコメントを探しに行く
            for(int j = 0; j < users.size(); j++) {
                // iの数+1で支社のIDごとにユーザーを振り分けながら探しに行く
                if(users.get(j).getBranchId() == i+1) {
                    List<UserMessageForm> messages = messageService.findAllUserPost(users.get(j).getId());
                    List<UserCommentForm> comments = commentService.findAllUserComment(users.get(j).getId());
                    // 支社ごとに振り分けられたユーザーの投稿数・コメント数の合計を足していく
                    totalMessage += messages.size();
                    totalComment += comments.size();
                }

            }
            BranchForm branch = new BranchForm();
            Branch result = results.get(i);

            branch.setId(result.getId());
            branch.setName(result.getName());
            // 支社ごとの投稿数・コメント数を順番にセット
            branch.setTotalPost(totalMessage);
            branch.setTotalComment(totalComment);
            branches.add(branch);
        }
        return branches;
    }
}
