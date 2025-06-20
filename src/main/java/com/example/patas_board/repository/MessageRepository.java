package com.example.patas_board.repository;

import com.example.patas_board.repository.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    Page<Message> findAllByOrderByCreatedDateDesc(Pageable pageable);
}
