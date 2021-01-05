package server.controllers.chat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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
import server.entities.wsmessage.Message;
import server.security.details.UserDetailImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class ChatMessageWSController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/message")
    @PreAuthorize("isAuthenticated()")
    public void getMessages(Message message, Authentication authentication) {

        UserDetailImpl userDetail = (UserDetailImpl) authentication.getPrincipal();
        UserDataModel userDataModel = userDetail.getUser();

        if (!userDataModel.getId().equals(message.getFrom())) {
            throw new IllegalArgumentException("user not found");
        }

        Optional<RoomDto> optionalRoomDto = roomService.findRoomById(message.getRoomId());
        if (!optionalRoomDto.isPresent()) {
            throw new IllegalArgumentException("room not found");
        }

        RoomDto roomDto = optionalRoomDto.get();
        UserDataDto userDataDto = UserDataDto.getUserDataDto(userDataModel);

        if (userIsBelong(optionalRoomDto.get(), userDataModel.getId())) {
            MessageDto messageDto = messageService.addMessage(getMessageDto(message, userDataDto, roomDto));
            message = castToMessage(messageDto);
            message.setUser(userDataDto);
            message.setMessageId(messageDto.getId());

            simpMessagingTemplate.convertAndSend("/ws/chat/messages/" + roomDto.getId(), message);

            UserDataDto otherUserDto = getOtherUser(roomDto, userDataModel.getId());
            simpMessagingTemplate.convertAndSend("/ws/chat/lobby/" + otherUserDto.getId(), getLobbyMessage(message, roomDto));
        } else {
            throw new IllegalArgumentException("user is not belong to room");
        }
    }


    private boolean userIsBelong(RoomDto roomDto, Long userId) {
        Set<LinkDto> links = roomDto.getLinkDtoSet();
        for (LinkDto link : links) {
            if (link.getUserDto().getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    private MessageDto getMessageDto(Message message, UserDataDto userDataDto, RoomDto roomDto) {
        return MessageDto.builder()
                .userDataDto(userDataDto)
                .text(message.getMessage())
                .roomModel(roomDto)
                .build();
    }

    private Message castToMessage(MessageDto messageDto) {
        return Message.builder()
                .from(messageDto.getUserDataDto().getId())
                .message(messageDto.getText())
                .roomId(messageDto.getRoomModel().getId())
                .build();
    }

    private UserDataDto getOtherUser(RoomDto roomDto, Long id) {
        Set<LinkDto> links = roomDto.getLinkDtoSet();
        for (LinkDto link : links) {
            if (!link.getUserDto().getId().equals(id)) {
                return link.getUserDto();
            }
        }
        throw new IllegalArgumentException("other user not find");
    }

    private LobbyMessage getLobbyMessage(Message message, RoomDto roomDto) {

        Optional<UserDataDto> optionalUser = userService.findUserById(message.getFrom());
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("user not found");
        }

        Lobby lobby = Lobby.builder()
                .user(optionalUser.get())
                .type("new")
                .roomId(roomDto.getId())
                .lastText(message.getMessage())
                .count(1)
                .build();

        List<Lobby> list = new LinkedList<>();
        list.add(lobby);
        return LobbyMessage.builder()
                .type("new")
                .lobbies(list)
                .build();
    }


}
