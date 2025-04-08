package com.example.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.jobhunter.domain.User;
import com.example.jobhunter.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User handleSaveUser(User user){
        return userRepository.save(user);
    }
    public void handleDeleteUser(long id){
        userRepository.deleteById(id);
    }
    public User fetchOneUser(long id){
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        return null;
    }
    public List<User> fetchAllUser(){
        return userRepository.findAll();
    }
    //update

    public User handleUpdateUser(User reqUser){
        User currentUser = this.fetchOneUser(reqUser.getId());
        if(currentUser != null){
            currentUser.setName(reqUser.getName());
            currentUser.setEmail(reqUser.getEmail());
            currentUser.setPassWord(reqUser.getPassWord());

            currentUser = userRepository.save(currentUser);
        }
        return currentUser;
    }
    public User handleGetUserByEmail(String username){
        return this.userRepository.findByEmail(username);
    }
}
