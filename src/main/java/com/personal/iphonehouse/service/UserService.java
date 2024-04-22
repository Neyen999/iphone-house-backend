package com.personal.iphonehouse.service;

import com.personal.iphonehouse.dto.JwtResponseDTO;
import com.personal.iphonehouse.dto.LoginRequest;
import com.personal.iphonehouse.dto.UserDTO;
import com.personal.iphonehouse.model.Role;
import com.personal.iphonehouse.model.User;
import com.personal.iphonehouse.repositorie.RoleRepository;
import com.personal.iphonehouse.repositorie.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
//    @Autowired
//    private AesEncrypter aesEncrypter;

    public User findById(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error"));

        return user;
    }

    public UserDTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("No hay");
        }

        return modelMapper.map(user, UserDTO.class);

    }

    @Transactional
    public JwtResponseDTO loginUser(LoginRequest authRequestDto) {
        User user = userRepository.findByUsername(authRequestDto.getUsername());

        if (user == null) {
            throw new UsernameNotFoundException("Username: " + authRequestDto.getUsername() + " not found");
        }

        String username = authRequestDto.getUsername();
        String password = authRequestDto.getPassword();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));


        return jwtService.GenerateToken(username);
    }

    @Transactional
    public UserDTO registerUser(LoginRequest registerRequestDto) {
        Role role = roleRepository.findById(1).orElseThrow(() -> new RuntimeException("Error"));

        User newUser = modelMapper.map(registerRequestDto, User.class);
        newUser.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        newUser.setRoles(List.of(role));
        userRepository.save(newUser);

        return modelMapper.map(newUser, UserDTO.class);

    }
//
//    @Transactional
//    public SystemUserDto getLoggedUser(HttpServletRequest req) {
//        String token = jwtService.getTokenFromHeader(req);
//        String username = jwtService.extractUsername(token);
//
//        SystemUser user = userRepository.findByUsername(username);
//
//        if (user == null) {
//            throw new RuntimeException("User not found");
//        }
//
//        SystemUserDto userDto = modelMapper.map(user, SystemUserDto.class);
//
//        return userDto;
//    }
//
//    @Transactional
//    public SystemUserDto updateUser(SystemUserDto request) {
//        SystemUser user = userRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("User not found"));
//
//        SystemUser updatedUser = modelMapper.map(request, SystemUser.class);
//
//        userRepository.save(updatedUser);
//
//        return modelMapper.map(updatedUser, SystemUserDto.class);
//    }
//
//    public boolean existsByEmail(String username) {
//        SystemUser user = userRepository.findByUsername(username);
//        return user != null;
//    }
//
//    public boolean existsByDocumentData(String documentData) {
//        SystemUser user = userRepository.findByDocumentData(documentData);
//        return user != null;
//    }

    public UserDTO getLoggedUser(HttpServletRequest req) {
        String token = jwtService.obtainTokenFromHeader(req);
        String username = jwtService.extractUsername(token);

        return this.findByUsername(username);
    }
}