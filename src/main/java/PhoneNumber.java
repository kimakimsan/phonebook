package com.example.main;

import java.io.Serializable;

public class PhoneNumber implements Serializable {
    private String number; //номер телефона
    private String type; //тип номера телефона

    public PhoneNumber(String number, String type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number; //возвращение номера телефона
    }

    public String getType() {
        return type; //возвращает тип номера телефона
    }

    @Override
    public String toString() {
        return type + ": " + number; //возвращение строки с типом и номером
    }
}







