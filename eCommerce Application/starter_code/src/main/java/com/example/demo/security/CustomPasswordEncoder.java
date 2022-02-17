package com.example.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@Slf4j
public class CustomPasswordEncoder {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    public String saltAndHash(String password){

        String securePassword = get_SecurePassword(password, this.createSalt(), "SHA-512");

        String HashedPassword =bCryptPasswordEncoder.encode(password); // Use password-hashing function "bcrypt" to Hash the password

        return HashedPassword;
    }





    // Method to generate the hash.
    //It takes a password and the Salt as input arguments
    private String get_SecurePassword(String passwordToHash, byte[] salt, String algorithm ){
        String generatedPassword = null;
        try {
            //https://docs.oracle.com/javase/6/docs/technotes/guides/security/StandardNames.html#MessageDigest
            //Supported by Java : MD2,MD5,SHA-1,SHA-256,SHA-384,SHA-512
            MessageDigest md = MessageDigest.getInstance(algorithm);

            if(salt!=null) {
                md.update(salt);
            }

            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }







    // Method to generate a Salt
    private byte[] createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        log.debug(" salt created "+salt );
        return salt;
    }






}
