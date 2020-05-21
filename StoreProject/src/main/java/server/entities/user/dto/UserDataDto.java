package server.entities.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.entities.user.model.UserDataModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDataDto {
    private Long id;
    private String mail;
    private String password;
    private String name;
    private String role;
    private String state;
    private String address;

    public static UserDataDto getUserDataDto(UserDataModel model){
        return UserDataDto.builder()
                .id(model.getId())
                .mail(model.getMail())
                .name(model.getName())
                .role(model.getRole().toString())
                .password(model.getHashPassword())
                .state(model.getState().toString())
                .address(model.getAddress())
                .build();
    }
}
