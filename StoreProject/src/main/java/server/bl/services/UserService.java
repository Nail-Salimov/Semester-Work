package server.bl.services;

import server.entities.user.dto.UserDataDto;

import java.util.Optional;

public interface UserService {
    Optional<UserDataDto> findUserByMail(String mail);
    Optional<UserDataDto> findUserById(Long id);
    boolean saveUser(UserDataDto userDataDto);
    boolean confirmUser(String name, String mail, String token);
    void updateUserName(String newName, UserDataDto userDataDto);
    void updateUserAddress(String newAddress, UserDataDto userDataDto);
}
