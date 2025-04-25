package com.example.springApp.waterservice;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class JavaFXExample extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Создание корневого контейнера
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Создание компонентов
        Label label = new Label("Введите текст:");
        TextField textField = new TextField();
        Button button = new Button("Нажми меня");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);

        // Обработчик события для кнопки
        button.setOnAction(e -> {
            textArea.appendText(textField.getText() + "\n");
            textField.clear();
        });

        // Добавление компонентов в контейнер
        root.getChildren().addAll(label, textField, button, textArea);

        // Создание сцены и отображение
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Пример JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
