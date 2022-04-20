package com.revature.lostchapterbackend.controller;

import com.revature.lostchapterbackend.dto.LoginDto;
import com.revature.lostchapterbackend.dto.SignUpDto;
import com.revature.lostchapterbackend.exceptions.InvalidLoginException;
import com.revature.lostchapterbackend.exceptions.InvalidParameterException;
import com.revature.lostchapterbackend.exceptions.UserNotFoundException;
import com.revature.lostchapterbackend.model.Users;
import com.revature.lostchapterbackend.service.UserService;
import com.revature.lostchapterbackend.utility.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class ResetPasswordController {
    @Autowired
    private UserService us;

    @Autowired
    private HttpServletRequest req;

    @Autowired
    private ValidateUtil validateUtil;

    @PostMapping(path = "/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestBody SignUpDto dto) throws NoSuchAlgorithmException {

        try {
            Users user = this.us.getUser(dto.getEmail(), dto.getPassword());

            HttpSession session = req.getSession();
            session.setAttribute("currentUser", user);

            return ResponseEntity.status(200).body(user);
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    @PutMapping(path = "/user")
    public ResponseEntity<Object> updateUser(@RequestBody Users user) throws UserNotFoundException {

        try {
            validateUtil.verifyUpdateUser(user);
            HttpSession session = req.getSession();
            Users currentlyLoggedInUser = (Users) session.getAttribute("currentUser");

            Users userToBeUpdated = us.updateUser(currentlyLoggedInUser, user);
            session.setAttribute("currentUser", userToBeUpdated);
            return ResponseEntity.status(200).body(userToBeUpdated);
        } catch (InvalidParameterException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
