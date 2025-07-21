package org.example.service;

import org.example.model.Product;
import org.example.model.ProductDTOInput;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ServiceTest {

    private PrintStream originalOut;
    private PrintStream mockPrintStream;

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private Service service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        mockPrintStream = mock(PrintStream.class);
        System.setOut(mockPrintStream);
    }
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void displayTable_NoData(){
        service.displayTable();
        verify(repository, times(1)).findAll();
        verify(mockPrintStream).println("В базе данных нет товаров.");
    }

    @Test
    void displayTable_WithDataAndInvokeFindAll(){
        Service serviceSpy = spy(service);

        when(repository.findAll()).thenReturn(List.of(new Product(1, "TestProduct", new BigDecimal("33.22"))));
        serviceSpy.displayTable();
        verify(repository).findAll();
        verify(serviceSpy).displayProduct(any());
    }

    @Test
    void addProduct_TestCheckIfAlreadyExist() {
        Service service = spy(this.service);
        when(service.isPresentByName("TestProduct")).thenReturn(true);

        ProductDTOInput testProduct = new ProductDTOInput();
        testProduct.setName("TestProduct");
        testProduct.setPrice(BigDecimal.valueOf(33.32));

        service.addProduct(testProduct);
        verify(mockPrintStream).println("Товар с этим названием уже существует.");
    }

    @Test
    void addProduct_InvokeSave(){
        Service serviceSpy = spy(this.service);
        ProductDTOInput testProduct2 = new ProductDTOInput();
        testProduct2.setName("TestProduct");
        testProduct2.setPrice(BigDecimal.valueOf(33.32));
        serviceSpy.addProduct(testProduct2);

        verify(repository).save(any());
        verify(serviceSpy).isPresentByName("TestProduct");
    }

    @Test
    void displayProductByID_InvokeFindByIdAndWriteSomething(){
        Product testProduct = new Product(1, "testProduct", BigDecimal.valueOf(33.22));
        Optional<Product> returned = Optional.of(testProduct);
        when(repository.findById(1)).thenReturn(returned);
        service.displayProductByID(1);
        verify(repository).findById(1);
        verify(mockPrintStream).println(Optional.ofNullable(any()));
    }


    @Test
    void removeProductByID_NotFound() {
        service.removeProductByID(2);
        verify(mockPrintStream).println("Товар с указанным ID не найден");
    }

    @Test
    void removeProductByID_DeleteAndMakeReport(){
        when(repository.existsById(1)).thenReturn(true);
        service.removeProductByID(1);

        verify(repository).existsById(1);
        verify(repository).deleteById(1);
        verify(mockPrintStream).println("Товар с ID=" + 1 + " успешно удалён");
    }

    @Test
    void updateProductByID_ChangeSumAndSave(){
        Product testProduct = new Product(1, "testProduct", BigDecimal.valueOf(33.22));
        when(repository.findById(1)).thenReturn(Optional.of(testProduct));

        service.updateProductByID(1, BigDecimal.valueOf(22.33));
        Assertions.assertEquals(BigDecimal.valueOf(22.33), testProduct.getPrice());
        verify(repository).save(testProduct);
    }
}