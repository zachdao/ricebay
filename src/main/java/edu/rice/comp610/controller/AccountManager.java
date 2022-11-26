package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.ObjectNotFoundException;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.UnauthorizedException;

import java.util.UUID;

public interface AccountManager {
    Account save(Account account) throws BadRequestException, DatabaseException;
    Account get(String email) throws ObjectNotFoundException, DatabaseException;
    Account get(UUID id) throws ObjectNotFoundException, DatabaseException;
    boolean validateLogin(Credentials credentials);
    void savePassword(Credentials credentials, String newPassword) throws BadRequestException, UnauthorizedException, DatabaseException;
}
