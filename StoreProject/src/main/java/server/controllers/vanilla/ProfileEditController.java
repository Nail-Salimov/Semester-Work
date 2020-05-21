package server.controllers.vanilla;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import server.bl.services.UserService;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;

@Controller
public class ProfileEditController {

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
    public String getPage(Authentication authentication, Model model){
        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();
        model.addAttribute("userData", UserDataDto.getUserDataDto( userDataModel));
        return "edit_profile";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String editProfile(Authentication authentication,
                              @RequestParam(value = "name", required = false) String newName,
                              @RequestParam(value = "address", required = false) String newAddress){

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();
        if(newName != null && !newName.equals("")) {
            userService.updateUserName(newName, UserDataDto.getUserDataDto(userDataModel));
            userDataModel.setName(newName);
        }

        if (newAddress != null && !newAddress.equals("")) {
            userService.updateUserAddress(newAddress, UserDataDto.getUserDataDto(userDataModel));
            userDataModel.setAddress(newAddress);
        }
        return "redirect:/profile";
    }
}
