package com.srin23.playfair.domain.repository.dto.response;

import lombok.Getter;

@Getter
public class DelSpaceDto {
    private String zCheck;
    private String blankCheck;
    private String str;

    public DelSpaceDto(String zCheck, String blankCheck, String str){
        this.zCheck = zCheck;
        this.blankCheck = blankCheck;
        this.str = str;
    }
}
