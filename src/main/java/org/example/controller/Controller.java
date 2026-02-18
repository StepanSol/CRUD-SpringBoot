package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.ProductDTOInput;
import org.example.readerManager.ReaderManager;
import org.example.service.Service;
import org.springframework.boot.CommandLineRunner;

import java.io.BufferedReader;
import java.math.BigDecimal;

@RequiredArgsConstructor
@org.springframework.stereotype.Controller
public class Controller implements CommandLineRunner {

    private final Service service;
    private final BufferedReader reader = ReaderManager.getReader();
    private boolean isActive = true;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Напишите в консоль номер действия: \n" +
                "1. Вывести список товаров \n" +
                "2. Добавление товара в базу данных \n" +
                "3. Вывод информации о товаре по id \n" +
                "4. Удаление товара по id \n" +
                "5. Обновление цены товара \n" +
                "Для завершения работы введите 0");


        while (isActive) {
            try {
                byte numberOfAction = Byte.parseByte(reader.readLine());

                switch (numberOfAction){
                    case 1:
                        service.displayTable();
                        break;
                    case 2:
                        ProductDTOInput productDTOInput = new ProductDTOInput();
                        System.out.println("Введите название товара");
                        productDTOInput.setName(reader.readLine());
                        System.out.println("Введите стоимость");
                        productDTOInput.setPrice(BigDecimal.valueOf(Double.parseDouble(reader.readLine())));
                        service.addProduct(productDTOInput);
                        break;
                    case 3:
                        System.out.println("Введите ID товара для отображения");
                        int id = Integer.parseInt(reader.readLine());
                        service.displayProductByID(id);
                        break;
                    case 4:
                        System.out.println("Введите ID товара для удаления");
                        int idToRemove = Integer.parseInt(reader.readLine());
                        service.removeProductByID(idToRemove);
                        break;
                    case 5:
                        System.out.println("Введите ID товара для изменения его цены");
                        int idToUpdate = Integer.parseInt(reader.readLine());
                        if (service.isPresentByID(idToUpdate)){
                            System.out.println("Введите новую цену");
                            BigDecimal newPrice = BigDecimal.valueOf(Double.parseDouble(reader.readLine()));
                            service.updateProductByID(idToUpdate, newPrice);
                            System.out.println("Цена товара обновлена:");
                            service.displayProductByID(idToUpdate);
                        }else {
                            System.out.println("Товар с указанным ID не найден");
                        }
                        break;
                    case 0:
                        reader.close();
                        isActive = false;
                        break;
                    default:
                        System.out.println("Введите число от 0 до 5");
                }

            } catch (NumberFormatException ex) {
                System.out.println("Введены неверные данные");
            }
        }
    }
}
