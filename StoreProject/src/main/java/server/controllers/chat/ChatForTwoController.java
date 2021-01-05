package server.controllers.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import server.addition.chat.bussineslogic.services.MessageService;
import server.addition.chat.bussineslogic.services.RoomService;
import server.addition.chat.entity.link.LinkDto;
import server.addition.chat.entity.message.MessageDto;
import server.addition.chat.entity.room.RoomDto;
import server.bl.services.UserService;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;
import server.entities.wsmessage.Lobby;
import server.entities.wsmessage.LobbyMessage;
import server.security.details.UserDetailImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class ChatForTwoController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/chat/{chat-id:.+}", method = RequestMethod.GET)
    public String getPage(@PathVariable("chat-id") Long chatId,
                          Authentication authentication, Model model) {

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        if (chatId == null) {
            return "redirect:/";
        }

        Optional<RoomDto> optionalRoom = roomService.findRoomById(chatId);
        if (!optionalRoom.isPresent()) {
            return "redirect:/";
        }

        model.addAttribute("room", optionalRoom.get());
        model.addAttribute("userData", userDataModel);

        List<MessageDto> messages = messageService.findAllMessages(optionalRoom.get());
        List<MessageDto> newMessages = messageService.findAllNewMessages(optionalRoom.get(),userDataModel);
        for (MessageDto message : newMessages){
            messageService.readMessage(message, userDataModel.getId());
        }

        simpMessagingTemplate.convertAndSend("/ws/chat/lobby/" + userDataModel.getId(), getLobbyMessage(optionalRoom.get()));
        model.addAttribute("messages", messages);
        model.addAttribute("otherUser", getOtherUser(optionalRoom.get(), userDataModel.getId()));
        return "chat_ws";

    }

    private UserDataDto getOtherUser(RoomDto roomDto, Long id) {
        Set<LinkDto> links = roomDto.getLinkDtoSet();
        for (LinkDto link : links){
            if(!link.getUserDto().getId().equals(id)){
                return link.getUserDto();
            }
        }
        throw new IllegalArgumentException("other user not find");
    }

    private LobbyMessage getLobbyMessage(RoomDto roomDto) {

        Lobby lobby = Lobby.builder()
                .user(null)
                .type("read")
                .roomId(roomDto.getId())
                .lastText(null)
                .count(1)
                .build();

        List<Lobby> list = new LinkedList<>();
        list.add(lobby);
        return LobbyMessage.builder()
                .type("read")
                .lobbies(list)
                .build();
    }
}
