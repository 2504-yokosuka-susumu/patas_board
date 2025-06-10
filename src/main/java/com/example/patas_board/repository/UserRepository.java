package com.example.patas_board.repository;

import com.example.patas_board.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public List<User> findByAccountAndPassword(String account, String password);
}
