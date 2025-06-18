package com.example.patas_board.service;

import com.example.patas_board.controller.form.BranchForm;
import com.example.patas_board.controller.form.UserCommentForm;
import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.controller.form.UserMessageForm;
import com.example.patas_board.repository.BranchRepository;
import com.example.patas_board.repository.entity.Branch;
import com.example.patas_board.repository.entity.Message;
import com.example.patas_board.repository.entity.User;
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

    public List<BranchForm> findAllBranch() {

        List<Branch> results = branchRepository.findAll();
        List<BranchForm> branches = setBranchForm(results);
        return branches;
    }

    private List<BranchForm> setBranchForm(List<Branch> results) {
        List<BranchForm> branches = new ArrayList<>();

        //Listのひとつずつ取り出し
        for (int i = 0; i < results.size(); i++) {
            List<UserForm> users = userService.findAllUser();
            int totalMessage = 0;
            int totalComment = 0;
            for(int j = 0; j < users.size(); j++){
                if(users.get(j).getBranchId() == i+1){
                    List<UserMessageForm> messages = messageService.findAllUserPost(users.get(j).getId());
                    List<UserCommentForm> comments = commentService.findAllUserPost(users.get(j).getId());
                    totalMessage += messages.size();
                    totalComment += comments.size();
                }

            }
            BranchForm branch = new BranchForm();
            Branch result = results.get(i);

            branch.setId(result.getId());
            branch.setName(result.getName());
            branch.setTotalPost(totalMessage);
            branch.setTotalComment(totalComment);
            branches.add(branch);
        }
        return branches;
    }
}
