package com.ex.demo.service;

import com.ex.demo.domain.dto.UserDTO;
import com.ex.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public boolean join(UserDTO userDTO) {
        return userMapper.insertUser(userDTO)==1;
    }
}
