package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.dtos.JwtResponseDTO;
import com.personal.iphonehouse.dtos.LoginRequest;
import com.personal.iphonehouse.dtos.ResetPasswordDto;
import com.personal.iphonehouse.dtos.UserDTO;
import com.personal.iphonehouse.services.JwtService;
import com.personal.iphonehouse.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v${api.version}/auth")
//@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class AuthenticationController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

//    @GetMapping("/user")
//    public SystemUserDto getLoggedUser(HttpServletRequest req) {
//        return userService.getLoggedUser(req);
//    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequest authRequestDto) {
        return ResponseEntity.ok(userService.loginUser(authRequestDto));
    }

    @PostMapping("/register")
    public UserDTO registerSuperadmin(@RequestBody LoginRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/validateJwt")
    public ResponseEntity<Boolean> validateToken(@RequestBody JwtResponseDTO tokenRequest) {
        return ResponseEntity.ok(jwtService.validateToken(tokenRequest.getToken()));
    }

    @PostMapping("/refreshJwt")
    public ResponseEntity<JwtResponseDTO> refreshJwt(HttpServletRequest req) {
        return ResponseEntity.ok(jwtService.refreshJwt(req));
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<Boolean> resetPassword(HttpServletRequest req, @RequestBody ResetPasswordDto resetPasswordDto) {
        return ResponseEntity.ok(userService.resetPassword(req, resetPasswordDto));
    }

//    @PutMapping("/changePassword")
//    public ResponseEntity<Boolean> changePassword(HttpServletRequest req, @RequestBody ResetPasswordDto resetPasswordDto) {
//        return ResponseEntity.ok(userService.resetPassword(req, resetPasswordDto));
//    }
}