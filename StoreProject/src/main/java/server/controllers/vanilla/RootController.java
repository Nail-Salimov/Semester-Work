package server.controllers.vanilla;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @PreAuthorize("permitAll()")
    @GetMapping("/")
    public String getRootPage(Authentication authentication){

        return "redirect:/store";
    }
}
