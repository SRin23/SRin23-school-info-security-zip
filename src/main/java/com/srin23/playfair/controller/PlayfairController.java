package com.srin23.playfair.controller;

import com.srin23.playfair.domain.Playfair;
import com.srin23.playfair.domain.repository.dto.request.RequestDto;
import com.srin23.playfair.service.PlayfairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/3204")
public class PlayfairController {
    private PlayfairService playfairService;

    public PlayfairController(PlayfairService playfairService) {
        this.playfairService = playfairService;
    }

    @RequestMapping(value="")
    public String indexPage(Model model){
        return "index";
    }

    @GetMapping(value="/input")
    public String inputPage(Model model){
        model.addAttribute("requestDto", new RequestDto());
        return "input";
    }

    @PostMapping(value="/input")
    public String inputPost(@ModelAttribute("requestDto") RequestDto requestDto){
        String key = requestDto.getEncryptionKey().toUpperCase();
        String text = requestDto.getPlainText().toUpperCase();
        return "output";
    }
}
