package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // 실제 DB에서 테스트하겠다는 의미
public class UserRepoTest {

    @Autowired
    UserRepository repo;

    @Test
    public void insertTest() {
        // given
        //	User user = new User("test1", ""신짱구"", "1234", "GUEST");
        User user = User.builder().id("test1").name("신짱구").pass("1234").build();
        repo.save(user); // 저장하고 바로 엔티티 매니저에 반영

        // when
        User selected = repo.findById("test1").get();

        // then
        Assertions.assertEquals(user, selected, "제대로 user 객체가 들어가지 않았습니다.");
    }


}