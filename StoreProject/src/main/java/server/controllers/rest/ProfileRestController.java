package server.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.bl.services.UserService;
import server.bl.services.rest.SignInRestService;
import server.entities.ancillary.newChanges.ChangesDto;
import server.entities.signIn.dto.SignInDto;
import server.entities.token.dto.TokenDto;
import server.entities.user.dto.UserDataDto;
import server.security.jwt.details.UserDetailsJwtImpl;

import java.util.Optional;

@RestController
public class ProfileRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private SignInRestService signInRestService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/userData")
    public ResponseEntity<UserDataDto> getUserData() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsJwtImpl userDetailsJwt = (UserDetailsJwtImpl) authentication.getDetails();
        return ResponseEntity.ok(UserDataDto.builder()
                .id(userDetailsJwt.getUserId())
                .address(userDetailsJwt.getAddress())
                .mail(userDetailsJwt.getUsername())
                .name(userDetailsJwt.getName())
                .role(userDetailsJwt.getRole())
                .state(userDetailsJwt.getState())
                .build());

    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/change_userData")
    public ResponseEntity<TokenDto> changeData(@RequestBody ChangesDto changesDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsJwtImpl userDetailsJwt = (UserDetailsJwtImpl) authentication.getDetails();
        UserDataDto userDataDto = UserDataDto.builder()
                .id(userDetailsJwt.getUserId())
                .address(userDetailsJwt.getAddress())
                .mail(userDetailsJwt.getUsername())
                .name(userDetailsJwt.getName())
                .role(userDetailsJwt.getRole())
                .state(userDetailsJwt.getState())
                .build();

        String newName = changesDto.getNewName();
        String newAddress = changesDto.getNewAddress();

        Optional<UserDataDto> optional = userService.findUserByMail(userDataDto.getMail());

        if(!optional.isPresent()){
            throw new IllegalArgumentException("user not exist");
        }

        UserDataDto userDto = optional.get();

        if (newName != null && !newName.equals("")) {
            userService.updateUserName(newName, userDataDto);
        }

        if (newAddress != null && !newAddress.equals("")) {
            userService.updateUserAddress(newAddress, userDataDto);
        }

        return ResponseEntity.ok(signInRestService.signInWithHashPassword(new SignInDto(userDto.getMail(), userDto.getPassword())));
    }
}
