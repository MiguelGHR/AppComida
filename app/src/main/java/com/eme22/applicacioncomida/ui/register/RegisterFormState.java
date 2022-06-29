package com.eme22.applicacioncomida.ui.register;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class RegisterFormState {

    @Nullable
    private Integer nameError;
    @Nullable
    private Integer lastNameError;
    @Nullable
    private Integer phoneError;

    @Nullable
    private Integer addressError;
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;


    private boolean isDataValid;

    public RegisterFormState(@Nullable Integer nameError, @Nullable Integer lastNameError, @Nullable Integer phoneError, @Nullable Integer addressError, @Nullable Integer usernameError, @Nullable Integer passwordError) {
        this.nameError = nameError;
        this.lastNameError = lastNameError;
        this.phoneError = phoneError;
        this.addressError = addressError;
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }


    RegisterFormState(boolean isDataValid) {
        this.nameError = null;
        this.lastNameError = null;
        this.phoneError = null;
        this.addressError = null;
        this.usernameError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

    @Nullable
    public Integer getNameError() {
        return nameError;
    }

    @Nullable
    public Integer getLastNameError() {
        return lastNameError;
    }

    @Nullable
    public Integer getPhoneError() {
        return phoneError;
    }

    @Nullable
    public Integer getAddressError() {
        return addressError;
    }
}