package server.addition.chat.bussineslogic.repositories;

import server.addition.chat.entity.message.MessageModel;
import server.addition.chat.entity.room.RoomModel;
import server.addition.chat.entity.state.MessageState;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    MessageModel addMessage(MessageModel message);
    List<MessageModel> findAllMessages(RoomModel roomModel);
    List<MessageModel> findAllNewMessages(RoomModel roomModel, Long userId);
    List<MessageModel> findAllOldMessages(RoomModel roomModel, Long userId);
    Optional<MessageModel> findMessageById(Long messageId);

    MessageState update(MessageState messageState);

    Optional<MessageState> findMessageState(Long userId, MessageModel message);
    void readMessage(Long messageId, Long userId);
}
