package com.ioet.bpm.people.utils;

/*
 * Copyright (C) 2017 Dominik Schadow, dominikschadow@gmail.com
 *
 * This file is part of the Java Security project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


public class PasswordStorage {
    private static final Logger log = LoggerFactory.getLogger(PasswordStorage.class);
    private static final String ALGORITHM = "SHA-512";
    private static final int ITERATIONS = 64000;
    private static final int SALT_SIZE = 64;

    /**
     * Private constructor.
     */
    private PasswordStorage() {
    }


    public static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        random.nextBytes(salt);

        return salt;
    }

    public static void main(String[] args) {
        String password = "ioet";

        try {
            String cypher = "02OPuS2zfK9bj3RP1mSo5dqV5G6IkO5/xD4HdXkzk8pS+AfMDgtfe5qDh6nGnYMrtS9Txc2uORAFKtirUopc3A==:HCsB0jToMY39w7knpHOb+WEMObmlvR48oFk4RS7VWJsE519UoVGCGfcGV1ebBR0vfVe4aZ+KE6Z5LbZU8ysesw==";
            String salt = cypher.split(":")[0];
            String hash = cypher.split(":")[1];
            byte[] salt_ = Base64.getDecoder().decode(salt);
            byte[] hash_ = Base64.getDecoder().decode(hash);
            boolean correct = verifyPassword(hash_, password, salt_);
            log.info("result={}", correct);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] calculateHash(String password, byte[] salt) throws NoSuchAlgorithmException,
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

    public static boolean verifyPassword(byte[] originalHash, String password, byte[] salt) throws
            NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] comparisonHash = calculateHash(password, salt);
        return comparePasswords(originalHash, comparisonHash);
    }

    /**
     * Compares the two byte arrays in length-constant time using XOR.
     *
     * @param originalHash   The original password hash
     * @param comparisonHash The comparison password hash
     * @return True if both match, false otherwise
     */
    public static boolean comparePasswords(byte[] originalHash, byte[] comparisonHash) {
        int diff = originalHash.length ^ comparisonHash.length;
        for (int i = 0; i < originalHash.length && i < comparisonHash.length; i++) {
            diff |= originalHash[i] ^ comparisonHash[i];
        }

        return diff == 0;
    }
}