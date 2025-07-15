package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.readerManager.ReaderManager;
import org.example.service.Service;
import org.springframework.boot.CommandLineRunner;

import java.io.BufferedReader;

@RequiredArgsConstructor
@org.springframework.stereotype.Controller
public class Controller implements CommandLineRunner {

    private final Service service;
    private final BufferedReader reader = ReaderManager.getReader();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Напишите в консоль номер действия: \n" +
                "1. Вывести список товаров \n" +
                "2. Добавление товара в базу данных \n" +
                "3. Вывод информации о товаре по id \n" +
                "4. Удаление товара по id \n" +
                "5. Обновление цены товара \n" +
                "Для завершения работы введите 0");


        while (true) {
            try {
                byte numberOfAction = Byte.parseByte(reader.readLine());
                if (numberOfAction == 1) {
                    service.displayTable();
                } else if (numberOfAction == 2) {
                    service.addProduct();
                } else if (numberOfAction == 3) {
                    service.displayProductByID();
                } else if (numberOfAction == 4) {
                    service.removeProductByID();
                } else if (numberOfAction == 5) {
                    service.updateProductByID();
                } else if (numberOfAction == 0) {
                    reader.close();
                    break;
                } else {
                    System.out.println("Введите число от 0 до 5");
                }
            } catch (NumberFormatException ex) {
                System.out.println("Введены неверные данные");
            }
        }
    }
}
