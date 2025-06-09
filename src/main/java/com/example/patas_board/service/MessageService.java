package com.example.patas_board.service;

import com.example.patas_board.controller.form.MessageForm;
import com.example.patas_board.repository.MessageRepository;
import com.example.patas_board.repository.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    /*
     * レコード全件取得処理
     */
    public List<MessageForm> findAllMessage() {
        List<Message> results = messageRepository.findAllByOrderByUpdatedDateDesc();
        List<MessageForm> messages = setMessageForm(results);
        return messages;
    }

    /*
     * レコード日付範囲取得処理
     */
    public List<MessageForm> findByCreated_dateMessage(String start, String end) throws ParseException {

        if (start == null || start.isEmpty()) {
            start = "2020-01-01 00:00:00";
        } else {
            start += " 00:00:00";
        }
        if (end == null || end.isEmpty()) {
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTimestamp);
        } else {
            end += " 23:59:59";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Timestamp startDate = Timestamp.valueOf(start);
        Timestamp endDate = Timestamp.valueOf(end);
        List<Message> results = messageRepository.findByCreatedDateBetweenOrderByUpdatedDateDesc(startDate, endDate);
        List<MessageForm> messages = setMessageForm(results);
        return messages;
    }

    /*
     * DBから取得したデータをFormに設定
     */
    private List<MessageForm> setMessageForm(List<Message> results) {
        List<MessageForm> messages = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            MessageForm message = new MessageForm();
            Message result = results.get(i);
            message.setId(result.getId());
            message.setTitle(result.getTitle());
            message.setText(result.getText());
            message.setCategory(result.getCategory());
            message.setUserId(result.getUserId());
            messages.add(message);
        }
        return messages;
    }
}
