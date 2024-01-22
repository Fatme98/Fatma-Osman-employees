package com.example.employee.service;


import com.example.employee.model.entity.UserEntity;
import com.example.employee.model.service.UserServiceModel;
import com.example.employee.model.view.UserViewModel;

import java.util.List;

public interface UserService {
    UserViewModel findUser(String username);
    UserViewModel findUserByUsername(String username);
    boolean existsUser(String username);
    UserEntity getOrCreateUser(UserServiceModel userServiceModel);
    void createAndLoginUser(UserServiceModel userServiceModel);
    void loginUser(String username,String password);
}
