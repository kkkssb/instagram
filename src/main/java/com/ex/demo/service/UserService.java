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
    boolean insertFile(MultipartFile[] files,String name) throws IOException;
    public ResponseEntity<Resource> forProfile(String name) throws IOException;

}
