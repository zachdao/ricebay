package edu.rice.comp610.controller;

import edu.rice.comp610.model.Account;
import edu.rice.comp610.util.DatabaseException;
import edu.rice.comp610.util.ObjectNotFoundException;
import edu.rice.comp610.util.BadRequestException;
import edu.rice.comp610.util.UnauthorizedException;

public class AccountAdapter {

    private final AccountManager accountManager;

    public AccountAdapter(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    AppResponse<?> register(ViewAccount viewAccount, Credentials credentials) {
        Account account = new Account();
        account.setGivenName(viewAccount.getGivenName());
        account.setSurname(viewAccount.getSurname());
        account.setAlias(viewAccount.getAlias());
        account.setEmail(credentials.getEmail());
        account.setPassword(credentials.getPassword());
        try {
            Account newAccount = this.accountManager.save(account);
            return new AppResponse<>(201, true, newAccount.getId(), "OK");
        } catch (BadRequestException e) {
            return new AppResponse<>(400, false, e.getRequestErrors(), "Bad Request");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        }
    }

    AppResponse<?> update(ViewAccount viewAccount) {
        try {
            Account account = this.accountManager.get(viewAccount.getId());
            if (!viewAccount.getGivenName().isEmpty()) {
                account.setGivenName(viewAccount.getGivenName());
            }
            if (!viewAccount.getSurname().isEmpty()) {
                account.setSurname(viewAccount.getSurname());
            }
            if (!viewAccount.getAlias().isEmpty()) {
                account.setAlias(viewAccount.getAlias());
            }
            if (!viewAccount.getZelleId().isEmpty()) {
                account.setZelleId(viewAccount.getZelleId());
            }
            if (!viewAccount.getImage().isEmpty()) {
                account.setImage(viewAccount.getImage().getBytes());
            }
            this.accountManager.save(account);
            return new AppResponse<>(200, true, account.getId(), "OK");
        } catch (ObjectNotFoundException e) {
            return new AppResponse<>(404, false, null, "Not Found");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        } catch (BadRequestException e) {
            return new AppResponse<>(400, false, e.getRequestErrors(), "Bad Request");
        }
    }

    AppResponse<?> login(Credentials credentials) {
        if (this.accountManager.validateLogin(credentials)) {
            return new AppResponse<>(200, true, null, "OK");
        }
        return new AppResponse<>(401, false, null, "Unauthorized");
    }

    AppResponse<?> updatePassword(Credentials credentials, String newPassword) {
        try {
            this.accountManager.savePassword(credentials, newPassword);
            return new AppResponse<>(200, true, null, "OK");
        } catch (BadRequestException e) {
            return new AppResponse<>(400, false, e.getRequestErrors(), "Bad Request");
        } catch (UnauthorizedException e) {
            return new AppResponse<>(401, false, null, "Unauthorized");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        }
    }

    AppResponse<ViewAccount> me(String email) {
        try {
            Account account = this.accountManager.get(email);
            ViewAccount viewAccount = new ViewAccount();
            viewAccount.setId(account.getId());
            viewAccount.setGivenName(account.getGivenName());
            viewAccount.setSurname(account.getSurname());
            viewAccount.setEmail(account.getEmail());
            viewAccount.setAlias(account.getAlias());
            if (account.getZelleId() != null) {
                viewAccount.setZelleId(account.getZelleId());
            }
            if (account.getImage() != null) {
                viewAccount.setImage(new String(account.getImage()));
            }
            return new AppResponse<>(200, true, viewAccount, "OK");
        } catch (ObjectNotFoundException e) {
            return new AppResponse<>(404, false, null, "Not Found");
        } catch (DatabaseException e) {
            return new AppResponse<>(500, false, null, "Internal Server Error");
        }
    }
}
