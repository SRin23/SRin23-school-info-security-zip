package com.srin23.playfair.controller;

import com.srin23.playfair.domain.Playfair;
import com.srin23.playfair.domain.repository.dto.request.RequestDto;
import com.srin23.playfair.service.PlayfairService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PlayfairController {
    private final PlayfairService playfairService;

    public PlayfairController(PlayfairService playfairService) {
        this.playfairService = playfairService;
    }

    @RequestMapping(value="")
    public String indexPage(Model model){
        RequestDto requestDto = new RequestDto();
        model.addAttribute("requestPlayfair", requestDto);
        return "index";
    }

}
