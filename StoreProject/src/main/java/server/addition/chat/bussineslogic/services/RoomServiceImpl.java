package server.addition.chat.bussineslogic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.addition.chat.bussineslogic.repositories.RoomRepository;
import server.addition.chat.entity.link.LinkDto;
import server.addition.chat.entity.link.LinkModel;
import server.addition.chat.entity.message.MessageDto;
import server.addition.chat.entity.message.MessageModel;
import server.addition.chat.entity.room.RoomDto;
import server.addition.chat.entity.room.RoomModel;
import server.bl.repositories.UserRepository;
import server.entities.user.dto.UserDataDto;
import server.entities.user.model.UserDataModel;

import java.util.*;

@Component
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<RoomDto> findRoomById(Long id) {
        Optional<RoomModel> optionalRoomModel = roomRepository.findRoomById(id);
        return optionalRoomModel.map(this::castToRoomDto);
    }

    @Override
    public Optional<RoomDto> findRoomByUsers(Long firstId, Long secondId) {
        Optional<Long> optionalId = roomRepository.findRoomId(firstId, secondId);
        if (!optionalId.isPresent()) {
            return Optional.empty();
        }
        Optional<RoomDto> optionalRoomDto = findRoomById(optionalId.get());
        if (!optionalRoomDto.isPresent()) {
            throw new IllegalArgumentException("founded room is not exist (error in searching for a room)");
        }

        return optionalRoomDto;


    }

    @Override
    public RoomDto createNewRoom(Long firstId, Long secondId) {
        RoomModel roomModel = new RoomModel();
        Set<LinkModel> linkSet = new HashSet<>();
        linkSet.add(LinkModel.builder()
                .room(roomModel)
                .userId(firstId)
                .build());
        linkSet.add(LinkModel.builder()
                .room(roomModel)
                .userId(secondId)
                .build());
        roomModel.setLinkModels(linkSet);

        return castToRoomDto(roomRepository.addRoom(roomModel));

    }

    @Override
    public List<RoomDto> findRoomsByUserId(Long userId) {
        List<RoomModel> roomModelList = roomRepository.findRoomsByUserId(userId);
        List<RoomDto> roomDtoList = new LinkedList<>();

        for (RoomModel model : roomModelList){
            roomDtoList.add(castToRoomDto(model));
        }
        return roomDtoList;
    }

    private RoomDto castToRoomDto(RoomModel roomModel) {
        return RoomDto.builder()
                .id(roomModel.getId())
                .linkDtoSet(getLinksDto(roomModel))
                .messages(castToMessageDtoSet(roomModel.getMessages()))
                .build();
    }

    private Set<LinkDto> getLinksDto(RoomModel roomModel){
        List<LinkModel> linkDtoList = roomRepository.findLinks(roomModel);
        return castToLinkDtoSet(linkDtoList);
    }

    private Set<MessageDto> castToMessageDtoSet(Set<MessageModel> modelSet) {
        if(modelSet == null){
            return new HashSet<>();
        }

        Set<MessageDto> dtoSet = new HashSet<>();
        for (MessageModel model : modelSet) {
            dtoSet.add(castToMessageDto(model));
        }
        return dtoSet;
    }

    private MessageDto castToMessageDto(MessageModel messageModel) {
        return MessageDto.builder()
                .id(messageModel.getId())
                .messageStates(messageModel.getMessageStates())
                .text(messageModel.getText())
                .userDataDto(findUserDto(messageModel.getId()))
                .build();
    }


    private Set<LinkDto> castToLinkDtoSet(Set<LinkModel> modelSet) {
        System.out.println(modelSet == null);
        if(modelSet == null){
            return new HashSet<>();
        }

        Set<LinkDto> dtoSet = new HashSet<>();
        for (LinkModel model : modelSet) {
            dtoSet.add(castToLinkDto(model));
        }
        return dtoSet;
    }


    private Set<LinkDto> castToLinkDtoSet(List<LinkModel> linkList) {
        System.out.println(linkList == null);
        if(linkList == null){
            return new HashSet<>();
        }

        Set<LinkDto> dtoSet = new HashSet<>();
        for (LinkModel model : linkList) {
            dtoSet.add(castToLinkDto(model));
        }
        return dtoSet;
    }

    private LinkDto castToLinkDto(LinkModel linkModel) {
        return LinkDto.builder()
                .id(linkModel.getId())
                .userDto(findUserDto(linkModel.getUserId()))
                .build();
    }

    private UserDataDto findUserDto(Long id) {
        Optional<UserDataModel> optionalModel = userRepository.findById(id);
        if (!optionalModel.isPresent()) {
            throw new IllegalArgumentException("user not found");
        }

        return UserDataDto.getUserDataDto(optionalModel.get());
    }
}
