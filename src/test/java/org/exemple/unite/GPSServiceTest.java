package org.exemple.unite;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.exemple.demo.App;
import org.junit.jupiter.api.Test;

public class GPSServiceTest {

    @Test
    public void testCoordonate() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            App app = new App();
            app.showCoords();
            
            String output = outContent.toString();
            assertTrue(output.contains("Longitude"));
            assertTrue(output.contains("Latitude"));
        } finally {
            System.setOut(originalOut);
        }
    }
}
