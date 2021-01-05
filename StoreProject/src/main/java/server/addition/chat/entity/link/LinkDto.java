package server.addition.chat.entity.link;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.addition.chat.entity.room.RoomDto;
import server.entities.user.dto.UserDataDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LinkDto {

    private Long id;
    private RoomDto room;
    private UserDataDto userDto;
}
