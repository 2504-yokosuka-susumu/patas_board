package com.example.patas_board.service;

import com.example.patas_board.repository.BranchRepository;
import com.example.patas_board.repository.entity.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BranchService {
    @Autowired
    BranchRepository branchRepository;
    /*
     * レコード全件取得処理
     */
    public HashMap<Integer,String> findAllBranchesMap() {
        List<Branch> results = branchRepository.findAll();
        HashMap<Integer,String> branchChoices = new HashMap<>();
        for(int i=0;i<results.size();i++){
            Branch result = results.get(i);
            branchChoices.put(result.getId(), result.getName());
        }
        return branchChoices;
    }
}
