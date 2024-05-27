package com.ex.demo.domain.dto;

import lombok.Data;

@Data
public class BoardDTO {
    private Long boardnum;
    private String contents;
    private String nickName;
    private int likes;
    private int reply;
    private String date;
    private String modify;
}
