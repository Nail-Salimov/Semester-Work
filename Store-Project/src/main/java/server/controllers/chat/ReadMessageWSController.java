package server.controllers.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import server.addition.chat.bussineslogic.services.MessageService;
import server.addition.chat.bussineslogic.services.RoomService;
import server.addition.chat.entity.message.MessageDto;
import server.addition.chat.entity.room.RoomDto;
import server.bl.services.UserService;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;
import server.entities.wsmessage.Lobby;
import server.entities.wsmessage.LobbyMessage;
import server.entities.wsmessage.MessageRead;
import server.security.details.UserDetailImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Controller
public class ReadMessageWSController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @MessageMapping("/read_message")
    @PreAuthorize("isAuthenticated()")
    public void readMessage(MessageRead message, Authentication authentication) {

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        if(message.getMessageId() == null){
            throw new IllegalArgumentException("invalid message id");
        }
        Optional<MessageDto> optionalMessageDto = messageService.findMessageById(message.getMessageId());
        if(!optionalMessageDto.isPresent()){
            throw new IllegalArgumentException("message is not exist");
        }
        Optional<RoomDto> optionalRoomDto = roomService.findRoomById(message.getRoomId());
        if(!optionalRoomDto.isPresent()){
            throw new IllegalArgumentException("room not found");
        }

        messageService.readMessage(optionalMessageDto.get(), userDataModel.getId());
        simpMessagingTemplate.convertAndSend("/ws/chat/lobby/" + message.getFrom(), getLobbyMessage(message, optionalRoomDto.get()));
    }

    private LobbyMessage getLobbyMessage(MessageRead message, RoomDto roomDto) {

        Optional<UserDataDto> optionalUser = userService.findUserById(message.getFrom());
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("user not found");
        }

        Lobby lobby = Lobby.builder()
                .user(optionalUser.get())
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
