package org.example.service;

import org.example.model.Product;
import org.example.model.ProductDTOInput;
import org.example.readerManager.ReaderManager;
import org.example.repository.ProductRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {
    private final ProductRepository repository;
    private final BufferedReader reader = ReaderManager.getReader();
    public Service(ProductRepository repository) {
        this.repository = repository;
    }

    public void displayTable() {
        List<Product> allProducts = repository.findAll();
        if (allProducts.isEmpty()){
            System.out.println("В базе данных нет товаров.");
        }else {
            allProducts.stream()
                            .map(Product::toOutputDTO)
                            .forEach(System.out::println);
        }
    }

    public void addProduct() throws IOException {
        ProductDTOInput productDTOInput = new ProductDTOInput();
        System.out.println("Введите название товара");
        productDTOInput.setName(reader.readLine());
        System.out.println("Введите стоимость");
        productDTOInput.setPrice(BigDecimal.valueOf(Double.parseDouble(reader.readLine())));

        repository.save(new Product(productDTOInput.getName(), productDTOInput.getPrice()));
    }

    public void displayProductByID() throws IOException {
        System.out.println("Введите ID товара для отображения");
            int id = Integer.parseInt(reader.readLine());
            Optional<Product> optionalProduct = repository.findById(id);

            if (optionalProduct.isPresent()){
                System.out.println(optionalProduct.get().toOutputDTO());
            }else {
                System.out.println("Товар с указанным ID не найден");
            }
    }


    public void removeProductByID() throws IOException {
        System.out.println("Введите ID товара для удаления");
        int id = Integer.parseInt(reader.readLine());

        if (repository.existsById(id)){
            repository.deleteById(id);
            System.out.println("Товар с ID=" + id + " успешно удалён");
        }else {
            System.out.println("Товар с указанным ID не найден");
        }
    }

    public void updateProductByID() throws IOException {
        System.out.println("Введите ID товара для изменения его цены");
        int id = Integer.parseInt(reader.readLine());
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isPresent()){
            Product required = optionalProduct.get();
            System.out.println("Введите новую цену");
            required.setPrice(BigDecimal.valueOf(Double.parseDouble(reader.readLine())));
            repository.save(required);
            System.out.println("Цена товара обновлена:");
            System.out.println(repository.findById(id).get().toOutputDTO());
        }else {
            System.out.println("Товар с указанным ID не найден");
        }
    }
}
