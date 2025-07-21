package org.example.service;

import org.example.model.Product;
import org.example.model.ProductDTOInput;
Simport org.example.repository.ProductRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {
    private final ProductRepository repository;

    public Service(ProductRepository repository) {
        this.repository = repository;
    }

    public void displayTable() {
        List<Product> allProducts = repository.findAll();
        if (allProducts.isEmpty()) {
            System.out.println("В базе данных нет товаров.");
        } else {
            for (Product product : allProducts) {
                displayProduct(product);
            }
        }
    }

    public void addProduct(ProductDTOInput productDTOInput) {
        if (isPresentByName(productDTOInput.getName())) {
            System.out.println("Товар с этим названием уже существует.");
        } else {
            repository.save(new Product(productDTOInput.getName(), productDTOInput.getPrice()));
        }
    }

    public void displayProductByID(int id){
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isPresent()) {
            displayProduct(optionalProduct.get());
        } else {
            System.out.println("Товар с указанным ID не найден");
        }
    }


    public void removeProductByID(int id){
        if (repository.existsById(id)) {
            repository.deleteById(id);
            System.out.println("Товар с ID=" + id + " успешно удалён");
        } else {
            System.out.println("Товар с указанным ID не найден");
        }
    }

    public void updateProductByID(int id, BigDecimal newPrice){
        Product required = repository.findById(id).get();
        required.setPrice(newPrice);
        repository.save(required);
    }

    public void displayProduct(Product product) {
        System.out.println(product.toOutputDTO());
    }

    public boolean isPresentByID(int id) {
        Optional<Product> optionalProduct = repository.findById(id);
        return optionalProduct.isPresent();
    }

    public boolean isPresentByName(String name) {
        return repository.findByName(name).isPresent();
    }
}