package com.srin23.playfair.domain.repository.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DecryptionRequestDto {
    private String encryptionKey;
    private String encryption;
}
