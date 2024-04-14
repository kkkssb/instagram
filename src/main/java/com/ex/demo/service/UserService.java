package com.ex.demo.service;

import com.ex.demo.domain.dto.UserDTO;

public interface UserService {
    boolean join(UserDTO userDTO);
    UserDTO login(int phoneNumber, String password);
}
