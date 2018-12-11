package com.ioet.bpm.people.services;

import com.ioet.bpm.people.domain.PasswordHistory;
import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.domain.UpdatePassword;
import com.ioet.bpm.people.repositories.PasswordHistoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@AllArgsConstructor
@Component
public class PasswordManagementService {

    private static final String ALGORITHM = "SHA-512";
    private static final int ITERATIONS = 64000;
    private static final int SALT_SIZE = 64;

    private final PasswordHistoryRepository passwordHistoryRepository;



    public String encryptPassword(String password) {
        byte[] salt = generateSalt();
        byte[] hash = calculateHash(password, salt);
        return Base64.getEncoder().encodeToString(salt).concat(":").concat(Base64.getEncoder().encodeToString(hash));
    }


    public byte[] calculateHash(String password, byte[] salt) {
        byte[] hash = new byte[64];
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.reset();
            md.update(salt);
            hash = md.digest(password.getBytes("UTF-8"));

            for (int i = 0; i < ITERATIONS; i++) {
                md.reset();
                hash = md.digest(hash);
            }

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            log.error(ex.getMessage(), ex);
        }
        return hash;
    }

    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);
        return salt;
    }

    public boolean verifyPassword(String password, byte[] originalHash, byte[] salt) {
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

    public PasswordHistory createPasswordHistory(Person person) {
        PasswordHistory passwordHistory = new PasswordHistory();
        passwordHistory.setPersonId(person.getId());
        passwordHistory.setPassword(person.getPassword());
        return passwordHistory;
    }

    public void recordPasswordHistory(Person person) {
        PasswordHistory passwordHistory = createPasswordHistory(person);
        passwordHistoryRepository.save(passwordHistory);
    }

    public boolean isProvidedPasswordCorrect(Person personUpdate, UpdatePassword updatePassword){
        String saltPart = personUpdate.getPassword().split(":")[0];
        String hashPart = personUpdate.getPassword().split(":")[1];
        byte[] salt = Base64.getDecoder().decode(saltPart);
        byte[] hash = Base64.getDecoder().decode(hashPart);

        String oldPasswordProvided = updatePassword.getOldPassword();
        boolean correct = verifyPassword(oldPasswordProvided, hash, salt);

        return correct&&updatePassword.getNewPassword().equals(updatePassword.getNewPasswordConfirmation());

    }


}
