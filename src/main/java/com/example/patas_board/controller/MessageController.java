package com.example.patas_board.controller;

import com.example.patas_board.controller.form.MessageForm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MessageController {
    @GetMapping("/new")
    public ModelAndView view(){
        ModelAndView mav = new ModelAndView();
        MessageForm messageForm = new MessageForm();
        mav.addObject("formModel", messageForm);
        mav.setViewName("/new");
        return mav;
    }
}
