package org.exemple.unite;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.exemple.demo.App;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class WebcamServiceTest {
    @AfterAll
    public static void deleteTestFiles() {
        File pictureFile = new File("test.png");

        if (pictureFile.exists()) {
            if (pictureFile.delete()) {
                System.out.println("Le fichier a été supprimé avec succès.");
            } else {
                System.out.println("Échec de la suppression du fichier.");
            }
        }
    }

    @Test
    public void testTakePhoto() {
        File pictureFile = new File("test.png");

        App app = new App();
        app.takePhoto("test");
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertTrue(pictureFile.exists());
    }
}
