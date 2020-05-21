package server.entities.wsmessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.entities.user.dto.UserDataDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private Long messageId;
    private Long from;
    private String message;
    private Long roomId;
    private UserDataDto user;

}