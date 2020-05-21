package server.addition.chat.entity.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.addition.chat.entity.room.RoomDto;
import server.addition.chat.entity.state.MessageState;
import server.entities.user.dto.UserDataDto;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {

    private Long id;
    private RoomDto roomModel;

    private UserDataDto userDataDto;

    private String text;

    private Set<MessageState> messageStates;

}
