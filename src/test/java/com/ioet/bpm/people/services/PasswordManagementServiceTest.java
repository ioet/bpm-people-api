package com.ioet.bpm.people.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Base64;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PasswordManagementServiceTest {
    @Mock
    public PasswordManagementService passwordManagementService;

    @InjectMocks
    public PasswordManagementService passwordManagementServiceMock;

    @Test
    public void verifyPassword() {
        String encrypt = "eH5M9JvVWRa512VUtba+s5AV9KhWLPJukTFW62+ezaqQhCGO88ckKf8dh94UO0dVr4dC9ixjzztUtG9GCcuN8g==:pRcsaPQ19rDOIvfaiYtl1DIiaMoxbRP6E7GHgCVQjlX/JEU79fzay6SnVXTvw+mofCh2gogb/8j8phk4l8/wvg==";
        String saltPart = encrypt.split(":")[0];
        String hashPart = encrypt.split(":")[1];
        byte[] salt = Base64.getDecoder().decode(saltPart);
        byte[] hash = Base64.getDecoder().decode(hashPart);
        String password = "ioet";

        Boolean correct = passwordManagementServiceMock.verifyPassword(hash, password, salt);
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
}