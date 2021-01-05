package server.controllers.vanilla;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;

//@Controller
public class LobbiesController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lobbies")
    public String getPage(Authentication authentication, Model model){
        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        model.addAttribute("userData", userDataModel);
        return "lobbies";
    }
}
