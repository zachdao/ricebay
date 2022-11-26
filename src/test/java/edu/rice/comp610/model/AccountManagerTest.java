package edu.rice.comp610.model;

import edu.rice.comp610.controller.Credentials;
import edu.rice.comp610.model.Account;
import edu.rice.comp610.model.LocalAccountManager;
import edu.rice.comp610.model.QueryManager;
import edu.rice.comp610.store.PostgresQueryManager;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.model.DatabaseManager;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountManagerTest {

    Account BOBS_ACCOUNT = new Account();
    Account NEW_ACCOUNT = new Account();

    QueryManager queryManager = mock(QueryManager.class);
    DatabaseManager databaseManager = mock(DatabaseManager.class);

    LocalAccountManager accountManager = new LocalAccountManager(queryManager, databaseManager);

    @BeforeEach
    void setUp() throws DatabaseException {
        when(queryManager.makeLoadQuery(any(), any()))
                .thenReturn(new Query<>());
        // Default to returning an empty list
        when(databaseManager.loadObjects(any(Query.class), any(Object[].class)))
                .thenReturn(List.of());

        BOBS_ACCOUNT.setId(UUID.randomUUID());
        NEW_ACCOUNT.setGivenName("bob");
        NEW_ACCOUNT.setSurname("youruncle");
        BOBS_ACCOUNT.setAlias("bobsyouruncle");
        BOBS_ACCOUNT.setEmail("bob123@rice.edu");

        // password = "password'
        BOBS_ACCOUNT.setPassword("d5771df6c7dcd34d64717fa22158b161857195f46e3704df76f6045839aaccabed7cbc8ee824c4eef6c2012e4e9e2f5db4e29241de5eb928cd0ce14560c315bc$46db9e126d858b68392159a6beb4ac57");
        when(databaseManager.loadObjects(any(Query.class), eq(BOBS_ACCOUNT.getId())))
                .thenReturn(List.of(BOBS_ACCOUNT));
        when(databaseManager.loadObjects(any(Query.class), eq(BOBS_ACCOUNT.getEmail())))
                .thenReturn(List.of(BOBS_ACCOUNT));
        when(databaseManager.loadObjects(any(Query.class), eq(BOBS_ACCOUNT.getAlias())))
                .thenReturn(List.of(BOBS_ACCOUNT));

        NEW_ACCOUNT.setId(null);
        NEW_ACCOUNT.setGivenName("foo");
        NEW_ACCOUNT.setSurname("bar");
        NEW_ACCOUNT.setAlias("newuser");
        NEW_ACCOUNT.setEmail("nu123@rice.edu");
        NEW_ACCOUNT.setPassword("password");
    }

    @Test
    void validateLoginWithValidCredentials() throws DatabaseException {
        Credentials creds = new Credentials();
        creds.setEmail("bob123@rice.edu");
        creds.setPassword("password");
        boolean success = accountManager.validateLogin(creds);
        assertTrue(success);
        verify(databaseManager).loadObjects(any(Query.class), eq(BOBS_ACCOUNT.getEmail()));
    }

    @Test
    void validateLoginWithAutomaticFail() throws DatabaseException {
        Credentials creds = new Credentials();
        creds.setEmail("bob123@gmail.edu");
        creds.setPassword("password");
        boolean success = accountManager.validateLogin(creds);
        assertFalse(success);
        verify(databaseManager, never()).loadObjects(any(), any(), any());
    }

    @Test
    void validateLoginWithBadPasswordFails() throws DatabaseException {
        Credentials creds = new Credentials();
        creds.setEmail("bob123@rice.edu");
        creds.setPassword("not bob's password");
        boolean success = accountManager.validateLogin(creds);
        assertFalse(success);
        verify(databaseManager).loadObjects(any(Query.class), eq(BOBS_ACCOUNT.getEmail()));
    }

    @Test
    void validateLoginWithBadEmailFails() throws DatabaseException {
        Credentials creds = new Credentials();
        creds.setEmail("foobar@rice.edu");
        creds.setPassword("password");
        boolean success = accountManager.validateLogin(creds);
        assertFalse(success);
        verify(databaseManager).loadObjects(any(Query.class), eq("foobar@rice.edu"));
    }

    @Test
    void retrieveAccountWithUnknownUUID() throws DatabaseException {
        UUID uuid = UUID.randomUUID();
        assertThrows(ObjectNotFoundException.class, () -> accountManager.get(uuid));
        verify(databaseManager).loadObjects(any(Query.class), eq(uuid));
    }

    @Test
    void retrieveAccountWithKnownID() throws DatabaseException, ObjectNotFoundException {
        Account account = accountManager.get(BOBS_ACCOUNT.getId());
        assertNotNull(account);
        verify(databaseManager).loadObjects(any(Query.class), eq(BOBS_ACCOUNT.getId()));
    }

    @Test
    void retrieveAccountWithKnownEmail() throws DatabaseException, ObjectNotFoundException {
        Account account = accountManager.get(BOBS_ACCOUNT.getEmail());
        assertNotNull(account);
        verify(databaseManager).loadObjects(any(Query.class), eq(BOBS_ACCOUNT.getEmail()));
    }

    @Test
    void createsANewAccount() {
        try {
            Account response = accountManager.save(NEW_ACCOUNT);
            assertNotNull(response);
        } catch (BadRequestException | DatabaseException e) {
            e.printStackTrace(System.err);
            fail("saveAccount failed validation when it shouldn't have");
        }
    }

    @Test
    void throwsErrorForFirstName() {
        NEW_ACCOUNT.setGivenName("");

        try {
            accountManager.save(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an first name");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("firstName"));
            assertTrue(badRequestException.getRequestErrors().get("firstName").startsWith("Invalid Name:"));
        } catch (DatabaseException e) {
            e.printStackTrace(System.err);
            fail("saveAccount failed validation in an unexpected way");
        }
    }

    @Test
    void throwsErrorForInvalidLastName() {
        NEW_ACCOUNT.setSurname("");

        try {
            accountManager.save(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an invalid last name");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("lastName"));
            assertTrue(badRequestException.getRequestErrors().get("lastName").startsWith("Invalid Name:"));
        } catch (DatabaseException e) {
            e.printStackTrace(System.err);
            fail("saveAccount failed validation in an unexpected way");
        }
    }

    @Test
    void throwsErrorForInvalidEmail() {
        NEW_ACCOUNT.setEmail("foo@gmail.com");

        try {
            accountManager.save(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an invalid email");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("email"));
            assertTrue(badRequestException.getRequestErrors().get("email").startsWith("Invalid Email:"));
        } catch (DatabaseException e) {
            e.printStackTrace(System.err);
            fail("saveAccount failed validation in an unexpected way");
        }
    }

    @Test
    void throwsErrorForDuplicateEmail() {
        NEW_ACCOUNT.setEmail(BOBS_ACCOUNT.getEmail());

        try {
            accountManager.save(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for a duplicate email");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("email"));
            assertTrue(badRequestException.getRequestErrors().get("email").startsWith("Invalid Email:"));
        } catch (DatabaseException e) {
            e.printStackTrace(System.err);
            fail("saveAccount failed validation in an unexpected way");
        }
    }

    @Test
    void throwsErrorForInvalidAlias() {
        NEW_ACCOUNT.setAlias("a");

        try {
            accountManager.save(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an invalid alias");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("alias"));
            assertTrue(badRequestException.getRequestErrors().get("alias").startsWith("Invalid Alias:"));
        } catch (DatabaseException e) {
            e.printStackTrace(System.err);
            fail("saveAccount failed validation in an unexpected way");
        }
    }

    @Test
    void throwsErrorForDuplicateAlias() {
        NEW_ACCOUNT.setAlias(BOBS_ACCOUNT.getAlias());

        try {
            accountManager.save(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for a duplicate alias");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("alias"));
            assertTrue(badRequestException.getRequestErrors().get("alias").startsWith("Invalid Alias:"));
        } catch (DatabaseException e) {
            e.printStackTrace(System.err);
            fail("saveAccount failed validation in an unexpected way");
        }
    }

    @Test
    void throwsErrorForInvalidPassword() {
        NEW_ACCOUNT.setPassword("foo");

        try {
            accountManager.save(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an invalid password");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("password"));
            assertTrue(badRequestException.getRequestErrors().get("password").startsWith("Invalid Password:"));
        } catch (DatabaseException e) {
            e.printStackTrace(System.err);
            fail("saveAccount failed validation in an unexpected way");
        }
    }
}
