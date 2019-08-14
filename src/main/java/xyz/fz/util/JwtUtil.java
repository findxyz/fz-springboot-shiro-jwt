package xyz.fz.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.util.Base64;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import xyz.fz.configuration.shiro.realm.JwtData;

import java.security.SecureRandom;
import java.util.Date;

public class JwtUtil {

    private static final String CLAIM_VERSION = "version";

    private static final String CLAIM_EXT = "ext";

    private JWSSigner jwsSigner;

    private JWSVerifier jwsVerifier;

    public JwtUtil(String jwtKey) {
        byte[] jwtKeyBytes = new Base64(jwtKey).decode();
        try {
            jwsSigner = new MACSigner(jwtKeyBytes);
            jwsVerifier = new MACVerifier(jwtKeyBytes);
        } catch (Exception e) {
            throw new RuntimeException("JwtUtil创建失败");
        }
    }

    public String buildJwt(JwtData jwtData) {
        String jwt = null;
        try {
            JWTClaimsSet claimsSet = new JWTClaimsSet
                    .Builder()
                    .jwtID(jwtData.getId())
                    .claim(CLAIM_VERSION, jwtData.getVersion())
                    .expirationTime(jwtData.getExpTime())
                    .claim(CLAIM_EXT, jwtData.getExt())
                    .build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(jwsSigner);
            jwt = signedJWT.serialize();
        } catch (Exception ignore) {
        }
        return jwt;
    }

    protected JwtData restoreJwt(String jwt) {
        JwtData jwtData = null;
        try {
            SignedJWT parseSignedJWT = SignedJWT.parse(jwt);
            if (parseSignedJWT.verify(jwsVerifier)
                    && parseSignedJWT.getState().equals(JWSObject.State.VERIFIED)) {
                JWTClaimsSet claimsSet = parseSignedJWT.getJWTClaimsSet();
                jwtData = new JwtData(
                        claimsSet.getJWTID(),
                        Integer.parseInt(claimsSet.getClaim(CLAIM_VERSION).toString()),
                        claimsSet.getExpirationTime(),
                        claimsSet.getClaim(CLAIM_EXT).toString()
                );
            }
        } catch (Exception ignore) {
        }
        return jwtData;
    }

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] jwtKeyBytes = new byte[32];
        random.nextBytes(jwtKeyBytes);
        Base64 secretBase64 = Base64.encode(jwtKeyBytes);
        String jwtKey = secretBase64.toString();
        System.out.println(jwtKey);

        JwtUtil jwtUtil = new JwtUtil(jwtKey);
        String jwt = jwtUtil.buildJwt(new JwtData("1", 0, new Date(System.currentTimeMillis() + 30 * 1000)));
        System.out.println(jwt);
        JwtData jwtData = jwtUtil.restoreJwt(jwt);
        System.out.println(jwtData);

        String jwt2 = jwtUtil.buildJwt(new JwtData("2", 1, new Date(System.currentTimeMillis() + 30 * 1000)));
        System.out.println(jwt2);
        JwtData jwtData2 = jwtUtil.restoreJwt(jwt2);
        System.out.println(jwtData2);
    }
}
