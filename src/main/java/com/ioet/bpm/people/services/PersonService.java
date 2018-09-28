package com.ioet.bpm.people.services;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PersonService {

    private static final String ALGORITHM = "SHA-512";
    private static final int ITERATIONS = 64000;
    private static final int SALT_SIZE = 64;


    public String generatePassword(String password) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] salt = generateSalt();
        byte[] hash = calculateHash(password, salt);
        return Base64.getEncoder().encodeToString(salt).concat(":").concat(Base64.getEncoder().encodeToString(hash));
    }

    public byte[] calculateHash(String password, byte[] salt) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.reset();
        md.update(salt);
        byte[] hash = md.digest(password.getBytes("UTF-8"));

        for (int i = 0; i < ITERATIONS; i++) {
            md.reset();
            hash = md.digest(hash);
        }

        return hash;
    }

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        return salt;
    }

    public boolean verifyPassword(byte[] originalHash, String password, byte[] salt) throws
            NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] comparisonHash = calculateHash(password, salt);
        return comparePasswords(originalHash, comparisonHash);
    }

    public boolean comparePasswords(byte[] originalHash, byte[] comparisonHash) {
        int diff = originalHash.length ^ comparisonHash.length;
        for (int i = 0; i < originalHash.length && i < comparisonHash.length; i++) {
            diff |= originalHash[i] ^ comparisonHash[i];
        }

        return diff == 0;
    }
}
