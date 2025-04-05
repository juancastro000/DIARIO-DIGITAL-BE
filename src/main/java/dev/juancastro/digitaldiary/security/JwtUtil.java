package dev.juancastro.digitaldiary.security;

import java.util.Date;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class JwtUtil {

    private final String secret;
    private final long expirationMs;

    public JwtUtil(String secret, long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    public String generateToken(String username) throws JOSEException {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("digital-diary")
                .issueTime(new Date())
                .expirationTime(new Date(System.currentTimeMillis() + expirationMs))
                .build();

        SignedJWT signedJWT = new SignedJWT(header, claimsSet);

        JWSSigner signer = new MACSigner(secret.getBytes());
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }
}