package com.example.patas_board.repository;

import com.example.patas_board.repository.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    public List<Message> findAllByOrderByUpdatedDateDesc();
    public List<Message> findByCreatedDateBetweenOrderByUpdatedDateDesc(Timestamp start, Timestamp end);

    Page<Message> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
