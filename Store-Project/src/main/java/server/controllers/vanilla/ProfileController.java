package server.controllers.vanilla;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;

@Controller
public class ProfileController {

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getPage(Authentication authentication, Model model){

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();
        model.addAttribute("userData", UserDataDto.getUserDataDto( userDataModel));

        return "profile";
    }
}
