package com.example.patas_board.controller;

import com.example.patas_board.controller.form.MessageForm;
import com.example.patas_board.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MessageController {

    @Autowired
    MessageService messageService;

    @GetMapping("/new")
    public ModelAndView view(){
        ModelAndView mav = new ModelAndView();
        MessageForm messageForm = new MessageForm();
        mav.addObject("formModel", messageForm);
        mav.setViewName("/new");
        return mav;
    }

    @PostMapping("/add")
    public ModelAndView addMessage(@ModelAttribute("formModel") @Validated MessageForm messageForm,
                                   BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            List<String> errorMessages = new ArrayList<String>();
            for (ObjectError error : result.getAllErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }
            mav.setViewName("/new");
            return mav;
        } else {
            messageService.addMessage(messageForm);
            return new ModelAndView("redirect:/");
        }
    }
}
