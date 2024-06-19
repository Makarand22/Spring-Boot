package com.spring.scheduler.service;

import com.spring.scheduler.model.User;
import com.spring.scheduler.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    //Logger log = (Logger) LoggerFactory.getLogger(UserService.class);

    @Scheduled(fixedRate = 5000)
    public void addUser(){
        User user = new User();
        user.setName("mAdmin" + new Random().nextInt(374483));
        user.setName("mAdmin" + new Random().nextInt(374483) + "@gmail.com");
        userRepository.save(user);
        System.out.println("user added in database at : " + new Date().toString());

    }

    @Scheduled(cron = "0/15 * * * * *")
    public void getUserCount(){
        List<User> user = userRepository.findAll();
        System.out.println("get users from database at : " + new Date().toString());
        System.out.println("No of Users : " + user.size());
        //log.info("Users : {}", user );
    }

    @Scheduled(fixedRate = 30000)
    public void deleteAllUsers(){
        List<User> user = userRepository.findAll();
        userRepository.deleteAll(user);
        System.out.println("deleted users from database at : " + new Date().toString());
        System.out.println("No of Users : " + user.size());
    }
}
