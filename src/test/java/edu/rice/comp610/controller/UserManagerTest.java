package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.store.DatabaseManager;
import edu.rice.comp610.util.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserManagerTest {

    Account BOBS_ACCOUNT = new Account();

    DatabaseManager databaseManager = mock(DatabaseManager.class);

    UserManager userManager = new UserManager(databaseManager);

    @BeforeEach
    void setUp() {
        BOBS_ACCOUNT.setId(null);
        BOBS_ACCOUNT.setAlias("bobsyouruncle");
        BOBS_ACCOUNT.setEmail("bob123@rice.edu");

        // password = "password'
        BOBS_ACCOUNT.setPassword("d5771df6c7dcd34d64717fa22158b161857195f46e3704df76f6045839aaccabed7cbc8ee824c4eef6c2012e4e9e2f5db4e29241de5eb928cd0ce14560c315bc$46db9e126d858b68392159a6beb4ac57");
    }

    @Test
    void validateLoginWithValidCredentials() {
        when(databaseManager.loadObjects(eq(Account.class), anyString(), eq(BOBS_ACCOUNT.getEmail())))
                .thenReturn(List.of(BOBS_ACCOUNT));

        try {
            AppResponse<Account> response = userManager.validateLogin("bob123@rice.edu", "password");
            assertTrue(response.isSuccess());
            assertNotNull(response.getData());
            verify(databaseManager).loadObjects(eq(Account.class), anyString(), eq(BOBS_ACCOUNT.getEmail()));
        } catch (UnauthorizedException e) {
            e.printStackTrace(System.err);
            fail("validateLogin threw an exception when it shouldn't have");
        }
    }

    @Test
    void validateLoginWithAutomaticFail() {
        assertThrows(UnauthorizedException.class, () -> userManager.validateLogin("bob123@gmail.com", "password"));
        verify(databaseManager, never()).loadObjects(any(), any(), any());
    }

    @Test
    void validateLoginWithBadPasswordFails() {
        when(databaseManager.loadObjects(eq(Account.class), anyString(), eq(BOBS_ACCOUNT.getEmail())))
                .thenReturn(List.of(BOBS_ACCOUNT));

        assertThrows(UnauthorizedException.class, () -> userManager.validateLogin("bob123@rice.edu", "not bob's password"));
        verify(databaseManager).loadObjects(eq(Account.class), anyString(), eq(BOBS_ACCOUNT.getEmail()));
    }

    @Test
    void validateLoginWithBadEmailFails() {
        when(databaseManager.loadObjects(any(), any(), any())).thenReturn(List.of());
        when(databaseManager.loadObjects(eq(Account.class), anyString(), eq(BOBS_ACCOUNT.getEmail())))
                .thenReturn(List.of(BOBS_ACCOUNT));

        assertThrows(UnauthorizedException.class, () -> userManager.validateLogin("foobar@rice.edu", "password"));
        verify(databaseManager).loadObjects(eq(Account.class), anyString(), eq("foobar@rice.edu"));
    }
}
