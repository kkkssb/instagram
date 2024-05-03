package com.ex.demo.service;

import com.ex.demo.domain.dto.UserDTO;
import com.ex.demo.domain.dto.UserFileDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    boolean join(UserDTO userDTO);
    UserDTO login(int phoneNumber, String password);
    boolean insertFile(Long idx) throws IOException;
    public ResponseEntity<Resource> forProfile(String nickName) throws IOException;
    UserDTO userInfo(String nickName);
    boolean modify(UserDTO userDTO);
    boolean updateFile(MultipartFile[] files,Long idx)throws IOException;
    UserFileDTO userfileInfo(Long idx);
}
