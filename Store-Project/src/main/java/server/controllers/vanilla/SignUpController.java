package server.controllers.vanilla;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import server.bl.services.UserService;
import server.entities.user.dto.UserDataDto;

@Controller
public class SignUpController {

    @Autowired
    private UserService userService;

    @PreAuthorize("permitAll()")
    @GetMapping("/signUp")
    public String getPage(Authentication authentication) {
        if (authentication != null) {
            return "redirect:/store";
        } else {
            return "signUp";
        }
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/signUp")
    public String signUp(UserDataDto form) {
        if(userService.saveUser(form)) {
            return "redirect:/signIn";
        }else {
            return "redirect:/signUp";
        }
    }

}
