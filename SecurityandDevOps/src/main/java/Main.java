//Necessary imports
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.MessageDigest;


public class Main {


    public static void main(String[] args) {

        // A static String for the example
        String passwordToHash = "myPassword";

        // Create a salt
        byte[] salt = createSalt();

        // Create a hash with "SHA-256"
        String securePassword = get_SecurePassword(passwordToHash, salt, "SHA-256"); // better than MD5
        System.out.println("hash with 'SHA-256' ="+securePassword);

        // Create a hash with "MD5" + Salting
        securePassword = get_SecurePassword(passwordToHash, salt, "MD5");
        System.out.println("hash with 'SHA-256' ="+securePassword);

        //Create a hash with "MD5" + No Salt
        securePassword = get_SecurePassword(passwordToHash, null , "MD5");
        System.out.println("hash with 'SHA-256' ="+securePassword);


        securePassword = get_SecurePassword(passwordToHash, null , "MD5");
        System.out.println("hash with 'SHA-256' ="+securePassword);


        /**************************************
         *     bCrypt implementation
         **************************************/

        //Implement Hashing (bCrypt)
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        // 1) Call a built-in method  -- encode() method - Encodes the raw password. Generally, a good encoding algorithm applies an SHA-1 or greater hash combined with an 8-byte or greater randomly generated salt.
        securePassword = bCryptPasswordEncoder.encode("mySaltedPassword");
        System.out.println("=> securePassword="+securePassword);

        // 2) matches() method - It matches and verifies the encoded password obtained from the storage, and the submitted raw password (after encoding). Returns true if the passwords match, false if they do not. The stored password itself is never decoded
        Boolean validation = bCryptPasswordEncoder.matches(securePassword, "mySaltedPassword");


        // 3) upgradeEncoding() method - It returns true if the encoded password should be encoded again for better security, else false. The default implementation always returns false.
        validation = bCryptPasswordEncoder.upgradeEncoding(securePassword);


    }







    // Method to generate a Salt
    private static byte[] createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }








    // Method to generate the hash.
    //It takes a password and the Salt as input arguments
    private static String get_SecurePassword(String passwordToHash, byte[] salt, String algorithm ){
        String generatedPassword = null;
        try {
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








}
