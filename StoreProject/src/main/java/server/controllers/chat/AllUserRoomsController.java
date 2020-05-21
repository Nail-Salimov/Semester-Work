package server.controllers.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import server.addition.chat.bussineslogic.services.MessageService;
import server.addition.chat.bussineslogic.services.RoomService;
import server.addition.chat.entity.message.MessageDto;
import server.addition.chat.entity.room.RoomDto;
import server.bl.services.UserService;
import server.entities.user.model.UserDataModel;
import server.entities.wsmessage.Lobby;
import server.security.details.UserDetailImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class AllUserRoomsController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/lobbies")
    public String getAllLobbies(Authentication authentication, Model model){

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        List<RoomDto> roomDtoList = roomService.findRoomsByUserId(userDataModel.getId());
        Map<Long, Lobby> lobbies = new HashMap<>();

        for (RoomDto room : roomDtoList){
            List<MessageDto> messages = messageService.findAllNewMessages(room, userDataModel);
            if(messages.size() > 0) {
                Lobby lobby = Lobby.builder()
                        .count(messages.size())
                        .lastText(messages.get(messages.size() - 1).getText())
                        .roomId(room.getId())
                        .type("show")
                        .user(messages.get(messages.size() - 1).getUserDataDto())
                        .build();
                lobbies.put(lobby.getRoomId(), lobby);
            }
        }

        for (RoomDto room : roomDtoList){
            List<MessageDto> messages = messageService.findAllOldMessages(room, userDataModel);
            if(messages.size() > 0 && !lobbies.containsKey(room.getId())) {
                Lobby lobby = Lobby.builder()
                        .count(0)
                        .lastText(messages.get(messages.size() - 1).getText())
                        .roomId(room.getId())
                        .type("show")
                        .user(messages.get(messages.size() - 1).getUserDataDto())
                        .build();
                lobbies.put(lobby.getRoomId(), lobby);
            }
        }

        model.addAttribute("lobbies",  new ArrayList<Lobby>(lobbies.values()));
        model.addAttribute("userData", userDataModel);
        return "lobbies";
        //simpMessagingTemplate.convertAndSend("/ws/chat/lobby/" + userDataModel.getId(), new LobbyMessage("show", new ArrayList<Lobby>(lobbies.values())));
    }

}
