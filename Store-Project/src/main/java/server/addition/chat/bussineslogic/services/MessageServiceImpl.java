package server.addition.chat.bussineslogic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.addition.chat.bussineslogic.repositories.MessageRepository;
import server.addition.chat.bussineslogic.repositories.RoomRepository;
import server.addition.chat.entity.link.LinkModel;
import server.addition.chat.entity.message.MessageDto;
import server.addition.chat.entity.message.MessageModel;
import server.addition.chat.entity.room.RoomDto;
import server.addition.chat.entity.room.RoomModel;
import server.addition.chat.entity.state.MessageState;
import server.bl.repositories.UserRepository;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;

import java.util.*;

@Component
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public List<MessageDto> findAllMessages(RoomDto roomDto) {
        List<MessageModel> modelList = messageRepository.findAllMessages(castToRoomModel(roomDto));
        List<MessageDto> messageDtoList = new LinkedList<>();

        for (MessageModel model : modelList) {
            messageDtoList.add(castToMessageDto(model));
        }
        return messageDtoList;
    }

    @Override
    public List<MessageDto> findAllNewMessages(RoomDto roomDto, UserDataModel user) {
        List<MessageModel> modelList = messageRepository.findAllNewMessages(castToRoomModel(roomDto), user.getId());
        List<MessageDto> messageDtoList = new LinkedList<>();

        for (MessageModel model : modelList) {
            messageDtoList.add(castToMessageDto(model));
        }
        return messageDtoList;
    }

    @Override
    public List<MessageDto> findAllOldMessages(RoomDto roomDto, UserDataModel user) {
        List<MessageModel> modelList = messageRepository.findAllOldMessages(castToRoomModel(roomDto), user.getId());
        List<MessageDto> messageDtoList = new LinkedList<>();

        for (MessageModel model : modelList) {
            messageDtoList.add(castToMessageDto(model));
        }
        return messageDtoList;
    }

    @Override
    public MessageDto addMessage(MessageDto messageDto) {
       MessageModel model = messageRepository.addMessage(castToMessageModel(messageDto));
       messageDto.setId(model.getId());
       return messageDto;
    }

    @Override
    public Optional<MessageDto> findMessageById(Long messageId) {
        Optional<MessageModel> optionalMessageModel = messageRepository.findMessageById(messageId);
        return optionalMessageModel.map(this::castToMessageDto);
    }

    @Override
    public void readMessage(MessageDto message, Long userId) {
        messageRepository.readMessage(message.getId(), userId);
    }

    private MessageDto castToMessageDto(MessageModel messageModel) {
        return MessageDto.builder()
                .id(messageModel.getId())
                .userDataDto(findUserDto(messageModel.getSenderId()))
                .text(messageModel.getText())
                .build();
    }

    private UserDataDto findUserDto(Long id) {
        Optional<UserDataModel> optional = userRepository.findById(id);
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("user not found");
        }

        return UserDataDto.getUserDataDto(optional.get());
    }

    private RoomModel castToRoomModel(RoomDto roomDto) {
        Optional<RoomModel> optional = roomRepository.findRoomById(roomDto.getId());
        if (!optional.isPresent()) {
            throw new IllegalArgumentException("room not found");
        }
        return optional.get();
    }

    private MessageModel castToMessageModel(MessageDto messageDto) {
        MessageModel messageModel = MessageModel.builder()
                .room(castToRoomModel(messageDto.getRoomModel()))
                .senderId(messageDto.getUserDataDto().getId())
                .text(messageDto.getText())
                .build();

        Set<MessageState> stateSet = new HashSet<>();
        List<LinkModel> links = roomRepository.findLinks(messageModel.getRoom());
        for (LinkModel link : links) {
            stateSet.add(MessageState.builder()
                    .message(messageModel)
                    .isRead(false)
                    .userId(link.getUserId())
                    .build());
        }
        messageModel.setMessageStates(stateSet);
        return messageModel;
    }
}
