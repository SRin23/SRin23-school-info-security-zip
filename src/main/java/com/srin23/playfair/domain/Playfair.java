package com.srin23.playfair.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Playfair {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="encryption_key", nullable = false)
    private String encryptionKey;   //암호키

    @Column(name="plain_text", nullable = false)
    private String plainText;   //평문

    @Column
    private String cryptogram;  //암호문
}
