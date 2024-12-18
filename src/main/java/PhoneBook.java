package com.example.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhoneBook implements Serializable {
    private List<Contact> contacts = new ArrayList<>(); //список всех контактов

    public List<Contact> getContacts() {
        return contacts; //возвращение всего списка
    }

    public void addContact(Contact contact) {
        contacts.add(contact); //добавление контакта
    }

    public Contact findContactByName(String name) { //сортировка по имени
        return contacts.stream().filter(c -> c.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}





