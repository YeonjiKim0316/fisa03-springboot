package com.example.demo.service;

import com.example.demo.model.dto.UserDto;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private UserRepository urepo;

    // 생성자
    public UserService(UserRepository urepo) {
        this.urepo = urepo;
    }

//    join	회원 가입 - setter void
    public void join(UserDto dto) {
        User user = dto.toEntity(); // dto -> entity로 변환

        // db에 똑같은 id를 가진 회원이 있는지 확인
        if (urepo.findById(user.getId()).isPresent()) { // true / false로 변환
            log.info("service----" + String.valueOf(urepo.findById(user.getId())));
            throw new RuntimeException("이미 존재하는 사용자입니다."); // 있으면 예외 발생
        } else {
            // 없으면
            urepo.save(user);
        }
    }


//    login	로그인 entity -> dto
    public UserDto login(UserDto dto){
        // db에서 dto.id와 일치하는 값을 조회
        Optional<User> result = urepo.findById(dto.getId()); // test, 1234
            // 있으면 dto.pass 와 db의 pass가 일치하는지 확인
            if (result.isPresent()) {
                User selected = result.get();
                if (selected.getPass().equals(dto.getPass())) {  // db의 pw와 form으로 입력받은 pw 일치여부 확인
                    // 일치하면 다음 화면으로 데이터(사용자 정보) 이동
                    return selected.toDto();
                    }
                }
        // 그렇지 않으면 예외 발생
        throw new RuntimeException("아이디나 비밀번호를 확인하세요."); // 있으면 예외 발생
    }
}
