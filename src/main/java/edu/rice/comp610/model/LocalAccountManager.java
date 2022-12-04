package edu.rice.comp610.model;

import edu.rice.comp610.controller.AccountManager;
import edu.rice.comp610.controller.Credentials;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.ObjectNotFoundException;
import edu.rice.comp610.util.UnauthorizedException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

/**
 * Controller that handles incoming requests for creating, viewing and updating accounts in the RiceBay system.
 */
public class LocalAccountManager implements AccountManager {

    private static final String RICE_EMAIL_SUFFIX = "@rice.edu";

    private final QueryManager queryManager;
    DatabaseManager databaseManager;

    public LocalAccountManager(QueryManager queryManager, DatabaseManager databaseManager) {
        this.queryManager = queryManager;
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
    public Account save(Account account) throws BadRequestException, DatabaseException {
        // Validate the account is correct
        try {
            Map<String, String> validationErrors = validateAccount(account);
            if (!validationErrors.isEmpty()) {
                throw new BadRequestException("invalid_account_parameters", validationErrors);
            }

            // When saving a new account, we will get the password, and we will get a null ID
            if (account.getId() == null) {
                // Add the UUID
                account.setId(UUID.randomUUID());

                // Replace the plaintext password with the hashed + salted password
                account.setPassword(hashPassword(account.getPassword()));
            }

            var accountQuery = queryManager.makeUpdateQuery(Account.class);
            databaseManager.saveObjects(accountQuery, account);

            return account;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to proceed");
        }
    }

    public void savePassword(Credentials credentials, String newPassword) throws BadRequestException, UnauthorizedException, DatabaseException {
        try {
            boolean validCredentials = validateLogin(credentials);
            if (!validCredentials) {
                throw new UnauthorizedException();
            }

            var emailQuery = queryManager.makeLoadQuery(Account.class, queryManager.filters().makeEqualityFilter("email"));
            var accounts = databaseManager.loadObjects(emailQuery, credentials.getEmail());
            if (accounts.size() != 1) {
                throw new UnauthorizedException();
            }
            var account = accounts.get(0);
            account.setLastPassword(account.getPassword());
            account.setPassword(hashPassword(newPassword));

            Map<String, String> validationErrors = validateAccount(account);
            if (Objects.equals(credentials.getPassword(), newPassword)) {
                validationErrors.put("password", "Invalid Password: new password must be different from last password");
            }
            if (!validationErrors.isEmpty()) {
                throw new BadRequestException("invalid_account_parameters", validationErrors);
            }

            var accountQuery = queryManager.makeUpdateQuery(Account.class);
            databaseManager.saveObjects(accountQuery, account);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to proceed");
        }
    }

    /**
     * Given an account, build a map of errors for each field in the account. The keys should match the frontend form.
     * @param account the account to check validity
     * @return a map of frontend key to error message, an empty map implies no errors
     */
    private Map<String, String> validateAccount(Account account) throws DatabaseException, NoSuchAlgorithmException, InvalidKeySpecException {
        HashMap<String, String> errors = new HashMap<>();

        // Validate First Name
        if (account.getGivenName().isEmpty()) {
            errors.put("firstName", "Invalid Name: must provide a first name");
        }

        // Validate Last name
        if (account.getSurname().isEmpty()) {
            errors.put("lastName", "Invalid Name: must provide a last name");
        }

        // Validate email
        errors.putAll(checkEmail(account.getEmail()));

        // Ensure unique email
        var emailQuery = queryManager.makeLoadQuery(Account.class, queryManager.filters().makeEqualityFilter("email"));
        List<Account> accountsByEmail = databaseManager.loadObjects(emailQuery, account.getEmail());
        if (accountsByEmail.size() > 0 && !accountsByEmail.get(0).getId().equals(account.getId())) {
            errors.put("email", "Invalid Email: email already in use");
        }

        // Ensure non-empty alias
        if (account.getAlias().length() < 2) {
            errors.put("alias", "Invalid Alias: alias must be at least two characters");
        }

        // Ensure unique alias
        var aliasQuery = queryManager.makeLoadQuery(Account.class, queryManager.filters().makeEqualityFilter("alias"));
        List<Account> accountsByAlias = databaseManager.loadObjects(aliasQuery, account.getAlias());
        if (accountsByAlias.size() > 0 && !accountsByAlias.get(0).getId().equals(account.getId())) {
            errors.put("alias", "Invalid Alias: alias already in use");
        }

        // Ensure password length
        if (!account.getPassword().isEmpty()) {
            errors.putAll(checkPassword(account.getPassword()));
        }

        return errors;
    }

    private Map<String, String> checkEmail(String email) {
        HashMap<String, String> errors = new HashMap<>();
        if (!email.endsWith(RICE_EMAIL_SUFFIX) || email.length() < RICE_EMAIL_SUFFIX.length() + 1) {
            errors.put("email", "Invalid Email: must be a @rice.edu email");
        }

        return errors;
    }

    private Map<String, String> checkPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        HashMap<String, String> errors = new HashMap<>();

        // TODO: Potential expansion to include checking compromised passwords and other requirements
        if (password.length() < 4) {
            errors.put("password", "Invalid Password: password must be at least four characters");
        }

        return errors;
    }

