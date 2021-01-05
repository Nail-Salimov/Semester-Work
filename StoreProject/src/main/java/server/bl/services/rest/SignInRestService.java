package server.bl.services.rest;

import server.entities.signIn.dto.SignInDto;
import server.entities.token.dto.TokenDto;

public interface SignInRestService {
    TokenDto signIn(SignInDto signInDto);
    TokenDto signInWithHashPassword(SignInDto signInDto);
}
