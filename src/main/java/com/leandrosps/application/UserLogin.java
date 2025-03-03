package com.leandrosps.application;

import com.leandrosps.dtos.UserLoginOuput;
import com.leandrosps.exceptions.UserUnauthorizedException;
import com.leandrosps.infra.UserDAO;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.AeadAlgorithm;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

public class UserLogin {

    private UserDAO userDAO;

    public UserLogin(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserLoginOuput excute(String email, String password) {
        var userOp = this.userDAO.getUserByEmail(email);

        if (userOp.isEmpty()) {
            throw new UserUnauthorizedException("Passoword or email is invalid!!");
        }

        var user = userOp.get();

        if (!user.isPasswordValid(password)) {
            throw new UserUnauthorizedException("Passoword or email is invalid!!");
        }

        /* Use RSA or basic secrety? */
        SecretKey key = Jwts.SIG.HS256.key().build();

        Instant expiredAtInst = Instant.now().plusSeconds(300L);
        Date expiryDate = Date.from(expiredAtInst);

        KeyPair pair = Jwts.SIG.RS512.keyPair().build();
        AeadAlgorithm enc = Jwts.ENC.A256GCM;

        String jws = Jwts.builder()
                .issuer("skp.Tridimensional@gmail.com")
                .subject(user.getId().toString())
                .claim("user_email", user.getEmail())
                .expiration(expiryDate)
                //.signWith(key)
                .encryptWith(pair.getPublic(), Jwts.KEY.RSA_OAEP_256, enc)
                .compact();

        return new UserLoginOuput(jws, expiryDate);
    }

}
