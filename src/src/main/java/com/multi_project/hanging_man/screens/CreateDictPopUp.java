package com.multi_project.hanging_man.screens;

import com.multi_project.hanging_man.SplashScreen;
import com.multi_project.hanging_man.controllers.CreateDictController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateDictPopUp {
    private Stage popup = new Stage();
    private SplashScreen splashScreen;

    public CreateDictPopUp(SplashScreen splashScreen){
        this.splashScreen = splashScreen;
    }

    public void start() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SplashScreen.class.getResource("create-dict-screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        CreateDictController controller = fxmlLoader.getController();
        controller.setSplashScreen(this.splashScreen);
        this.popup.initModality(Modality.APPLICATION_MODAL);
        this.popup.setTitle("MediaLab Hangman");
        this.popup.setScene(scene);
        this.popup.show();
    }

    public Stage getPopup(){
        return this.popup;
    }
}
