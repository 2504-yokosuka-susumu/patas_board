package com.example.patas_board.service;

import com.example.patas_board.controller.form.UserForm;
import com.example.patas_board.repository.UserRepository;
import com.example.patas_board.repository.entity.User;
import com.example.patas_board.utils.CipherUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public UserForm login(String account, String password){
        String encPassword = CipherUtil.encrypt(password);

        List<User> results = userRepository.findByAccountAndPassword(account, encPassword);

        if(results.size() == 0) {
            return null;
        }else{
            List<UserForm> users = setUserForm(results);
            return users.get(0);
        }
    }

    public UserForm checkedAccount(String account){
        List<User> results = userRepository.findByAccount(account);

        if(results.size() == 0) {
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

    /*
     * レコード全件取得処理
     */
    public List<UserForm> findAllUser() {

        List<User> results = userRepository.findAllByOrderById();

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
            user.setCreatedDate(result.getCreatedDate());
            user.setUpdatedDate(new Date());
            user.setLoginDate(result.getLoginDate());
            users.add(user);
        }

        return users;
    }

    /*
     * レコード追加
     */
    public void save(UserForm reqUser) {

        List<User> results = new ArrayList<>();
        results.add((User) userRepository.findById(reqUser.getId()).orElse(null));

        User user = new User();

        user.setId(reqUser.getId());
        user.setAccount(results.get(0).getAccount());
        user.setPassword(results.get(0).getPassword());
        user.setName(results.get(0).getName());
        user.setBranchId(results.get(0).getBranchId());
        user.setDepartmentId(results.get(0).getDepartmentId());
        user.setIsStopped(reqUser.getIsStopped());
        user.setCreatedDate(results.get(0).getCreatedDate());
        user.setUpdatedDate(new Date());

        userRepository.save(user);
    }

    public void createUser(UserForm reqUser) {
        String encPassword = CipherUtil.encrypt(reqUser.getPassword());
        reqUser.setPassword(encPassword);

        User user = new User();

        user.setId(reqUser.getId());
        user.setAccount(reqUser.getAccount());
        user.setPassword(reqUser.getPassword());
        user.setName(reqUser.getName());
        user.setBranchId(reqUser.getBranchId());
        user.setDepartmentId(reqUser.getDepartmentId());
        user.setIsStopped(reqUser.getIsStopped());
        user.setCreatedDate(reqUser.getCreatedDate());
        user.setUpdatedDate(new Date());

        userRepository.save(user);
    }

    /*
     * 指定のidレコード情報取得
     */
    public UserForm selectUser(int id){

        List<User> userResults = new ArrayList<>();
        userResults.add((User) userRepository.findById(id).orElse(null));

        if(userResults.contains(null)) {
            return null;
        }

        UserForm user = new UserForm();

        user.setId(userResults.get(0).getId());
        user.setAccount(userResults.get(0).getAccount());
        user.setPassword(userResults.get(0).getPassword());
        user.setName(userResults.get(0).getName());
        user.setBranchId(userResults.get(0).getBranchId());
        user.setDepartmentId(userResults.get(0).getDepartmentId());
        user.setIsStopped(userResults.get(0).getIsStopped());
        user.setCreatedDate(userResults.get(0).getCreatedDate());
        user.setUpdatedDate(new Date());

        return user;
    }

    /*
     * ユーザ情報編集
     */
    public void updateUser(UserForm reqUser) {

        List<User> userResults = new ArrayList<>();
        userResults.add((User) userRepository.findById(reqUser.getId()).orElse(null));

        String encPassword = CipherUtil.encrypt(reqUser.getPassword());

        User user = new User();

        user.setId(reqUser.getId());
        user.setAccount(reqUser.getAccount());
        if(!reqUser.getPassword().isBlank()) {
            user.setPassword(encPassword);
        } else {
            user.setPassword(userResults.get(0).getPassword());
        }
        user.setName(reqUser.getName());
        if(reqUser.getBranchId() == 0) {
            user.setBranchId(userResults.get(0).getBranchId());
        } else {
            user.setBranchId(reqUser.getBranchId());
        }
        if(reqUser.getDepartmentId() == 0) {
            user.setDepartmentId(userResults.get(0).getDepartmentId());
        } else {
            user.setDepartmentId(reqUser.getDepartmentId());
        }

        userRepository.save(user);
    }

    @Transactional
    public void saveLoginDate(int id){
        Calendar cl = Calendar.getInstance();
        //現在時刻の情報を取得
        Date currentTime = cl.getTime();
        userRepository.updateLoginDate(id, currentTime);
    }
}
