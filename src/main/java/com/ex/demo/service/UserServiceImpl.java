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
import java.util.ArrayList;
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
    public boolean insertFile(Long idx) throws IOException {
        boolean flag = false;
        UserFileDTO fdto = new UserFileDTO();
                fdto.setSystemname("systemname");
                fdto.setOrgname("orgname");
                fdto.setIdx(idx);
        flag = userFileMapper.insertFile(fdto) == 1;
        System.out.println(fdto);
                if(!flag) {
                    //업로드 했던 파일 삭제, 게시글 데이터 삭제
                    return flag;
                }
        return true;
    }

    //    @Override
//    public boolean insertFile(MultipartFile[] files,String name) throws IOException {
//        int row = 1;
//        if(row != 1) {
//            System.out.println("row");
//            return false;
//        }
//        if(files == null || files.length == 0) {
//            System.out.println("empty");
//            return true;
//        }
//        else {
//            boolean flag = false;
//            for(int i=0;i<files.length;i++) {
//                MultipartFile file = files[i];
//
//                String orgname = file.getOriginalFilename();
//
//                int lastIdx = orgname.lastIndexOf(".");
//
//                String extension = orgname.substring(lastIdx);
//
//                LocalDateTime now = LocalDateTime.now();
//                String time = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
//
//                //20231005103911237랜덤문자열.png
//                String systemname = time+ UUID.randomUUID().toString()+extension;
//                System.out.println(systemname);
//
//                //실제 저장될 파일의 경로
//                String path = saveFolder+systemname;
//
//                UserFileDTO fdto = new UserFileDTO();
//                fdto.setSystemname(systemname);
//                fdto.setOrgname(orgname);
//                fdto.setName(name);
//
//                //실제 파일 업로드
//                file.transferTo(new File(path));
//
//                flag = userFileMapper.insertFile(fdto) == 1;
//
//                if(!flag) {
//                    //업로드 했던 파일 삭제, 게시글 데이터 삭제
//                    return flag;
//                }
//            }
//        }
//        return true;
//    }
    @Override
    public ResponseEntity<Resource> forProfile(String nickName) throws IOException {
        UserDTO user = userMapper.findById(nickName);
        if(user == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        List<UserFileDTO> files = userFileMapper.getFiles(user.getIdx());

        if(files == null || files.size()==0) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        int lastIndex = files.size() - 1;
        String systemname = files.get(lastIndex).getSystemname();
        if("systemname".equals(systemname)) {
        return new ResponseEntity<>(HttpStatus.OK);
        }
        Path path = Paths.get(saveFolder + systemname);
        String contentType = Files.probeContentType(path);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        System.out.println(systemname);
        System.out.println("path: "+path);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @Override
    public UserDTO userInfo(String nickName) {
        return userMapper.findById(nickName);
    }

    @Override
    public boolean modify(UserDTO userDTO) {
        return userMapper.modify(userDTO)==1;
    }

    @Override
    public boolean updateFile(MultipartFile[] files,Long idx) throws IOException {
        List<UserFileDTO> org_file_list = userFileMapper.getFiles(idx);
        if(org_file_list.size()==0 && (files == null || files.length == 0)) {
            return true;
        }
        else {
            if (files != null) {
                boolean flag = false;
                //후에 비즈니스 로직 실패 시 원래대로 복구하기 위해 업로드 성공했던 파일들도 삭제해주어야 한다.
                //업로드 성공한 파일들의 이름을 해당 리스트에 추가하면서 로직을 진행한다.
                ArrayList<String> sysnames = new ArrayList<>();
                System.out.println("service : " + files.length);
                for (int i = 0; i < files.length; i++) {
                    MultipartFile file = files[i];
                    String orgname = file.getOriginalFilename();
                    System.out.println(orgname);
                    //수정의 경우 중간에 있는 파일은 수정이 되지 않은 경우도 있다.
                    //그런 경우의 file의 orgname은 null 이거나 "" 이다.
                    //따라서 업로드가 될 필요가 없으므로 continue로 다음 파일로 넘어간다.
                    if (orgname == null || orgname.equals("")) {
                        continue;
                    }
                    int lastIdx = orgname.lastIndexOf(".");
                    String extension = orgname.substring(lastIdx);
                    LocalDateTime now = LocalDateTime.now();
                    String time = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
                    String systemname = time + UUID.randomUUID().toString() + extension;
                    sysnames.add(systemname);

                    String path = saveFolder + systemname;
                    UserFileDTO fdto = new UserFileDTO();
                    fdto.setSystemname(systemname);
                    fdto.setOrgname(orgname);
                    fdto.setIdx(idx);
                    file.transferTo(new File(path));

                    flag = userFileMapper.updateFile(fdto) == 1;
                    if (!flag) {
                        break;
                    }
                }
            }

            return true;
        }
    }

    @Override
    public UserFileDTO userfileInfo(Long idx) {
        return userFileMapper.fileInfo(idx);
    }

}
