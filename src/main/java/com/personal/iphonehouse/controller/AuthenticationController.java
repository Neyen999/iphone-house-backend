package com.personal.iphonehouse.controller;

import com.personal.iphonehouse.dto.JwtResponseDTO;
import com.personal.iphonehouse.dto.LoginRequest;
import com.personal.iphonehouse.dto.UserDTO;
import com.personal.iphonehouse.service.JwtService;
import com.personal.iphonehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v${api.version}")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class AuthenticationController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

//    @GetMapping("/user")
//    public SystemUserDto getLoggedUser(HttpServletRequest req) {
//        return userService.getLoggedUser(req);
//    }

    @PostMapping("/auth/login")
    public JwtResponseDTO login(@RequestBody LoginRequest authRequestDto) {
        return userService.loginUser(authRequestDto);
    }

    @PostMapping("/auth/register")
    public UserDTO registerSuperadmin(@RequestBody LoginRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/auth/validateJwt")
    public ResponseEntity<Boolean> validateToken(@RequestBody JwtResponseDTO tokenRequest) {
        return ResponseEntity.ok(jwtService.validateToken(tokenRequest.getToken()));
    }

    @PostMapping("/auth/refreshJwt")
    public ResponseEntity<JwtResponseDTO> refreshJwt(@RequestBody JwtResponseDTO tokenRequest) {
        return ResponseEntity.ok(jwtService.refreshJwt(tokenRequest.getToken()));
    }
}