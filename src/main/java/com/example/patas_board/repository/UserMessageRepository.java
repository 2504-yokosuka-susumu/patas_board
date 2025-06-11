package com.example.patas_board.repository;

import com.example.patas_board.repository.entity.Comment;
import com.example.patas_board.repository.entity.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {
    public List<UserMessage> findAllByOrderByCreatedDateDesc();
    @Query(value = "select e.id as id, e.title as title, e.text as text,"
            + "e.userId as userId, e.category as category, d.name as name, d.account as account,"
            + "e.created_date as createdDate"
            + "from messages e "
            + "inner join users d on d.id = e.user_id "
            + "order by e.created_date ",
            nativeQuery = true)
    List<UserMessage> findUserMessages();
}
