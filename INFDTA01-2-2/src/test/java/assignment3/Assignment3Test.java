package assignment3;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Assignment3Test {

    static List<Double> data;

    @BeforeClass
    public static void setUp() throws IOException {
        data = Assignment3.getData();
    }

    @Test
    public void testData() throws IOException{
        Assignment3 demo = new Assignment3("Assignment 3 - Data science", "Forecasting chart");
        demo.pack();
        demo.setVisible(true);
    }

    @Test
    public void testDataset() {
        assertNotNull(data);
        assertTrue(data.size() > 10);
    }
}