package View;

import Controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author samir
 */
public class View extends Application {

    Controller Controller;
    serverStartFXMLController startFXMLController;
    SeverHomeFXMLDocController homeFXMLDocController;

    public View() {
        this.Controller = new Controller(this);
    }

    public void intializeHomeRef(SeverHomeFXMLDocController homeFXMLDocController) {
        this.homeFXMLDocController = homeFXMLDocController;
    }

    void startServer() {
        Controller.startserver();
    }

    void stopServer() {
        Controller.stopServer();
    }

    public void startAnnounceView(byte[] image) {
        System.out.println("View.View.startAnnounceView()");
        System.out.println(Controller == null);
        Controller.startAnnounceController(image);
    }

    public void updatePieChart(){
        homeFXMLDocController.updateGenderProperties();
    }
    @Override
    public void start(Stage stage) throws Exception {

        // Parent root = FXMLLoader.load(getClass().getResource("serverStartFXML.fxml"));
        FXMLLoader fxml = new FXMLLoader();
        Parent root = fxml.load((getClass().getResource("serverStartFXML.fxml")).openStream());
        startFXMLController = fxml.getController();
        startFXMLController.setControllerRef(this);
        
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image("/img/logo7.png"));
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

}
