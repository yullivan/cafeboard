package cafeboard;

import cafeboard.member.JwtProvider;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {

    private final JwtProvider jwtProvider;

    public TokenValidator(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public String validateAndGetUsername(String token) {
        if (!jwtProvider.isValidToken(token)) {
            throw new IllegalArgumentException(TokenUtils.INVALID_TOKEN_MESSAGE);
        }
        return jwtProvider.getSubject(token);
    }
}
