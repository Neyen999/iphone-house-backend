package com.personal.iphonehouse.controllers;

import com.personal.iphonehouse.dtos.UserDTO;
import com.personal.iphonehouse.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v${api.version}/user")
//@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/loggedUser")
    public ResponseEntity<UserDTO> getLoggedUser(HttpServletRequest req) {
        return ResponseEntity.ok(userService.getLoggedUser(req));
    }
}
