package server.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.bl.services.UserService;
import server.entities.user.dto.UserDataDto;

@RestController
public class SignUpRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/api/signUp")
    public ResponseEntity<String> register(@RequestBody UserDataDto userDataDto){

        if(userService.saveUser(userDataDto)){
            return ResponseEntity.ok().body("ok");
        }else {
            return ResponseEntity.ok().body("Mail already registered");
        }

    }

}
