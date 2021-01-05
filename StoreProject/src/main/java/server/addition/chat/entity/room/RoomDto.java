package server.addition.chat.entity.room;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.addition.chat.entity.link.LinkDto;
import server.addition.chat.entity.message.MessageDto;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDto {
    private Long id;
    private Set<LinkDto> linkDtoSet;
    private Set<MessageDto> messages;
}