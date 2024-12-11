package org.exemple.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import com.github.sarxos.webcam.Webcam;
import javax.imageio.ImageIO;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpClient;
import org.json.*;

public class App extends Application {

    public TargetDataLine microphone;

    @Override
    public void start(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Button startRecording = new Button("Commencer l'enregistrement");
        Button stopRecording = new Button("Arrêter l'enregistrement");
        Button takePhoto = new Button("Prendre une photo");
        Button showCoords = new Button("Afficher les coordonnées");

        startRecording.setOnAction(e -> startRecording(null));
        stopRecording.setOnAction(e -> stopRecording());
        takePhoto.setOnAction(e -> takePhoto(null));
        showCoords.setOnAction(e -> showCoords());

        root.getChildren().addAll(startRecording, stopRecording, takePhoto, showCoords);

        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Ma première app java");
        stage.show();
    }

    public void startRecording(String fileName) {
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = "audio.wav";
            } else {
                fileName += ".wav";
            }
    
            File audioFile = new File(fileName);
    
            AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
    
            System.out.println("Début de l'enregistrement : " + fileName);
    
            // Thread pour enregistrer l'audio dans le fichier
            Thread thread = new Thread(() -> {
                try (AudioInputStream ais = new AudioInputStream(microphone)) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, audioFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            thread.start();
        } catch (LineUnavailableException ex) {
            System.out.println("Erreur lors de l'enregistrement");
            ex.printStackTrace();
        }
    }

    public void stopRecording() {
        if (microphone != null) {
            microphone.stop();
            microphone.close();

            System.out.println("Arrêt de l'enregistrement");
        }
    }

    public void takePhoto(String fileName) {
        Webcam webcam = Webcam.getDefault();
        webcam.open();
        try {
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = "photo.png";
            } else {
                fileName += ".png";
            }

            ImageIO.write(webcam.getImage(), "PNG", new File(fileName));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            webcam.close();
        }
    }

    public void showCoords() {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        JSONObject ipResponse = new JSONObject(apiCall("https://api.ipify.org/?format=json"));

        String api_key = "b584e812a47344d5bdf5838a72dae7fb";

        JSONObject coordonneesResponse = new JSONObject(apiCall("https://ipgeolocation.abstractapi.com/v1/?api_key=" + api_key + "&ip_address=" + ipResponse.getString("ip")));
        
        Coordinate coordinate = new Coordinate(coordonneesResponse.getFloat("latitude"),coordonneesResponse.getFloat("longitude"));
        System.out.println("Latitude : " + coordinate.x);
        System.out.println("Longitude : " + coordinate.y);
    }

    public String apiCall(String url) {
        try {
            URI newUrl = new URI(url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(newUrl)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

                    HttpResponse<String> response = null;
                    try {
                        response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            
                    return response.body();
        } catch (Exception e) {
            System.out.println("Erreur lors de la récuération des coordonnées");
            
            return "";
        }
	}

    public static void main(String[] args) {
        launch();
    }
}
