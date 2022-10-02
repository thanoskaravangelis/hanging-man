package com.multi_project.hanging_man.screens;

import com.multi_project.hanging_man.SplashScreen;
import com.multi_project.hanging_man.controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreen {

    private SplashScreen splashScreen;
    private MainController mainController;
    private String activeDictId;
    private Scene scene;
    public MainScreen(SplashScreen splashScreen) throws IOException {
        this.splashScreen = splashScreen;
        FXMLLoader fxmlLoader = new FXMLLoader(SplashScreen.class.getResource("main-screen.fxml"));
        this.scene = new Scene(fxmlLoader.load(), 1300, 1000);
        this.mainController = fxmlLoader.getController();
        mainController.setSplashScreen(splashScreen);
        mainController.setMainScreen(splashScreen.getSplashController().getMainScreen());
        mainController.setCreateDictPopUp(splashScreen.getSplashController().getCreateDictPopUp());
        mainController.setLoadDictPopUp(splashScreen.getSplashController().getLoadDictPopUp());
    }

    public void start(Stage stage, String id) throws Exception {
        this.activeDictId = id;

        stage.setTitle("MediaLab Hangman");
        stage.setScene(this.scene);
        stage.show();
        mainController.updateGrid(id);
        mainController.updateImageAndChancesText();
    }

    public String getActiveDictId() {
        return activeDictId;
    }

    public void setActiveDictId(String activeDictId) {
        this.activeDictId = activeDictId;
    }

    public SplashScreen getSplashScreen() {
        return splashScreen;
    }

    public void setSplashScreen(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
