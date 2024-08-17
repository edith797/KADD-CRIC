import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class VideoPlayerSwing {

    private static void initAndShowGUI() {
        // This method is invoked on the EDT thread
        JFrame frame = new JFrame("Video Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setLayout(new BorderLayout());

        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel, BorderLayout.CENTER);

        //frame.setSize(800, 450);
        //frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);

        Platform.runLater(() -> {
            // This method is invoked on the JavaFX thread
            Group root = new Group();
            Scene scene = new Scene(root);
            fxPanel.setScene(scene);

            // Path to your video file
            String videoFilePath = "cricvideo.mp4";

            Media media = new Media(new File(videoFilePath).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            // Fit the video to the scene size
            mediaView.setFitWidth(scene.getWidth());
            mediaView.setFitHeight(scene.getHeight());
            //System.out.println(scene.getWidth());
            //System.out.println(scene.getHeight());

            root.getChildren().add(mediaView);
            mediaPlayer.play();

            mediaPlayer.setOnEndOfMedia(() -> {
                // Transition to HomePageSwing after video finishes
                SwingUtilities.invokeLater(() -> {
                    frame.dispose(); // Close the video player frame
                    HomePageSwing homePage = new HomePageSwing();
                    homePage.setVisible(true); // Show the homepage
                });
            });
        });
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(VideoPlayerSwing::initAndShowGUI);
    }
}
