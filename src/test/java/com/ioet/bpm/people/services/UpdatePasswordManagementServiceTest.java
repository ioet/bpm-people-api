package com.ioet.bpm.people.services;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.domain.UpdatePassword;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UpdatePasswordManagementServiceTest {
    @Mock
    public PasswordManagementService passwordManagementService;

    @InjectMocks
    public PasswordManagementService passwordManagementServiceMock;

    @Test
    public void verifyPassword() {
        String encrypt = "su5WnNHKHjZLJwmcIniJOmrEV2QsDsZrS/VJAoB4JH51vKyqf57zsTqr3BcfWc6Hj7QfGv8b3jy9M9N/aRlphw==:Vy047jiAneXBIvWsDyFtSGQ4Kh6bEggTglQ7vBxenoK8YdEDEMYKof7MDU7rjpOD6/Cgr5cp60iT1XFYn9qZgQ==";
        String saltPart = encrypt.split(":")[0];
        String hashPart = encrypt.split(":")[1];
        byte[] salt = Base64.getDecoder().decode(saltPart);
        byte[] hash = Base64.getDecoder().decode(hashPart);
        String password = "ioet";

        Boolean correct = passwordManagementServiceMock.verifyPassword(password, hash, salt);
        assertEquals(correct, true);
        assertEquals(salt.length, new byte[64].length);

    }

    @Test
    public void generateSaltTest() {
        byte[] salt = passwordManagementServiceMock.generateSalt();
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
        byte[] comparisonHash = passwordManagementServiceMock.calculateHash(password, salt);
        boolean verify = passwordManagementServiceMock.comparePasswords(hash, comparisonHash);

        assertEquals(verify, true);

    }

    @Test
    public void providedPasswordIsCorrectTest(){
       UpdatePassword updatePassword = new UpdatePassword();

       String encrypt = "su5WnNHKHjZLJwmcIniJOmrEV2QsDsZrS/VJAoB4JH51vKyqf57zsTqr3BcfWc6Hj7QfGv8b3jy9M9N/aRlphw==:Vy047jiAneXBIvWsDyFtSGQ4Kh6bEggTglQ7vBxenoK8YdEDEMYKof7MDU7rjpOD6/Cgr5cp60iT1XFYn9qZgQ==";
       String saltPart = encrypt.split(":")[0];
       String hashPart = encrypt.split(":")[1];
       byte[] salt = Base64.getDecoder().decode(saltPart);
       byte[] hash = Base64.getDecoder().decode(hashPart);
       String password = "ioet";

       Boolean correct = passwordManagementServiceMock.verifyPassword(password, hash, salt);
       assertEquals(correct, true);
       assertEquals(updatePassword.getNewPassword(),updatePassword.getNewPasswordConfirmation());
    }
  }
