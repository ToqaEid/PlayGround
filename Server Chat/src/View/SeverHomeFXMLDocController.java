/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Controller;
import DataTransferObject.DatabaseHandler;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.imageio.ImageIO;

/**
 *
 * @author Israa
 */
public class SeverHomeFXMLDocController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button userAvailability;
    @FXML
    private Button genderStatisticsBtn;
    @FXML
    private BorderPane rootPane;
    double numberOfOnlineUsers;
    double numberOfOfflineUsers;
    double numberOfUsers;
    NumberAxis yAxis;
    
    Controller control = new Controller();
    SimpleIntegerProperty maleProperty = new SimpleIntegerProperty(this, "Male");
    SimpleIntegerProperty femaleProperty = new SimpleIntegerProperty(this, "Female");
    private View view;

    public void intializeViewRef(View v) {
        this.view = v;
    }

    @FXML
    private void handleUserAvailabilityButton() {
      
        numberOfUsers = control.getAllUsersNum();

        final String online = "Online";
        final String offline = "Offline";
        CategoryAxis xAxis = new CategoryAxis();
        yAxis = new NumberAxis(0, numberOfUsers, 1);
        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        barChart.setTitle("Users Availability Statistics");
        xAxis.setLabel("Status");
        yAxis.setLabel("Number of Users");
        XYChart.Series series = new XYChart.Series();
        series.setName("Live Statistic");
        numberOfOnlineUsers = control.getOnlines();
        numberOfOfflineUsers = control.getOnlines();
        series.getData().add(new XYChart.Data(online, numberOfOnlineUsers));
        series.getData().add(new XYChart.Data(offline, numberOfOfflineUsers));

        Timeline timeLine = new Timeline();
        timeLine.getKeyFrames().add(
                new KeyFrame(Duration.millis(500),
                        new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        numberOfOnlineUsers = control.getOnlines();
                        //System.out.println("in thread");
                        numberOfOfflineUsers = control.getOfflines();

                        numberOfUsers =control.getAllUsersNum();
                        yAxis.setUpperBound(numberOfUsers);

                        for (XYChart.Series<String, Number> series : barChart.getData()) {
                            for (XYChart.Data<String, Number> data : series.getData()) {

                                if (data.getXValue() == online) {
                                    data.setYValue(numberOfOnlineUsers);
                                }
                                if (data.getXValue() == offline) {
                                    data.setYValue(numberOfOfflineUsers);
                                }
                            }
                        }
                    }
                }
                ));
        barChart.getData().add(series);
        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();
        rootPane.setCenter(barChart);

    }

    double numberOFMaleUsers;
    double numberOfFemaleUsers;
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    @FXML
    private void handleGenderStatisticsBtn() {
//        SimpleIntegerProperty maleProperty = new SimpleIntegerProperty(this, "Male");
//        SimpleIntegerProperty femaleProperty = new SimpleIntegerProperty(this, "Female");
        PieChart.Data maleData = new PieChart.Data(maleProperty.getName(), 0);
        PieChart.Data femaleData = new PieChart.Data(femaleProperty.getName(), 0);

        maleData.pieValueProperty().bind(maleProperty);
        femaleData.pieValueProperty().bind(femaleProperty);
        maleProperty.set(control.getMales());
        femaleProperty.set(control.getFemales());
        ObservableList<PieChart.Data> sourceData = FXCollections.observableArrayList(femaleData, maleData);
        PieChart pieChart = new PieChart(sourceData);

        rootPane.setCenter(pieChart);
        /////////////////////////////////


        /*

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    numberOfFemaleUsers = db.getFemaleUsers();
                    numberOFMaleUsers = db.getMaleUsers();
                    addData(male, numberOFMaleUsers);
                    addData(female, numberOfFemaleUsers);
                }

            }
        }).start();

        addData(male, numberOFMaleUsers);
        addData(female, numberOfFemaleUsers);

        pieChart.setData(pieChartData);
        rootPane.setCenter(pieChart);*/
    }

    public void updateGenderProperties() {
        //GenderStatisticsBean genderBean = new GenderStatisticsBean();
        maleProperty.set(control.getMales());
        femaleProperty.set(control.getFemales());

    }

    @FXML
    private void stopServerButton(ActionEvent event) {
        System.out.println("Checking out server services ... ");

        try {

            //new Controller().stopServer();
            ((Node) (event.getSource())).getScene().getWindow().hide();

            //Parent parent =  FXMLLoader.load(getClass().getResource("/View/serverStartFXML.fxml"));
            FXMLLoader fxml = new FXMLLoader();

            Parent parent = fxml.load((getClass().getResource("/View/serverStartFXML.fxml")).openStream());

            serverStartFXMLController startFXMLController
                    = fxml.getController();

            startFXMLController.setControllerRef(view);

            /////////////////////
            if (view == null) {
                System.out.println("View is NULL in HomeController");
            }

            view.stopServer();

            ////////////////////
            Stage stage = new Stage();
            stage.getIcons().add(new Image("/img/logo7.png"));
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex) {
            System.out.println("ERROR:: " + ex.toString());
        }

    }

    @FXML
    private void handleAnnounceButtonAction(ActionEvent event) throws IOException {
            FXMLLoader fxml = new FXMLLoader();
            Parent parent = fxml.load((getClass().getResource("/View/Announcement.fxml")).openStream());
            AnnouncementController announcementController= fxml.getController();
            announcementController.intializeViewRef(view);
            rootPane.setCenter(parent);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
