package com.ex.demo.service;

import com.ex.demo.domain.dto.UserDTO;
import com.ex.demo.domain.dto.UserFileDTO;
import com.ex.demo.mapper.UserFileMapper;
import com.ex.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserFileMapper userFileMapper;
    @Value("${file.dir}")
    private String saveFolder;

    @Override
    public boolean join(UserDTO userDTO) {
        return userMapper.insertUser(userDTO)==1;
    }

    @Override
    public UserDTO login(int phoneNumber, String password) {
        return userMapper.login(phoneNumber,password);
    }

    @Override
    public boolean insertFile(MultipartFile[] files,String name) throws IOException {
        int row = 1;
        if(row != 1) {
            System.out.println("row");
            return false;
        }
        if(files == null || files.length == 0) {
            System.out.println("empty");
            return true;
        }
        else {
            //방금 등록한 게시글 번호
            System.out.println("habe");
            boolean flag = false;
            System.out.println(name);
            System.out.println(files.length);
            for(int i=0;i<files.length;i++) {
                System.out.println("habe23");
                MultipartFile file = files[i];
                //apple.png
                String orgname = file.getOriginalFilename();
                System.out.println(orgname);
                System.out.println("habe3");

                //5
                int lastIdx = orgname.lastIndexOf(".");
                System.out.println(lastIdx);
                //.png
                String extension = orgname.substring(lastIdx);

                LocalDateTime now = LocalDateTime.now();
                String time = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

                //20231005103911237랜덤문자열.png
                String systemname = time+ UUID.randomUUID().toString()+extension;
                System.out.println(systemname);

                //실제 저장될 파일의 경로
                String path = saveFolder+systemname;

                UserFileDTO fdto = new UserFileDTO();
                fdto.setSystemname(systemname);
                fdto.setOrgname(orgname);
                fdto.setName(name);

                //실제 파일 업로드
                file.transferTo(new File(path));
                System.out.println("last");
                System.out.println(fdto);

                flag = userFileMapper.insertFile(fdto) == 1;

                if(!flag) {
                    //업로드 했던 파일 삭제, 게시글 데이터 삭제
                    return flag;
                }
            }
        }
        return true;
    }
    @Override
    public ResponseEntity<Resource> forProfile(String name) throws IOException {
        UserDTO user = userMapper.findById(name);
        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<UserFileDTO> files = userFileMapper.getFiles(user.getName());
        if(files == null || files.size()==0) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        int lastIndex = files.size() - 1;
        String systemname = files.get(lastIndex).getSystemname();
        Path path = Paths.get(saveFolder + systemname);
        String contentType = Files.probeContentType(path);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        System.out.println("path: "+path);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

}
