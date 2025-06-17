package com.example.patas_board.repository;

import com.example.patas_board.repository.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public List<User> findByAccountAndPassword(String account, String password);
    public List<User> findByAccount(String account);
    public List<User> findAllByOrderById();

//    @Transactional
//    @Query(value = "UPDATE users SET login_date = :currentTime WHERE id = :id", nativeQuery = true)
//    @Modifying
//    int updateLoginDate(@Param("id") int id, @Param("currentTime"));
}
