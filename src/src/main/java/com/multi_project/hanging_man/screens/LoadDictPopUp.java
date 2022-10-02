package com.multi_project.hanging_man.screens;

import com.multi_project.hanging_man.SplashScreen;
import com.multi_project.hanging_man.controllers.LoadDictController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadDictPopUp {
    private Stage popup = new Stage();
    private SplashScreen splashScreen;

    public LoadDictPopUp(SplashScreen splashScreen){
        this.splashScreen = splashScreen;
    }

    public void start() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SplashScreen.class.getResource("load-dict-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        LoadDictController controller = fxmlLoader.getController();
        controller.setSplashScreen(this.splashScreen);
        this.popup.initModality(Modality.APPLICATION_MODAL);
        this.popup.setTitle("MediaLab Hangman");
        this.popup.setScene(scene);
        this.popup.show();
    }

    public Stage getPopup() {
        return this.popup;
    }
}
