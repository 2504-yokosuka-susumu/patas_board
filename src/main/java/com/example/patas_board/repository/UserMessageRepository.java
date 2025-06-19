package com.example.patas_board.repository;

import com.example.patas_board.repository.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByOrderByCreatedDateDesc();
    List<Message> findByCreatedDateBetweenOrderByCreatedDateDesc(Timestamp startDate, Timestamp endDate);
    List<Message> findByCategoryContainingAndCreatedDateBetweenOrderByCreatedDateDesc(String categoryText, Timestamp startDate, Timestamp endDate);
    List<Message> findAllByUserId(int id);
}
