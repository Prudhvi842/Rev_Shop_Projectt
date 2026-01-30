package com.revshop.dao;

import static org.junit.Assert.*;

import org.junit.*;

import com.revshop.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductDAOTest {

    private ProductDAO dao;
    private List<Integer> tempIds;  // to track inserted products

    @Before
    public void setUp() {
        dao = new ProductDAO();
        tempIds = new ArrayList<Integer>();
    }

    @After
    public void tearDown() {
        // Clean up all products created during tests
        for (Integer id : tempIds) {
            dao.deleteProduct(id);
        }
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

        int id = dao.addProduct(p);  // receive generated ID
        assertTrue("Product should be added", id > 0);

        // track for cleanup
        tempIds.add(id);

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

        // track so it gets cleaned up if delete fails
        tempIds.add(id);

        assertTrue("Delete should return true", dao.deleteProduct(id));
        assertNull("Deleted product should not exist", dao.getProductById(id));

        // remove since it's already deleted
        tempIds.remove((Integer) id);
    }

    @Test
    public void testProductsByCategory() {
        // create a product in this category
        Product p = new Product();
        p.setSellerId(1);
        p.setName("Cat Test Prod");
        p.setDescription("desc");
        p.setCategory("TempCat123");
        p.setPrice(20);
        p.setDiscountPrice(18);
        p.setStock(5);
        p.setStockThreshold(1);

        int id = dao.addProduct(p);
        assertTrue("Insert should work", id > 0);
        tempIds.add(id);

        java.util.List<Product> list = dao.getProductsByCategory("TempCat123");
        assertNotNull("List should not be null", list);
        assertTrue("Should retrieve at least one product", list.size() > 0);
    }
}
