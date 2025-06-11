package com.example.patas_board.repository;

import com.example.patas_board.repository.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMessageRepository extends JpaRepository<Message, Integer> {
    @Query(value = "select messages.id as id, messages.title as title, messages.text as text,"
            + "messages.user_id as userId, messages.category as category, users.name as name, users.account as account,"
            + "messages.created_date as createdDate "
            + "from messages "
            + "inner join messages.users on users.id = messages.user_id "
            + "order by messages.created_date ",
            nativeQuery = true)
    List<Message> findUserMessages();
}
