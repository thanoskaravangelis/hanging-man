package com.multi_project.hanging_man.controllers;

import com.multi_project.hanging_man.SplashScreen;
import com.multi_project.hanging_man.utils.Directory;
import com.multi_project.hanging_man.utils.MyApi;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class CreateDictController {

    @FXML
    private TextField dictId, bookId;

    private SplashScreen splashScreen;

    public SplashScreen getSplashScreen() {
        return splashScreen;
    }

    public void setSplashScreen(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
    }

    @FXML
    private TextArea text_alarm_load;

    @FXML
    protected void createDict() throws Exception {
        String id = dictId.getText();
        Integer intId = Integer.parseInt(id);
        int newIntId = intId;
        String UrlToFetch = "https://openlibrary.org/works/"+bookId.getText()+".json";
        if (intId < Directory.getMaxId()) {
            newIntId = Directory.getMaxId()+1;
            MyApi.createDictionaryAndCheckAll(UrlToFetch, newIntId, -1);
        }
        else {
            MyApi.createDictionaryAndCheckAll(UrlToFetch, newIntId, intId);
        }
        String fileName = Directory.getFilenameWithId(newIntId);
        List<String> words = Directory.getWordsOfDict(fileName);
        this.getSplashScreen().getGame().addDict(id, (ArrayList<String>) words);
        System.out.println("Added words: " + this.splashScreen.getGame().getWords().toString() +" to Game.");
        this.text_alarm_load.setVisible(true);
        this.text_alarm_load.setText("Created dictionary with id: "+newIntId);
        this.text_alarm_load.setStyle("-fx-control-inner-background: #000000; -fx-text-fill: green;");
        this.splashScreen.getGame().newRound();
        this.splashScreen.getSplashController().updateGrid(String.valueOf(newIntId));
        this.splashScreen.getSplashController().getMainScreen().getMainController().updateGrid(String.valueOf(newIntId));
        this.splashScreen.getSplashController().getMainScreen().getMainController().updateImageAndChancesText();
        this.splashScreen.getSplashController().getMainScreen().getMainController().updateWord();
    }
}
