package server.addition.chat.bussineslogic.repositories;

import server.addition.chat.entity.link.LinkModel;
import server.addition.chat.entity.room.RoomModel;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    RoomModel addRoom(RoomModel roomModel);
    Optional<RoomModel> findRoomById(Long id);

    LinkModel addLink(LinkModel linkModel);
    Optional<LinkModel> findLinks(Long userId, RoomModel roomModel);
    Optional<Long> findRoomId(Long firstId, Long secondId);

    List<LinkModel> findLinks(RoomModel roomModel);
    List<RoomModel> findRoomsByUserId(Long userId);
}
