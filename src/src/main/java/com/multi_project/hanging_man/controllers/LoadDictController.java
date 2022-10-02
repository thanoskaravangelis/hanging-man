package com.multi_project.hanging_man.controllers;


import com.multi_project.hanging_man.SplashScreen;
import com.multi_project.hanging_man.utils.Directory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoadDictController implements Initializable {
    private SplashScreen splashScreen;

    public SplashScreen getSplashScreen() {
        return splashScreen;
    }

    public void setSplashScreen(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
    }

    @FXML
    private TextField dictId;

    @FXML
    private ChoiceBox dictDropdown;

    @FXML
    private TextArea text_alarm_load;

    protected void initialize() {
        List<String> allDicts = Directory.getDictionaries();
        for (int i = 0 ; i < allDicts.size(); i++) {
            dictDropdown.getItems().add(allDicts.get(i));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> allDicts = Directory.getDictionaries();
        int idOfDict = 0;
        for (int i = 0 ; i < allDicts.size(); i++) {
            idOfDict = Directory.getIdOfDict(allDicts.get(i));
            dictDropdown.getItems().add(idOfDict +": "+ allDicts.get(i).replace("_"," ").replaceAll("[0-9]", ""));
        }
    }

    @FXML
    protected void loadDict() throws Exception {
        String id = dictId.getText();
        String fileName = Directory.getFilenameWithId(Integer.parseInt(id));
        List<String> words = Directory.getWordsOfDict(fileName);
        this.splashScreen.getGame().addDict(id, (ArrayList<String>) words);
        System.out.println("Added words: " + this.splashScreen.getGame().getWords().toString() +" to Game.");
        this.text_alarm_load.setVisible(true);
        this.text_alarm_load.setText("Dictionary loaded successfully!");
        this.text_alarm_load.setStyle("-fx-control-inner-background: #000000; -fx-text-fill: green;");
        this.splashScreen.getGame().newRound();
        this.splashScreen.getSplashController().updateGrid(id);
        this.splashScreen.getSplashController().getMainScreen().getMainController().updateGrid(id);
        this.splashScreen.getSplashController().getMainScreen().getMainController().updateImageAndChancesText();
        this.splashScreen.getSplashController().getMainScreen().getMainController().updateWord();
    }
}
