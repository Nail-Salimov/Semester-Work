package server.jwt;

public interface Tokenizer {
    String getToken(String username, String email);
    boolean checkClient(String token, String username, String email);
}
