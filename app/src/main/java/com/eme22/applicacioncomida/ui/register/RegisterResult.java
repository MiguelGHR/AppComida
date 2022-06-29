package com.eme22.applicacioncomida.ui.register;

import androidx.annotation.Nullable;

import com.eme22.applicacioncomida.data.model.User;

/**
 * Authentication result : success (user details) or error message.
 */
class RegisterResult {
    @Nullable
    private User success;
    @Nullable
    private Integer error;

    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    RegisterResult(@Nullable User success) {
        this.success = success;
    }

    @Nullable
    User getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}