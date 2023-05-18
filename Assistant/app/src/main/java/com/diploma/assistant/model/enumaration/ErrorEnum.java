package com.diploma.assistant.model.enumaration;

public enum ErrorEnum {
    INVALID("INVALID"),
    CONNECTION_TO_INTERNET("Перевірте підключення до інтернету"),
    INVALID_COUNTRY_CODE("Невірний код країни!"),
    NO_MATCH_PASSWORDS("Паролі не співпадають."),
    PASSWORD("Виберіть надійніший пароль. Спробуйте комбінацію літер, цифр і символів від 8 до 16 символів.");
    private final String name;

    ErrorEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
