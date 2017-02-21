/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author toqae
 */
public class AnnouncementController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    ImageView imageView;
    @FXML
    Button addImage;
    @FXML
    Button announce;
    @FXML
    Label errorAnnounce;

    private View view;

    public void intializeViewRef(View v) {
        this.view = v;
    }

    @FXML
    private void handleAnnounceButtonAction(ActionEvent event) throws IOException {

        //Broadcasting image
        ///////////////////////Israa handling//////////////////
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (imageView.getImage() != null) {
                    ///////////////////////Israa handling//////////////////
                    // I already have a bitmap

                    BufferedImage bImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    try {
                        ImageIO.write(bImage, "jpg", out);
                        byte[] data = out.toByteArray();
                        System.out.println("View.SeverHomeFXMLDocController.handleAnnounceButtonAction()");
                        view.startAnnounceView(data);
                        out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(AnnouncementController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    ///////////////////////Israa handling//////////////////
                } else {
                    System.out.println("Nooo image!!!");
                    errorAnnounce.setText("No Image Selected");
                }
            }
        });

        ///////////////////////Israa handling//////////////////
    }

    @FXML
    public void addImageHandler(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("image files (*.jpg)", "*.jpg", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        Node source = (Node) e.getSource();
        File choosenFile = fileChooser.showOpenDialog(source.getScene().getWindow());
        if (choosenFile != null) {
            System.out.println(choosenFile.getPath());
            //imageView.fitWidthProperty().bind(source.getScene().widthProperty());
            Image image = new Image("file:///" + choosenFile.getPath(), 400, 300, false, false);
            imageView.setImage(image);

        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
