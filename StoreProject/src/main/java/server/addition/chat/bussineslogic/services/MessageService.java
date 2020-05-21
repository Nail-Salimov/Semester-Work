package server.addition.chat.bussineslogic.services;

import server.addition.chat.entity.message.MessageDto;
import server.addition.chat.entity.room.RoomDto;
import server.entities.user.model.UserDataModel;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    List<MessageDto> findAllMessages(RoomDto roomDto);
    List<MessageDto> findAllNewMessages(RoomDto roomDto, UserDataModel user);
    List<MessageDto> findAllOldMessages(RoomDto roomDto, UserDataModel user);
    MessageDto addMessage(MessageDto messageDto);
    Optional<MessageDto> findMessageById(Long messageId);
    void readMessage(MessageDto message, Long userId);
}
