package com.ex.demo.domain.dto;

import lombok.Data;

@Data
public class ReplyDTO {
    private int replynum;
    private Long boardnum;
    private String contents;
    private String nickName;
}
