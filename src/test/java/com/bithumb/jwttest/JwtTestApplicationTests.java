package com.bithumb.jwttest;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECGenParameterSpec;

@Slf4j
@SpringBootTest
class JwtTestApplicationTests {

    @Test
    void test_generate_key_pair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // Given
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(new ECGenParameterSpec("secp256r1")); // == P256

        // When
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();

        // Then
        // Nothing Happen
        log.info("ecKey.publicKey: {}", Base64.encodeBase64String(keyPair.getPublic().getEncoded()));
        log.info("ecKey.privateKey: {}", Base64.encodeBase64String(keyPair.getPrivate().getEncoded()));
    }

}
