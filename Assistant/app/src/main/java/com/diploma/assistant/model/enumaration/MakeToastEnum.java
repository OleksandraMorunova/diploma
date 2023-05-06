package com.diploma.assistant.model.enumaration;

public enum MakeToastEnum {
    NO_CONNECTION_WITH_SERVER("Немає зв'язку з сервером"),
    NO_CONNECTION_WITH_INTERNET("Немає мережі"),
    CAUSE("Щось пішло не так"),
    NO_RIGHT_CODE("Невірний код"),
    NO_PERMISSION("Немає дозволу"),
    NO_EXIST_PHONE_NUMBER("Не існує такого номеру"),
    UPDATE_DETAILS("Дані успішно додані"),
    CREATE_USER("Користувача успішно додано");

    private final String error;

    MakeToastEnum(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
