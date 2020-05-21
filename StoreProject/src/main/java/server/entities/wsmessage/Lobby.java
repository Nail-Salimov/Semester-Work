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
public class Lobby {

    private String type;
    private UserDataDto user;
    private String lastText;
    private Integer count;
    private Long roomId;

}
