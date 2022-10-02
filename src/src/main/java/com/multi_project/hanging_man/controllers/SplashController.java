package com.multi_project.hanging_man.controllers;

import com.multi_project.hanging_man.SplashScreen;
import com.multi_project.hanging_man.screens.CreateDictPopUp;
import com.multi_project.hanging_man.screens.LoadDictPopUp;
import com.multi_project.hanging_man.screens.MainScreen;
import com.multi_project.hanging_man.utils.Directory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class SplashController {
    private MainScreen mainScreen;
    private CreateDictPopUp createDictPopUp;
    private LoadDictPopUp loadDictPopUp;
    private SplashScreen splashScreen;

    public CreateDictPopUp getCreateDictPopUp() {
        return createDictPopUp;
    }

    public LoadDictPopUp getLoadDictPopUp() {
        return loadDictPopUp;
    }

    public MainScreen getMainScreen() {
        return mainScreen;
    }

    public SplashScreen getSplashScreen() {
        return splashScreen;
    }

    public void setSplashScreen(SplashScreen splashScreen) {
        this.splashScreen = splashScreen;
    }

    public void setCreateDictPopUp(CreateDictPopUp createDictPopUp) {
        this.createDictPopUp = createDictPopUp;
    }

    public void setLoadDictPopUp(LoadDictPopUp loadDictPopUp) {
        this.loadDictPopUp = loadDictPopUp;
    }

    public void setMainScreen(MainScreen mainScreen) {
        this.mainScreen = mainScreen;
    }

    @FXML
    private Button playBtn;

    @FXML
    private TextArea score, words_text, dictionary_text;

    @FXML
    protected void btnToMain() throws Exception{
        Stage stage = (Stage) playBtn.getScene().getWindow();
        mainScreen.start(stage, this.splashScreen.getActiveDictId());
    }

    @FXML
    protected void btnToMenuCreate() throws IOException{

        try {
            createDictPopUp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void btnToMenuLoad() throws IOException{
        try {
            loadDictPopUp.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void closeWindow() throws Exception{
        Stage stage = (Stage) playBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void updateGrid(String id){
        this.splashScreen.setActiveDictId(id);
        int idx = this.splashScreen.getGame().getLoaded_dicts_ids().indexOf(id);
        if(idx == -1) {
            this.dictionary_text.setText("Not Found");
        }
        else {
            this.dictionary_text.setText(Directory.getFilenameWithId(Integer.parseInt(this.splashScreen.getGame().getLoaded_dicts_ids().get(idx))).replace("_", " ").replaceAll("[0-9]", ""));
        }
        this.words_text.setText(String.valueOf(this.splashScreen.getGame().getWords().size()));
        this.score.setText(String.valueOf(this.splashScreen.getGame().getPoints()));
        String style = "-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00;";
        this.dictionary_text.setStyle(style);
        this.words_text.setStyle(style);
        this.score.setStyle(style);
    }
}