package com.ioet.bpm.people.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PasswordManagementServiceTest {
    
    @InjectMocks
    public PasswordManagementService passwordManagementServiceMock;

    @Test
    public void validateTest() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String encrypt = "eH5M9JvVWRa512VUtba+s5AV9KhWLPJukTFW62+ezaqQhCGO88ckKf8dh94UO0dVr4dC9ixjzztUtG9GCcuN8g==:pRcsaPQ19rDOIvfaiYtl1DIiaMoxbRP6E7GHgCVQjlX/JEU79fzay6SnVXTvw+mofCh2gogb/8j8phk4l8/wvg==";
        String saltPart = encrypt.split(":")[0];
        String hashPart = encrypt.split(":")[1];
        byte[] salt = Base64.getDecoder().decode(saltPart);
        byte[] hash = Base64.getDecoder().decode(hashPart);
        String password = "ioet";

        Boolean correct = passwordManagementServiceMock.verifyPassword(hash, password, salt);
        assertEquals(correct, true);

    }

}