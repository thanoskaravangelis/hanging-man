package com.multi_project.hanging_man;

import com.multi_project.hanging_man.controllers.SplashController;
import com.multi_project.hanging_man.screens.CreateDictPopUp;
import com.multi_project.hanging_man.screens.LoadDictPopUp;
import com.multi_project.hanging_man.screens.MainScreen;
import com.multi_project.hanging_man.utils.MyApi;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class SplashScreen extends Application {
    private Game game;
    private SplashController splashController;
    private String activeDictId;
    private int successTries = 0, totalTries = 0;

    @Override
    public void start(Stage stage) throws IOException {

        String[] args = new String[]{};
        MyApi.main(args);

        this.game = new Game(new ArrayList<String>());

        FXMLLoader fxmlLoader = new FXMLLoader(SplashScreen.class.getResource("splash-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);

        this.splashController = fxmlLoader.getController();
        CreateDictPopUp createDictPopUp = new CreateDictPopUp(this);
        LoadDictPopUp loadDictPopUp = new LoadDictPopUp(this);
        splashController.setCreateDictPopUp(createDictPopUp);
        splashController.setLoadDictPopUp(loadDictPopUp);

        MainScreen mainScreen = new MainScreen(this);
        splashController.setMainScreen(mainScreen);
        splashController.setSplashScreen(this);

        stage.setTitle("MediaLab Hangman");
        stage.setScene(scene);
        stage.show();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getActiveDictId() {
        return activeDictId;
    }

    public void setActiveDictId(String activeDictId) {
        this.activeDictId = activeDictId;
    }

    public SplashController getSplashController() {
        return splashController;
    }

    public void setSplashController(SplashController splashController) {
        this.splashController = splashController;
    }

    public int getSuccessTries() {
        return successTries;
    }

    public void addSuccessTries() {
        this.successTries++;
    }

    public void addTotalTries() {
        this.totalTries++;
    }

    public int getTotalTries() {
        return totalTries;
    }

    public void setSuccessTries(int successTries) {
        this.successTries = successTries;
    }

    public void setTotalTries(int totalTries) {
        this.totalTries = totalTries;
    }

    public static void main(String[] args) {
        launch();
    }
}