package edu.rice.comp610.model;

import java.util.Objects;

/**
 * A user's account information. Includes the following fields
 *
 * <ul>
 *
 * <li> alias - the user's chosen login name (must be unique in the system)
 * <li> email - the user's email address (must be unique in the system)
 * <li> givenName - the user's given name
 * <li> surname - the user's surname
 * <li> password - the password selected by the user
 * <li> zelleId - the user's Zelle ID
 * </ul>
 *
 */
public class Account {
    private String alias;
    private String email;
    private String givenName;
    private String surname;
    private String password;
    private String zelleId;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getZelleId() {
        return zelleId;
    }

    public void setZelleId(String zelleId) {
        this.zelleId = zelleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(alias, account.alias) && Objects.equals(email, account.email) && Objects.equals(givenName, account.givenName) && Objects.equals(surname, account.surname) && Objects.equals(password, account.password) && Objects.equals(zelleId, account.zelleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias, email, givenName, surname, password, zelleId);
    }
}
