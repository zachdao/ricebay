package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.store.DatabaseManager;
import edu.rice.comp610.store.Query;
import edu.rice.comp610.store.QueryManager;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserManagerTest {

    Account BOBS_ACCOUNT = new Account();
    Account NEW_ACCOUNT = new Account();

    QueryManager queryManager = new QueryManager();
    DatabaseManager databaseManager = mock(DatabaseManager.class);

    UserManager userManager = new UserManager(queryManager, databaseManager);

    @BeforeEach
    void setUp() throws SQLException {
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
    void validateLoginWithValidCredentials() throws SQLException {
        try {
            AppResponse<Account> response = userManager.validateLogin("bob123@rice.edu", "password");
            assertTrue(response.isSuccess());
            assertNotNull(response.getData());
            verify(databaseManager).loadObjects(any(Query.class), eq(BOBS_ACCOUNT.getEmail()));
        } catch (UnauthorizedException e) {
            e.printStackTrace(System.err);
            fail("validateLogin threw an exception when it shouldn't have");
        }
    }

    @Test
    void validateLoginWithAutomaticFail() throws SQLException {
        assertThrows(UnauthorizedException.class, () -> userManager.validateLogin("bob123@gmail.com", "password"));
        verify(databaseManager, never()).loadObjects(any(), any(), any());
    }

    @Test
    void validateLoginWithBadPasswordFails() throws SQLException {
        assertThrows(UnauthorizedException.class, () -> userManager.validateLogin("bob123@rice.edu", "not bob's password"));
        verify(databaseManager).loadObjects(any(Query.class), eq(BOBS_ACCOUNT.getEmail()));
    }

    @Test
    void validateLoginWithBadEmailFails() throws SQLException {

        assertThrows(UnauthorizedException.class, () -> userManager.validateLogin("foobar@rice.edu", "password"));
        verify(databaseManager).loadObjects(any(Query.class), eq("foobar@rice.edu"));
    }

    @Test
    void createsANewAccount() {
        try {
            AppResponse<Account> response = userManager.saveAccount(NEW_ACCOUNT);
            assertTrue(response.isSuccess());
            assertNotNull(response.getData());
        } catch (BadRequestException e) {
            e.printStackTrace(System.err);
            fail("saveAccount failed validation when it shouldn't have");
        }
    }

    @Test
    void throwsErrorForFirstName() {
        NEW_ACCOUNT.setGivenName("");

        try {
            userManager.saveAccount(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an first name");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("firstName"));
            assertTrue(badRequestException.getRequestErrors().get("firstName").startsWith("Invalid Name:"));
        }
    }

    @Test
    void throwsErrorForInvalidLastName() {
        NEW_ACCOUNT.setSurname("");

        try {
            userManager.saveAccount(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an invalid last name");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("lastName"));
            assertTrue(badRequestException.getRequestErrors().get("lastName").startsWith("Invalid Name:"));
        }
    }

    @Test
    void throwsErrorForInvalidEmail() {
        NEW_ACCOUNT.setEmail("foo@gmail.com");

        try {
            userManager.saveAccount(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an invalid email");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("email"));
            assertTrue(badRequestException.getRequestErrors().get("email").startsWith("Invalid Email:"));
        }
    }

    @Test
    void throwsErrorForDuplicateEmail() {
        NEW_ACCOUNT.setEmail(BOBS_ACCOUNT.getEmail());

        try {
            userManager.saveAccount(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for a duplicate email");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("email"));
            assertTrue(badRequestException.getRequestErrors().get("email").startsWith("Invalid Email:"));
        }
    }

    @Test
    void throwsErrorForInvalidAlias() {
        NEW_ACCOUNT.setAlias("a");

        try {
            userManager.saveAccount(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an invalid alias");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("alias"));
            assertTrue(badRequestException.getRequestErrors().get("alias").startsWith("Invalid Alias:"));
        }
    }

    @Test
    void throwsErrorForDuplicateAlias() {
        NEW_ACCOUNT.setAlias(BOBS_ACCOUNT.getAlias());

        try {
            userManager.saveAccount(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for a duplicate alias");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("alias"));
            assertTrue(badRequestException.getRequestErrors().get("alias").startsWith("Invalid Alias:"));
        }
    }

    @Test
    void throwsErrorForInvalidPassword() {
        NEW_ACCOUNT.setPassword("foo");

        try {
            userManager.saveAccount(NEW_ACCOUNT);
            fail("saveAccount should have thrown an exception for an invalid password");
        } catch (BadRequestException badRequestException) {
            assertTrue(badRequestException.getRequestErrors().containsKey("password"));
            assertTrue(badRequestException.getRequestErrors().get("password").startsWith("Invalid Password:"));
        }
    }
}
