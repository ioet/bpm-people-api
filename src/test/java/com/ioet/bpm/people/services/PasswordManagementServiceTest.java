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

    @InjectMocks
    public PasswordManagementService passwordManagementService;

    @Test
    public void verifyPassword() {
        String encrypt = "su5WnNHKHjZLJwmcIniJOmrEV2QsDsZrS/VJAoB4JH51vKyqf57zsTqr3BcfWc6Hj7QfGv8b3jy9M9N/aRlphw==:Vy047jiAneXBIvWsDyFtSGQ4Kh6bEggTglQ7vBxenoK8YdEDEMYKof7MDU7rjpOD6/Cgr5cp60iT1XFYn9qZgQ==";
        String saltPart = encrypt.split(":")[0];
        String hashPart = encrypt.split(":")[1];
        byte[] salt = Base64.getDecoder().decode(saltPart);
        byte[] hash = Base64.getDecoder().decode(hashPart);
        String password = "ioet";

        boolean correct = passwordManagementService.verifyPassword(password, hash, salt);
        assertEquals(correct, true);
        assertEquals(salt.length, new byte[64].length);

    }

    @Test
    public void generateSaltTest() {
        byte[] salt = passwordManagementService.generateSalt();
        assertEquals(salt.length, new byte[64].length);
    }

    @Test
    public void comparePasswordsTest() {
        String encrypt = "su5WnNHKHjZLJwmcIniJOmrEV2QsDsZrS/VJAoB4JH51vKyqf57zsTqr3BcfWc6Hj7QfGv8b3jy9M9N/aRlphw==:Vy047jiAneXBIvWsDyFtSGQ4Kh6bEggTglQ7vBxenoK8YdEDEMYKof7MDU7rjpOD6/Cgr5cp60iT1XFYn9qZgQ==";
        String saltPart = encrypt.split(":")[0];
        String hashPart = encrypt.split(":")[1];
        String password = "ioet";

        byte[] hash = Base64.getDecoder().decode(hashPart);
        byte[] salt = Base64.getDecoder().decode(saltPart);
        byte[] comparisonHash = passwordManagementService.calculateHash(password, salt);
        boolean verify = passwordManagementService.comparePasswords(hash, comparisonHash);

        assertEquals(verify, true);

    }

    @Test
    public void providedPasswordIsCorrectTest() {
        Person personToUpdate = mock(Person.class);
        UpdatePassword updatePassword = mock(UpdatePassword.class);
        String encrypt = "su5WnNHKHjZLJwmcIniJOmrEV2QsDsZrS/VJAoB4JH51vKyqf57zsTqr3BcfWc6Hj7QfGv8b3jy9M9N/aRlphw==:Vy047jiAneXBIvWsDyFtSGQ4Kh6bEggTglQ7vBxenoK8YdEDEMYKof7MDU7rjpOD6/Cgr5cp60iT1XFYn9qZgQ==";
        when(personToUpdate.getPassword()).thenReturn(encrypt);
        String password = "ioet";
        when(updatePassword.getOldPassword()).thenReturn(password);
        String newPassword = "ioetabc";
        when(updatePassword.getNewPassword()).thenReturn(newPassword);
        when(updatePassword.getNewPasswordConfirmation()).thenReturn(newPassword);

        boolean correct = passwordManagementService.isProvidedPasswordCorrect(personToUpdate, updatePassword);

        assertTrue(correct);
    }
}
