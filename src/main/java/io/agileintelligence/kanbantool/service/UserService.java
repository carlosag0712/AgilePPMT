package io.agileintelligence.kanbantool.service;

import io.agileintelligence.kanbantool.domain.User;
import io.agileintelligence.kanbantool.exceptions.UsernameAlreadyExistException;
import io.agileintelligence.kanbantool.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public User saveOrUpdateUser(User newUser){

        try {
            newUser.setUsername(newUser.getUsername());
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setConfirmPassword("");
            return userRepository.save(newUser);
        }catch (Exception e){
            throw new UsernameAlreadyExistException("Username "+"'"+newUser.getUsername()+"'"+" already exists");

        }

    }
}
