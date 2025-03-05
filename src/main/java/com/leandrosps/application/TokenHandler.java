package com.leandrosps.application;

import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.leandrosps.dtos.GenerateTokenOutput;
import io.jsonwebtoken.Jwts;
import spark.resource.ClassPathResource;

public class TokenHandler {

   public TokenHandler() {
   }

   public GenerateTokenOutput generateToken(String user_id, String user_email, List<String> roles) {

      Instant expiredAtInst = Instant.now().plusSeconds(300L);
      Date expiryDate = Date.from(expiredAtInst);
      PrivateKey privateKey = loadPrivateKey();

      String jws = Jwts.builder()
            .issuer("Trade-Fast")
            .subject(user_id)
            .claim("user_email", user_email)
            .claim("roles", roles)
            .claim("user_id", user_id)
            .expiration(expiryDate)
            .signWith(privateKey, Jwts.SIG.RS256)
            .compact();

      this.tokenValidation(jws); /* Ought to be valid */

      return new GenerateTokenOutput(jws, expiryDate);
   }

   public record TokenValidationResult(boolean isValid, String message, String user_id, List<String> roles) {
   }

   public TokenValidationResult tokenValidation(String token) {
      try {

         var payload = Jwts.parser()
               .verifyWith(this.loadPublicKey())
               .build().parseSignedClaims(token).getPayload();

         if (payload.isEmpty()) {
            return new TokenValidationResult(false, "Invalid token", null, null);
         }

         if (payload.getExpiration().toInstant().isBefore(Instant.now())) {
            return new TokenValidationResult(false, "Token has Expired", null, null);
         }

         Object rolesObj = payload.get("roles");

         List<String> roles = Collections.emptyList();;
         
         if (rolesObj instanceof List<?>) {
            roles = ((List<?>) rolesObj)
                  .stream()
                  .filter(String.class::isInstance)
                  .map(String.class::cast).toList();
         } 

         return new TokenValidationResult(true, "Token is valid", payload.get("user_id").toString(), roles);
      } catch (Exception e) {
         return new TokenValidationResult(false, "Token validation failed: " + e.getMessage(), null, null);
      }
   }

   private PrivateKey loadPrivateKey() {
      try {
         var resource = new ClassPathResource("priv.key");

         String keyContent = new String(Files.readAllBytes(resource.getFile().toPath()))
               .replace("-----BEGIN PRIVATE KEY-----", "")
               .replace("-----END PRIVATE KEY-----", "")
               .replace("\n", "")
               .replace("\r", "")
               .trim();

         byte[] keyBytes = Base64.getDecoder().decode(keyContent);

         PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

         var keyFactory = KeyFactory.getInstance("RSA");

         return keyFactory.generatePrivate(keySpec);
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Error on load Private Keys!: " + e.getMessage());
      }
   }

   private PublicKey loadPublicKey() {

      try {
         var resource = new ClassPathResource("pub.key");
         String keyContent = new String(Files.readAllBytes(resource.getFile().toPath()))
               .replace("-----BEGIN PUBLIC KEY-----", "")
               .replace("-----END PUBLIC KEY-----", "")
               .replace("\n", "")
               .replace("\r", "")
               .trim();

         byte[] keyBytes = Base64.getDecoder().decode(keyContent);
         X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

         var keyFactory = KeyFactory.getInstance("RSA");
         return keyFactory.generatePublic(keySpec);
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException("Error on load Public Keys!: " + e.getMessage());
      }
   }
}
