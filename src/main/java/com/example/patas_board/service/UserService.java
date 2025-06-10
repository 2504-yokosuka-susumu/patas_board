package com.example.patas_board.service;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.repository.UserRepository;
import com.example.patas_board.repository.entity.User;
import com.example.patas_board.utils.CipherUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserForm login(String account, String password){
        //String encPassword = CipherUtil.encrypt(password);

        List<User> results = userRepository.findByAccountAndPassword(account, password);

        if(results.contains(null)) {
            return null;
        }else{
            List<UserForm> users = setUserForm(results);
            return users.get(0);
        }
    }

    private List<UserForm> setUserForm(List<User> results){
        List<UserForm> users = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            UserForm user = new UserForm();
            User result = results.get(i);
            user.setId(result.getId());
            user.setAccount(result.getAccount());
            user.setPassword(result.getPassword());
            user.setName(result.getName());
            user.setBranchId(result.getBranchId());
            user.setDepartmentId(result.getDepartmentId());
            user.setIsStopped(result.getIsStopped());
            users.add(user);
        }
        return users;
    }
}
