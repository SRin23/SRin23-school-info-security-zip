package com.srin23.playfair.controller;

import com.srin23.playfair.domain.repository.dto.request.RequestDto;
import com.srin23.playfair.domain.repository.dto.response.DelSpaceDto;
import com.srin23.playfair.service.PlayfairService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/*
처리해야할것
1. 복호시, 추가되었던 X문자 삭제하기
2. 코드 정리
3. mirim 코드 다시 분석하기
 */

@Controller
@RequestMapping("")
public class PlayfairController {
    private PlayfairService playfairService;

    public PlayfairController(PlayfairService playfairService) {
        this.playfairService = playfairService;
    }

    @RequestMapping(value="/")
    public String indexPage(Model model){
        return "index";
    }

    @GetMapping(value="/playfair")
    public String inputPage(Model model){
        model.addAttribute("requestDto", new RequestDto());
        return "input";
    }

    @PostMapping(value="/playfair")
    public String inputPost(@ModelAttribute("requestDto") RequestDto requestDto, Model model){
        String key = requestDto.getEncryptionKey().toUpperCase();
        String plainText = requestDto.getPlainText().toUpperCase();
        System.out.println("Key : " + key);
        System.out.println("평문 : " + plainText);

        char[][] alphabatBoard = playfairService.setBoard(key);

        System.out.println("알파벳 보드");
        for(int i = 0; i<alphabatBoard.length; i++){
            for(int j = 0; j<alphabatBoard[i].length; j++){
                System.out.print(alphabatBoard[i][j] + " ");
            }
            System.out.println();
        }


        DelSpaceDto delSpaceDto = playfairService.DelSpace(plainText);
        String str = delSpaceDto.getStr();
        String zCheck = delSpaceDto.getZCheck();
        String blankCheck = delSpaceDto.getBlankCheck();
//        System.out.println("공백없는 평문 : " + str);
//        System.out.println("zCheck : " + zCheck);
//        System.out.println("blankCheck : " + blankCheck);


        String encryption = playfairService.strEncryption(alphabatBoard, key, str);
//        System.out.println("매핑된 암호문 : " + encryption);
        model.addAttribute("mapping", encryption);

        encryption = playfairService.RealEncryption(encryption);
//        System.out.println("암호문 : " + encryption);
        model.addAttribute("encryption", encryption);

        String decryption = playfairService.strDecryption(alphabatBoard, key, encryption, zCheck);
//        System.out.println("복호문 : " + decryption);


        decryption = playfairService.realDecryption(blankCheck, decryption);
//        System.out.println("공백있는 복호문 : " + decryption);
        model.addAttribute("decryption", decryption);

        System.out.println("복호 여부 : " + playfairService.finalCheck(plainText, decryption));;

        return "output";
    }
}
