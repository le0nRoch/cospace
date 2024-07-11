package ch.zli.cospace.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("#{new Long(${jwt.expiration})}")
    private Long LIFETIME;

    @Value("#{new Long(${jwt.leeway})}")
    private long LEEWAY;
    private final Algorithm algorithm;

    public JwtUtils(@Value("${jwt.secret}") String secret ) {
        algorithm = Algorithm.HMAC256(secret);
    }

    public boolean isInvalid(String token) {
        var verifier = JWT.require(algorithm)
                .acceptLeeway(LEEWAY)
                .build();

        try {
            verifier.verify(token);
            return false;
        } catch (JWTVerificationException e) {
            return true;
        }
    }

    public String getEmail(String token) {
        return JWT.decode(token).getClaim("name").asString();
    }

    public String generateToken(Long userId, String email) {
        return JWT.create()
                .withClaim("sub", userId)
                .withClaim("name", email)
                .withClaim("exp", System.currentTimeMillis() + LIFETIME)
                .sign(algorithm);
    }
}