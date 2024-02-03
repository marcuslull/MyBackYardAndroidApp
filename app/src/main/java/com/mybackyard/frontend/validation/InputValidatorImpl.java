package com.mybackyard.frontend.validation;

public class InputValidatorImpl implements InputValidator{
    @Override
    public boolean isValidString(String string) {
        return string.matches("^[a-zA-Z0-9 ]+( ?[a-zA-Z0-9 -./?!$%&*(),;:@#_]+)*$");
    }

    @Override
    public boolean isValidAPIKey(String string) {
        return string.matches("^[0-9a-fA-F_]+mby$");
    }
}
