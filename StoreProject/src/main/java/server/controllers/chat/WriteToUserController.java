package server.controllers.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import server.addition.chat.bussineslogic.services.RoomService;
import server.addition.chat.entity.room.RoomDto;
import server.bl.services.UserService;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;
import server.security.details.UserDetailImpl;

import java.util.Optional;

@Controller
public class WriteToUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/writeTo/{user-id:.+}", method = RequestMethod.GET)
    public String getFile(@PathVariable(name = "user-id") Long otherUserId,
                          Authentication authentication) {

        if (otherUserId == null) {
            return "redirect:/";
        }

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        Optional<UserDataDto> optionalUserDto = userService.findUserById(otherUserId);
        if (!optionalUserDto.isPresent()) {
            return "redirect:/";
        }

        Optional<RoomDto> optionalRoom = roomService.findRoomByUsers(userDataModel.getId(), otherUserId);
        RoomDto roomDto = null;
        roomDto = optionalRoom.orElseGet(() -> roomService.createNewRoom(userDataModel.getId(), otherUserId));

        return "redirect:/chat/" + roomDto.getId();
    }
}
