package com.ex.demo.domain.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long idx;
    private int phoneNumber;
    private String name;
    private String nickName;
    private String password;
}
