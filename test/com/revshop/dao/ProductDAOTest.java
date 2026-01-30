package com.revshop.dao;

import static org.junit.Assert.*;
import org.junit.*;
import com.revshop.dao.ProductDAO;
import com.revshop.model.Product;

public class ProductDAOTest {

    private ProductDAO dao;

    @Before
    public void setUp() {
        dao = new ProductDAO();
    }

    @Test
    public void testAddAndGetProduct() {
        Product p = new Product();
        p.setSellerId(1);
        p.setName("JUnit Test Prod");
        p.setDescription("desc");
        p.setCategory("Test");
        p.setPrice(100);
        p.setDiscountPrice(90);
        p.setStock(10);
        p.setStockThreshold(2);

        int id = dao.addProduct(p);            // receive generated ID
        assertTrue("Product should be added", id > 0);

        Product fetched = dao.getProductById(id);
        assertNotNull("Product should exist", fetched);
        assertEquals("Name should match", "JUnit Test Prod", fetched.getName());
    }

    @Test
    public void testDeleteProduct() {
        Product p = new Product();
        p.setSellerId(1);
        p.setName("Delete Test Prod");
        p.setDescription("desc");
        p.setCategory("Test");
        p.setPrice(50);
        p.setDiscountPrice(45);
        p.setStock(5);
        p.setStockThreshold(1);

        int id = dao.addProduct(p);
        assertTrue("Insert should work", id > 0);

        assertTrue("Delete should return true", dao.deleteProduct(id));
        assertNull("Deleted product should not exist", dao.getProductById(id));
    }

    @Test
    public void testProductsByCategory() {
        java.util.List<Product> list = dao.getProductsByCategory("Test");
        assertNotNull("List should not be null", list);
    }
}
