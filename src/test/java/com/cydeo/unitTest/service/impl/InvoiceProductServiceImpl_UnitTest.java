package com.cydeo.unitTest.service.impl;

import com.cydeo.TestDocumentInitializer;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.exception.AccountingException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import com.cydeo.service.implementation.InvoiceProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InvoiceProductServiceImpl_UnitTest {

    @Mock
    InvoiceProductRepository invoiceProductRepository;

    @Mock
    InvoiceService invoiceService;

    @Mock
    ProductService productService;

    @Spy
    MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @InjectMocks
    InvoiceProductServiceImpl invoiceProductService;


    @Test
    void findInvoiceProductById_happyPath(){

    }

    @Test
    void getInvoiceProductsOfInvoice(){
        // given
        List<InvoiceProductDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct()
        );
        dtoList.get(0).getInvoice().setDate(LocalDate.now().minusDays(5));
        dtoList.get(0).getProduct().setName("first");
        dtoList.get(1).getInvoice().setDate(LocalDate.now().minusDays(3));
        dtoList.get(1).getProduct().setName("second");
        dtoList.get(2).getInvoice().setDate(LocalDate.now().minusDays(10));
        dtoList.get(2).getProduct().setName("third");
        // list of entities with random sort
        List<InvoiceProduct> invoiceProducts = dtoList.stream()
                .map(invoiceProductDto -> mapperUtil.convert(invoiceProductDto, new InvoiceProduct()))
                .collect(Collectors.toList());
        List<InvoiceProductDto> expectedList = dtoList.stream()
                .sorted(Comparator.comparing((InvoiceProductDto each) -> each.getInvoice().getDate()).reversed())
                .peek(each -> each.setTotal(each.getPrice().multiply(BigDecimal.valueOf(each.getQuantity() * (each.getTax() + 100) / 100d))))
                .collect(Collectors.toList());

        // when
        when(invoiceService.findInvoiceById(1L)).thenReturn(TestDocumentInitializer.getInvoice(InvoiceStatus.AWAITING_APPROVAL, InvoiceType.PURCHASE));
        when(invoiceProductRepository.findAllByInvoice(any())).thenReturn(invoiceProducts);

        List<InvoiceProductDto> actualList = invoiceProductService.getInvoiceProductsOfInvoice(1L);

        // then
        assertThat(actualList).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(expectedList);
    }

    @Test
    void save(){

    }

    @Test
    void completeApprovalProcedures_SALES_happyPath_withoutProfitLoss(){
        // given
        List<InvoiceProductDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct()
        );
        dtoList.get(0).getInvoice().setInvoiceType(InvoiceType.SALES);
        dtoList.get(1).getInvoice().setInvoiceType(InvoiceType.SALES);
        dtoList.get(2).getInvoice().setInvoiceType(InvoiceType.SALES);
        List<InvoiceProduct> invoiceProducts = dtoList.stream()
                .map(invoiceProductDto -> mapperUtil.convert(invoiceProductDto, new InvoiceProduct()))
                .collect(Collectors.toList());
        // when
        when(invoiceProductRepository.findAllByInvoice_Id(1L)).thenReturn(invoiceProducts);

        invoiceProductService.completeApprovalProcedures(1L, InvoiceType.SALES);

        // then
        verify(invoiceProductRepository, times(invoiceProducts.size())).save(any());
        verify(productService, times(invoiceProducts.size())).update(any(), any());
        invoiceProducts.forEach(
                invoiceProduct -> assertEquals(10,invoiceProduct.getRemainingQuantity())
        );

        // decrease of quantity in stock cannot be tracked since it is done inside another method and object changes and java is pass-by-value
//        dtoList.forEach(
//                dto -> assertEquals(0, dto.getProduct().getQuantityInStock())
//        );
    }

    @Test
    void completeApprovalProcedures_SALES_happyPath_with_ProfitLoss(){
        // given
        List<InvoiceProductDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct());
        dtoList.get(0).getInvoice().setInvoiceType(InvoiceType.SALES);
        dtoList.get(0).getProduct().setName("first");
        dtoList.get(0).setProfitLoss(BigDecimal.ZERO);
        dtoList.get(1).setProfitLoss(BigDecimal.ZERO);
        dtoList.get(1).getProduct().setName("second");
        dtoList.get(1).getProduct().setQuantityInStock(25);
        dtoList.get(1).getInvoice().setInvoiceType(InvoiceType.SALES);
        dtoList.get(1).setQuantity(15);
        List<InvoiceProduct> soldInvoiceProducts = dtoList.stream()
                .map(invoiceProductDto -> mapperUtil.convert(invoiceProductDto, new InvoiceProduct()))
                .collect(Collectors.toList());

        //  purchase list for first item
        List<InvoiceProductDto> dtoListPurchase1 = Arrays.asList(
                TestDocumentInitializer.getInvoiceProduct()
        );
        dtoListPurchase1.get(0).getInvoice().setInvoiceType(InvoiceType.PURCHASE);
        dtoListPurchase1.get(0).setProduct(dtoList.get(0).getProduct());
        dtoListPurchase1.get(0).setRemainingQuantity(10);

        List<InvoiceProduct> availableProductsForSale1 = dtoList.stream()
                .map(invoiceProductDto -> mapperUtil.convert(invoiceProductDto, new InvoiceProduct()))
                .collect(Collectors.toList());

        // purchase list for second item
        List<InvoiceProductDto> dtoListPurchase2 = Arrays.asList(
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct()
        );
        dtoListPurchase2.get(0).getInvoice().setInvoiceType(InvoiceType.PURCHASE);
        dtoListPurchase2.get(0).setProduct(dtoList.get(1).getProduct());
        dtoListPurchase2.get(0).setRemainingQuantity(10);
        dtoListPurchase2.get(1).getInvoice().setInvoiceType(InvoiceType.PURCHASE);
        dtoListPurchase2.get(1).setRemainingQuantity(10);
        dtoListPurchase2.get(1).setProduct(dtoList.get(1).getProduct());

        List<InvoiceProduct> availableProductsForSale2 = dtoList.stream()
                .map(invoiceProductDto -> mapperUtil.convert(invoiceProductDto, new InvoiceProduct()))
                .collect(Collectors.toList());

        // when
        when(invoiceProductRepository.findAllByInvoice_Id(1L)).thenReturn(soldInvoiceProducts);
        when(invoiceProductRepository.findInvoiceProductsByInvoiceInvoiceTypeAndProductAndRemainingQuantityNotOrderByIdAsc(any(),any(),any()))
                .thenReturn(availableProductsForSale1)
                .thenReturn(availableProductsForSale2);

        invoiceProductService.completeApprovalProcedures(1L, InvoiceType.SALES);

        // then
        assertEquals(0, availableProductsForSale1.get(0).getRemainingQuantity());
        assertEquals(0, availableProductsForSale2.get(0).getRemainingQuantity());
        assertEquals(5, availableProductsForSale2.get(1).getRemainingQuantity());


        // decrease of quantity in stock cannot be tracked since it is done inside another method and object changes and java is pass-by-value
