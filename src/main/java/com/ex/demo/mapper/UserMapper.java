package com.ex.demo.mapper;

import com.ex.demo.domain.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insertUser(UserDTO userDTO);

    UserDTO login(int phoneNumber, String password);
}
