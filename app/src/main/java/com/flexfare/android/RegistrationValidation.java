package com.flexfare.android;

/**
 * Created by Blessyn on 7/31/2017.
 */

public interface RegistrationValidation {
    void showEmptyFieldsError();
    void doRegistratio();
    void showInvalidEmailError();
    void showInvalidPhoneNumberError();
    void showRegistrationSuccess();
}
