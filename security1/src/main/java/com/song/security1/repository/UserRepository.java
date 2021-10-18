package com.song.security1.repository;

import com.song.security1.model.User;
import com.sun.xml.bind.v2.model.core.ID;
import org.springframework.data.jpa.repository.JpaRepository;


//CRUD 함수를 JpaRepository가 들고 있음.
// @Repository라는 어노테이션이 없어도 IoC 되요. 이유는 JpaRepository를 상송했기 때문에...
public interface UserRepository extends JpaRepository<User, Integer>{ // 자동으로 bin으로 등록이 된다.
    //findBy 규칙 ->Username 문법
    // select * from user where username = 1?
    public User findByUsername(String username); //Jpa Query methods
}