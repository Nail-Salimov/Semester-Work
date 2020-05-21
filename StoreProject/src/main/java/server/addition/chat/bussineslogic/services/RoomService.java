package server.addition.chat.bussineslogic.services;

import server.addition.chat.entity.room.RoomDto;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    Optional<RoomDto> findRoomById(Long id);
    Optional<RoomDto> findRoomByUsers(Long firstId, Long secondId);

    RoomDto createNewRoom(Long firstId, Long secondId);
    List<RoomDto> findRoomsByUserId(Long userId);
}
