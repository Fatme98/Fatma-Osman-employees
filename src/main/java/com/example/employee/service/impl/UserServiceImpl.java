package com.example.employee.service.impl;

import com.example.employee.error.PasswordNotProvidedException;
import com.example.employee.error.UserNotFoundException;
import com.example.employee.model.entity.RoleEntity;
import com.example.employee.model.entity.UserEntity;
import com.example.employee.model.service.UserServiceModel;
import com.example.employee.model.view.UserViewModel;
import com.example.employee.repository.UserRepository;
import com.example.employee.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    private static final Logger LOGGER= LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           PasswordEncoder passwordEncoder, @Qualifier("userDetailsServiceImpl")
                                   UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.modelMapper=modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }


    @Override
    public UserViewModel findUser(String username) {
        UserEntity userEntity =this.userRepository.findByUsername(username).orElse(null);
        UserViewModel userViewModel=new UserViewModel();
        if(userEntity ==null){
            throw new UserNotFoundException(username);
        }else{
            userViewModel=this.modelMapper.map(userEntity,UserViewModel.class);
        }
        return userViewModel;
    }

    @Override
    public UserViewModel findUserByUsername(String username) {
        UserEntity userEntity= this.userRepository.findByUsername(username).orElse(null);
        if(userEntity==null){
            throw new UserNotFoundException(username);
        }
        UserViewModel userViewModel=this.modelMapper.map(userEntity,UserViewModel.class);
        return userViewModel;
    }

    @Override
    public boolean existsUser(String username) {
        Objects.requireNonNull(username);
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public UserEntity getOrCreateUser(UserServiceModel userServiceModel) {
        Objects.requireNonNull(userServiceModel.getPassword());
        Optional<UserEntity>userEntityOpt=userRepository.findByUsername(userServiceModel.getUsername());
        return userEntityOpt.orElseGet(()->createUser(userServiceModel));
    }

    @Override
    public void createAndLoginUser(UserServiceModel userServiceModel) {
        UserEntity newUser=createUser(userServiceModel);
        UserDetails userDetails=userDetailsService.loadUserByUsername(newUser.getUsername());
        Authentication authentication=new UsernamePasswordAuthenticationToken(userDetails,userServiceModel.getPassword(),
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public void loginUser(String username,String password) {
        UserDetails userDetails=userDetailsService.loadUserByUsername(username);
        Authentication authentication=new UsernamePasswordAuthenticationToken(userDetails,password,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserEntity createUser(UserServiceModel userServiceModel){
        UserEntity userEntity=new UserEntity();
        LOGGER.info("Creating a new user with username [GDPR].");
        userEntity=this.createUserWithRoles(userServiceModel,"USER");
        return userRepository.save(userEntity);
    }
    private UserEntity createUserWithRoles(UserServiceModel userServiceModel,String role){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userServiceModel.getUsername());
        userEntity.setEmail(userServiceModel.getEmail());
        userEntity.setFirstName(userServiceModel.getFirstName());
        userEntity.setLastName(userServiceModel.getLastName());
        if(userServiceModel.getPassword()!=null){
            userEntity.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));
        }else{
            throw new PasswordNotProvidedException();
        }
        RoleEntity userRole=new RoleEntity();
        userRole.setRole(role);
        userEntity.setRoles(List.of(userRole));
        return userEntity;
    }

}