    /**
     * Retrieves information about a user account
     *
     * @param id the user's id
     * @return a response object containing the corresponding user account, or null if the account does not exist.
     */
    public Account get(UUID id) throws DatabaseException, ObjectNotFoundException {
        // find account by alias
        var aliasQuery = queryManager.makeLoadQuery(Account.class, queryManager.filters().makeEqualityFilter("id"));
        var accounts = databaseManager.loadObjects(aliasQuery, id);
        // alias didn't match single account
        if (accounts.size() != 1) {
            throw new ObjectNotFoundException("Unable to find user");
        }
        return accounts.get(0);
    }

    public Account get(String email) throws DatabaseException, ObjectNotFoundException {
        // find account by alias
        var aliasQuery = queryManager.makeLoadQuery(Account.class, queryManager.filters().makeEqualityFilter("email"));
        var accounts = databaseManager.loadObjects(aliasQuery, email);
        // alias didn't match single account
        if (accounts.size() != 1) {
            throw new ObjectNotFoundException("Unable to find user");
        }
        return accounts.get(0);
    }


    /**
     * Given the alias (username), compare the password saved in the database and return a success or failure based on success of login
     *
     * @param credentials the user's email and password
     * @return a response with the status of the login attempt; if an error occurred, the response will include an error
     * message.
     */
    public boolean validateLogin(Credentials credentials) {
        try {
            // Throw out any requests that are automatically invalid
            if (!checkEmail(credentials.getEmail()).isEmpty() || !checkPassword(credentials.getPassword()).isEmpty()) {
                return false;
            }

            // Credentials were not correct
            return validatePassword(credentials.getPassword(), this.get(credentials.getEmail()).getPassword());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | DatabaseException | ObjectNotFoundException e) {
            System.err.println("Caught an exception while trying to authenticate a user. We'll return false for safety.");
            e.printStackTrace();
            return false;
        }

    }

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
        List<byte[]> parts = splitPasswordHash(ciphertext);
        byte[] hash = parts.get(0);
        byte[] salt = parts.get(1);

        byte[] possibleCiphertext = hashPassword(plaintext, salt);

        for (int i = 0; i < possibleCiphertext.length; i++) {
            if (possibleCiphertext[i] != hash[i]) {
                return false;
            }
        }

        return true;
    }

    private List<byte[]> splitPasswordHash(String ciphertext) {
        String[] parts = ciphertext.split("\\$");
        byte[] hash = fromHex(parts[0]);
        byte[] salt = fromHex(parts[1]);

        return List.of(hash, salt);
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
