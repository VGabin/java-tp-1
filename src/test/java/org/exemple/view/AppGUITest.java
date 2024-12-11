package org.exemple.view;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.exemple.demo.App;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.stage.Stage;

public class AppGUITest extends ApplicationTest {

    private App app;

    @AfterAll
    public static void deleteTestFiles() {
        File audioFile = new File("audio.wav");
        File pictureFile = new File("photo.png");

        if (audioFile.exists()) {
            if (audioFile.delete()) {
                System.out.println("Le fichier a été supprimé avec succès.");
            } else {
                System.out.println("Échec de la suppression du fichier.");
            }
        }

        if (pictureFile.exists()) {
            if (pictureFile.delete()) {
                System.out.println("Le fichier a été supprimé avec succès.");
            } else {
                System.out.println("Échec de la suppression du fichier.");
            }
        }
    }

    @Override
    public void start(Stage stage) {
        app = new App();
        app.start(stage);
    }

    @Test
    public void startRecording() {
        File audioFile = new File("audio.wav");

        clickOn("Commencer l'enregistrement");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        clickOn("Arrêter l'enregistrement");

        assertTrue(audioFile.exists(), "Le fichier audio.wav devrait être créé après le clic sur le bouton.");

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void captureImage() {
        clickOn("Prendre une photo");

        File picturFile = new File("photo.png");

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(picturFile.exists(), "Le fichier photo.png devrait être créé après le clic sur le bouton.");
    }
}