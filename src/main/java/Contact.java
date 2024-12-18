package com.example.main;

import java.io.Serializable;
import java.util.List;

public class Contact implements Serializable {
    private String name; //имя контакта
    private List<PhoneNumber> phoneNumbers;

    public Contact(String name, List<PhoneNumber> phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public String getName() {
        return name; //возвращение имени контакта
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers; //возвращение списка номеров
    }

    @Override
    public String toString() {
        return name + " " + phoneNumbers; //возвращение строки с именем и номерами
    }
}