//        dtoList.forEach(
//                dto -> assertEquals(0, dto.getProduct().getQuantityInStock())
//        );
    }

    @Test
    void completeApprovalProcedures_SALES_throws_exception(){
        // given
        List<InvoiceProductDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct()
        );
        dtoList.get(0).getInvoice().setInvoiceType(InvoiceType.SALES);
        dtoList.get(1).getInvoice().setInvoiceType(InvoiceType.SALES);
        dtoList.get(2).getInvoice().setInvoiceType(InvoiceType.SALES);
        dtoList.get(2).getProduct().setQuantityInStock(5);
        List<InvoiceProduct> invoiceProducts = dtoList.stream()
                .map(invoiceProductDto -> mapperUtil.convert(invoiceProductDto, new InvoiceProduct()))
                .collect(Collectors.toList());
        // when
        when(invoiceProductRepository.findAllByInvoice_Id(1L)).thenReturn(invoiceProducts);

        Throwable throwable = catchThrowable(() -> invoiceProductService.completeApprovalProcedures(1L, InvoiceType.SALES));
        // then
        assertInstanceOf(AccountingException.class, throwable);
        assertEquals("This sale cannot be completed due to insufficient quantity of the product", throwable.getMessage());
    }

    @Test
    void completeApprovalProcedures_PURCHASE_happyPath() {
        // given
        List<InvoiceProductDto> dtoList = Arrays.asList(
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct(),
                TestDocumentInitializer.getInvoiceProduct()
        );
        dtoList.get(0).getInvoice().setInvoiceType(InvoiceType.PURCHASE);
        dtoList.get(1).getInvoice().setInvoiceType(InvoiceType.PURCHASE);
        dtoList.get(2).getInvoice().setInvoiceType(InvoiceType.PURCHASE);
        List<InvoiceProduct> invoiceProducts = dtoList.stream()
                .map(invoiceProductDto -> mapperUtil.convert(invoiceProductDto, new InvoiceProduct()))
                .collect(Collectors.toList());
        // when
        when(invoiceProductRepository.findAllByInvoice_Id(1L)).thenReturn(invoiceProducts);

        invoiceProductService.completeApprovalProcedures(1L, InvoiceType.PURCHASE);

        // then
        verify(invoiceProductRepository, times(invoiceProducts.size())).save(any());
        verify(productService, times(invoiceProducts.size())).update(any(), any());
        invoiceProducts.forEach(
                invoiceProduct -> assertEquals(10, invoiceProduct.getRemainingQuantity())
        );

        // increase of quantity in stock cannot be tracked since it is done inside another method and object changes and java is pass-by-value
//        dtoList.forEach(
//                dto -> assertEquals(20, dto.getProduct().getQuantityInStock())
//        );
    }

//        @Test
//        void setProfitLossOfInvoiceProductsForSalesInvoice(){
//            // given
//            List<InvoiceProductDto> dtoList = Arrays.asList(
//                    TestDocumentInitializer.getInvoiceProduct(),
//                    TestDocumentInitializer.getInvoiceProduct(),
//                    TestDocumentInitializer.getInvoiceProduct()
//            );
//            dtoList.get(0).getInvoice().setInvoiceType(InvoiceType.PURCHASE);
//            dtoList.get(1).getInvoice().setInvoiceType(InvoiceType.PURCHASE);
//            dtoList.get(2).getInvoice().setInvoiceType(InvoiceType.PURCHASE);
//            List<InvoiceProduct> invoiceProducts = dtoList.stream()
//                    .map(invoiceProductDto -> mapperUtil.convert(invoiceProductDto, new InvoiceProduct()))
//                    .collect(Collectors.toList());
//            // when
//            when(invoiceProductRepository.findAllByInvoice_Id(1L)).thenReturn(invoiceProducts);
//
//            invoiceProductService.set
//        }





}
