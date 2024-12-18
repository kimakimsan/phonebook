package com.example.main;

import java.io.*;
import java.util.List;

public class FileManager {
    private static final String FILE_NAME = "phonebook.dat"; //название файла для сохранения информации

    public static void saveToFile(List<Contact> contacts) { //сохранение информации в файл
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(contacts);
        } catch (IOException e) {
            e.printStackTrace(); //вывод ошибки в консоль
        }
    }

    public static List<Contact> loadFromFile() { //подгрузка информации из файла
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Contact>) in.readObject(); //возвращение загруженного списка
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}




