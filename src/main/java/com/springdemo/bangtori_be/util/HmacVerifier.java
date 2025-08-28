//package com.springdemo.bangtori_be.util;
//
//import org.springframework.stereotype.Component;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.MessageDigest;
//import java.util.HexFormat;
//import org.springframework.beans.factory.annotation.Value;
//
//import static java.nio.charset.StandardCharsets.UTF_8;
//
//// util/HmacVerifier.java
//@Component
//public class HmacVerifier {
//    @Value("${ai.callback.secret}") private String secret;
//
//    public boolean verify(String body, String signatureHex) {
//        try {
//            Mac mac = Mac.getInstance("HmacSHA256");
//            mac.init(new SecretKeySpec(secret.getBytes(UTF_8), "HmacSHA256"));
//            byte[] calc = mac.doFinal(body.getBytes(UTF_8));
//            String calcHex = HexFormat.of().formatHex(calc);
//            return MessageDigest.isEqual(calcHex.getBytes(UTF_8), signatureHex.getBytes(UTF_8));
//        } catch (Exception e) { return false; }
//    }
//}
