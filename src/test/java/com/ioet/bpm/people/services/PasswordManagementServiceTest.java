package com.ioet.bpm.people.services;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.domain.UpdatePassword;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PasswordManagementServiceTest {

    private static final String ENCRYPTED_TESTING_PWD = "su5WnNHKHjZLJwmcIniJOmrEV2QsDsZrS/VJAoB4JH51vKyqf57zsTqr3BcfWc6Hj7QfGv8b3jy9M9N/aRlphw==:Vy047jiAneXBIvWsDyFtSGQ4Kh6bEggTglQ7vBxenoK8YdEDEMYKof7MDU7rjpOD6/Cgr5cp60iT1XFYn9qZgQ==";
    private static final String SALT_ENCRYPTED_TESTING_PWD = ENCRYPTED_TESTING_PWD.split(":")[0];
    private static final String HASH_ENCRYPTED_TESTING_PWD = ENCRYPTED_TESTING_PWD.split(":")[1];
    private static final String TESTING_PWD = "ioet";

    @InjectMocks
    public PasswordManagementService passwordManagementService;

    @Test
    public void verifyPassword() {
        byte[] salt = Base64.getDecoder().decode(SALT_ENCRYPTED_TESTING_PWD);
        byte[] hash = Base64.getDecoder().decode(HASH_ENCRYPTED_TESTING_PWD);

        boolean correct = passwordManagementService.verifyPassword(TESTING_PWD, hash, salt);
        assertEquals(true, correct);
        assertEquals(salt.length, new byte[64].length);

    }

    @Test
    public void generateSaltTest() {
        byte[] salt = passwordManagementService.generateSalt();
        assertEquals(salt.length, new byte[64].length);
    }

    @Test
    public void comparePasswordsTest() {
        byte[] hash = Base64.getDecoder().decode(HASH_ENCRYPTED_TESTING_PWD);
        byte[] salt = Base64.getDecoder().decode(SALT_ENCRYPTED_TESTING_PWD);
        byte[] comparisonHash = passwordManagementService.calculateHash(TESTING_PWD, salt);
        boolean verify = passwordManagementService.comparePasswords(hash, comparisonHash);

        assertEquals(true, verify);

    }

    @Test
    public void providedPasswordIsCorrectTest() {
        Person personToUpdate = mock(Person.class);
        UpdatePassword updatePassword = new UpdatePassword();
        when(personToUpdate.getPassword()).thenReturn(ENCRYPTED_TESTING_PWD);
        updatePassword.setOldPassword(TESTING_PWD);
        String newPassword = "ioetabc";
        updatePassword.setNewPassword(newPassword);
        updatePassword.setNewPasswordConfirmation(newPassword);

        boolean correct = passwordManagementService.isProvidedPasswordCorrect(personToUpdate, updatePassword);

        assertTrue(correct);
    }
}
