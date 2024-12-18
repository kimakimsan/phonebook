package com.example.main;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main extends Application {

    private PhoneBook phoneBook = new PhoneBook();

    @Override
    public void start(Stage primaryStage) {
        //загружаем контакты из файла
        List<Contact> loadedContacts = FileManager.loadFromFile();
        if (loadedContacts != null) {
            phoneBook.getContacts().addAll(loadedContacts);
        }

        //основной интерфейс
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label headerLabel = new Label("Телефонный справочник");

        VBox contactListBox = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(contactListBox); //функция скроллинга
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        Button addButton = new Button("Добавить контакт"); //функциональные кнопки
        Button deleteButton = new Button("Удалить контакт");
        Button sortButton = new Button("Сортировать по имени");

        //обработчики кнопок
        addButton.setOnAction(e -> addContact(contactListBox));
        deleteButton.setOnAction(e -> deleteContact(contactListBox));
        sortButton.setOnAction(e -> sortContacts(contactListBox));

        HBox buttonBox = new HBox(10, addButton, deleteButton, sortButton);

        root.getChildren().addAll(headerLabel, scrollPane, buttonBox);

        updateContactList(contactListBox);

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Телефонный справочник");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
//метод для добавления нового контакта
    private void addContact(VBox contactListBox) {
        Dialog<List<String>> dialog = new Dialog<>();
        dialog.setTitle("Добавить контакт");
        dialog.setHeaderText("Введите данные нового контакта");

        TextField nameField = new TextField();
        nameField.setPromptText("Имя");

        TextField numberField = new TextField();
        numberField.setPromptText("Номер телефона");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Имя:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Номер:"), 0, 1);
        grid.add(numberField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return List.of(nameField.getText(), numberField.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String name = result.get(0);
            String number = result.get(1);
            String type = choosePhoneType();

            if (isValidPhoneNumber(number)) {
                PhoneNumber phoneNumber = new PhoneNumber(number, type);
                Contact existingContact = phoneBook.findContactByName(name);

                if (existingContact != null) {
                    existingContact.getPhoneNumbers().add(phoneNumber);
                } else {
                    Contact contact = new Contact(name, new ArrayList<>(List.of(phoneNumber)));
                    phoneBook.addContact(contact);
                }

                updateContactList(contactListBox);
                FileManager.saveToFile(phoneBook.getContacts());
                showInfo("Контакт успешно добавлен.");
            } else {
                showError("Неверный номер телефона. Допустимый формат: +7XXXXXXXXXX или 8XXXXXXXXXX");
            }
        });
    }
//метод для удаления контакта
    private void deleteContact(VBox contactListBox) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Удалить контакт");
        dialog.setHeaderText("Введите имя контакта:");
        dialog.showAndWait().ifPresent(name -> {
            Contact contact = phoneBook.findContactByName(name);
            if (contact != null) {
                List<PhoneNumber> numbers = contact.getPhoneNumbers();
                if (!numbers.isEmpty()) {
                    ChoiceDialog<PhoneNumber> numberDialog = new ChoiceDialog<>(numbers.get(0), numbers);
                    numberDialog.setTitle("Удалить номер");
                    numberDialog.setHeaderText("Выберите номер для удаления:");
                    numberDialog.showAndWait().ifPresent(selectedNumber -> {
                        numbers.remove(selectedNumber);
                        if (numbers.isEmpty()) {
                            phoneBook.getContacts().remove(contact);
                        }
                        updateContactList(contactListBox);
                        FileManager.saveToFile(phoneBook.getContacts());
                        showInfo("Номер успешно удалён.");
                    });
                }
            } else {
                showError("Контакт не найден.");
            }
        });
    }
//метод для сортировки контактов
    private void sortContacts(VBox contactListBox) {
        phoneBook.getContacts().sort(Comparator.comparing(Contact::getName));
        updateContactList(contactListBox);
        FileManager.saveToFile(phoneBook.getContacts());
        showInfo("Контакты отсортированы.");
    }
//метод для выбора типа номера телефона
    private String choosePhoneType() {
        List<String> types = List.of("Домашний", "Рабочий", "Факс");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Домашний", types);
        dialog.setTitle("Тип номера");
        dialog.setHeaderText("Выберите тип номера:");
        return dialog.showAndWait().orElse("Домашний");
    }

    private boolean isValidPhoneNumber(String number) {
        return number.matches("^\\+7\\d{10}$") || number.matches("^8\\d{10}$");
    }
//метод для обновления списка контактов на интерфейсе
    private void updateContactList(VBox contactListBox) {
        contactListBox.getChildren().clear();
        for (Contact contact : phoneBook.getContacts()) {
            contactListBox.getChildren().add(new Label(contact.toString()));
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}






