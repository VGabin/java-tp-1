package org.exemple.unite;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

import org.exemple.demo.App;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AudioServiceTest 
    extends TestCase
{
    @AfterAll
    public static void deleteTestFiles() {
        File audioFile = new File("test.wav");

        if (audioFile.exists()) {
            if (audioFile.delete()) {
                System.out.println("Le fichier a été supprimé avec succès.");
            } else {
                System.out.println("Échec de la suppression du fichier.");
            }
        }
    }

    @Test
    public void testStartRecording() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            App app = new App();
            app.startRecording("test");

            String output = outContent.toString();

            assertFalse(output.contains("Erreur lors de l'enregistrement"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testStopRecording() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            App app = new App();
            File audioFile = new File("test.wav");

            app.microphone =  (TargetDataLine) AudioSystem.getLine(info);
            app.stopRecording();

            String output = outContent.toString();
            assertTrue(output.contains("Arrêt de l'enregistrement"));

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            assertTrue(audioFile.exists());
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            System.setOut(originalOut);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
