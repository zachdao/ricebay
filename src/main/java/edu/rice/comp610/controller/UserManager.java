package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.store.DatabaseManager;
import edu.rice.comp610.util.UnauthorizedException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * Controller that handles incoming requests for creating, viewing and updating accounts in the RiceBay system.
 */
public class UserManager {

    DatabaseManager databaseManager;

    public UserManager(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * When a user is registering for the website, they will be shown a page where they can enter basic information
     * about themselves such as their username, first and last name, their email, password, a profile photo, and their
     * Zelle ID. An error will be returned if the email address does not end in ‘@rice.edu’ or if the email is already
     * associated with an account. If neither of these scenarios occur, this information will be sent to the database
     * and stored.
     *
     * @param account the user's account information.
     * @return JsonStatusResponse indicating success or failure. In the case of failure, an error message will be
     * included in the response.
     * @see Account
     */
    public AppResponse<Account> saveAccount(Account account) {
        return new AppResponse<>(true, null, "OK");
    }

    /**
     * Retrieves information about a user account
     *
     * @param alias the user's login name
     * @return a response object containing the corresponding user account, or null if the account does not exist.
     */
    public AppResponse<Account> retrieveAccount(String alias) {
        return new AppResponse<>(true, null, "OK");
    }


    /**
     * Given the alias (username), compare the password saved in the database and return a success or failure based on success of login
     *
     * @param email the user's email
     * @param password the user's password
     * @return a response with the status of the login attempt; if an error occurred, the response will include an error
     * message.
     * @throws UnauthorizedException if unable to validate credentials
     */
    public AppResponse<Account> validateLogin(String email, String password) throws UnauthorizedException {
        // Throw out any requests that are automatically invalid
        if (!email.endsWith("@rice.edu") || password.length() < 4 || email.length() < "@rice.edu".length() + 1) {
            throw new UnauthorizedException();
        }

        try {
            // Find the account by email
            var accounts = databaseManager.loadObjects(Account.class, "SELECT firstName, lastName, alias, email WHERE email='$1'", email);

            // Email didn't match a single account
            if (accounts.size() != 1) {
                throw new UnauthorizedException();
            }

            // Credentials were not correct
            if (!validatePassword(password, accounts.get(0).getPassword())) {
                throw new UnauthorizedException();
            }

            return new AppResponse<>(true, accounts.get(0), "OK");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.err.println("Caught an exception while trying to authenticate a user. We'll throw an unauthorized error for safety.");
            e.printStackTrace();
            throw new UnauthorizedException();
        }

    }


    /**
     * Given the alias (username), destroy the user session
     *
     * @param alias the user's account information.
     * @return a response with the status of the session destruction; if an error occurred, the response will include an error
     * message.
     */
    public AppResponse<Account> logout(String alias) {return new AppResponse<>(true, null, "OK");}

    /**
     * Given a user's password, return a string we can safely store in the DB
     * @param plaintext the users password
     * @return a hex string of the hashed user's password and salt
     * @throws NoSuchAlgorithmException the JDK used to run this is older than Java 8
     * @throws InvalidKeySpecException the JDK used to run this is older than Java 8
     */
    private String hashPassword(String plaintext) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = generateSalt();
        byte[] hash = hashPassword(plaintext, salt);

        return String.format("%s$%s", toHex(hash), toHex(salt));
    }

    private String toHex(byte[] value) {
        BigInteger decimal = new BigInteger(1, value);
        return String.format("%0" + (value.length << 1) + "x", decimal);
    }

    /**
     * Given a plaintext password and salt, return the hashed password
     * @param plaintext the plaintext password
     * @param salt the salt used in the hashing algorithm
     * @return the password hashed with PBKDF2WithHmacSHA256
     * @throws NoSuchAlgorithmException the JDK used to run this is older than Java 8
     * @throws InvalidKeySpecException the JDK used to run this is older than Java 8
     * @see <a href="https://en.wikipedia.org/wiki/PBKDF2">PBKDF2</a>
     */
    private byte[] hashPassword(String plaintext, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(plaintext.toCharArray(), salt, 65536, 512);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        return factory.generateSecret(spec).getEncoded();
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    /**
     * Given a user's plaintext password and the hashed password from the DB, check that they match
     * @param plaintext the user's password (probably from the login form)
     * @param ciphertext the user's password from the DB
     * @return true if they match, false if they don't
     * @throws NoSuchAlgorithmException the JDK used to run this is older than Java 8
     * @throws InvalidKeySpecException the JDK used to run this is older than Java 8
     */
    private boolean validatePassword(String plaintext, String ciphertext) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = ciphertext.split("\\$");
        byte[] hash = fromHex(parts[0]);
        byte[] salt = fromHex(parts[1]);

        byte[] possibleCiphertext = hashPassword(plaintext, salt);

        for (int i = 0; i < possibleCiphertext.length; i++) {
            if (possibleCiphertext[i] != hash[i]) {
                return false;
            }
        }

        return true;
    }

    private byte[] fromHex(String hexString) {
        byte[] value = new byte[hexString.length() / 2];

        for (int i = 0; i < value.length; i++) {
            int index = i * 2;
            value[i] = (byte) Integer.parseInt(hexString.substring(index, index + 2), 16);
        }

        return value;
    }

}
