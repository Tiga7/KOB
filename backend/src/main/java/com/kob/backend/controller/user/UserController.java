package com.kob.backend.controller.user;

import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Gan
 */
@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/user/all")
    public List<User> getAll(){
        return userMapper.selectList(null);
    }
    @RequestMapping("/user/{userId}")
    public User getById(@PathVariable int userId){
        return userMapper.selectById(userId);
    }

    @GetMapping("/user/add/{username}/{password}")
    public String addUser(@PathVariable String username,@PathVariable String password){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encode = passwordEncoder.encode(password);

        User user=new  User(username,encode);
        userMapper.insert(user);
        return "insert user successfully!";
    }
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable int id){
        userMapper.deleteById(id);
        return "delete user successfully";
    }

}
