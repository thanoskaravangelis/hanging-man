package com.multi_project.hanging_man.controllers;

import com.multi_project.hanging_man.SplashScreen;
import com.multi_project.hanging_man.screens.CreateDictPopUp;
import com.multi_project.hanging_man.screens.LoadDictPopUp;
import com.multi_project.hanging_man.screens.MainScreen;
import com.multi_project.hanging_man.utils.Directory;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    public TextArea alert_txt;

    private MainScreen mainScreen;
    private CreateDictPopUp createDictPopUp;
    private LoadDictPopUp loadDictPopUp;

    @FXML
    TextArea dict_label_text;

    @FXML
    private TextArea score, words_text, words_left, dictionary_text, hidden_word;

    @FXML
    private ImageView image_view_hang;

    @FXML
    private TextArea chances_text;

    @FXML
    private HBox hbox_word_chars;

    @FXML
    private VBox vbox_available_chars;

    @FXML
    private TextField insert_letter;

    @FXML
    private TextField insert_index;

    @FXML
    private Button insert_btn;

    @FXML
    private SplashScreen splashScreen;

    @FXML
    private List<TextArea> charsAt = new ArrayList<>();

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
    public void updateImageAndChancesText() throws Exception {
        int chances = this.splashScreen.getGame().getChances_remaining();
        this.chances_text.setText(chances + " chances remaining");
        int photo_idx;
        if (chances == 0 ) {
            photo_idx = 6;
        }
        else {
            photo_idx = 7 - chances;
        }
        FileInputStream fileStream = new FileInputStream("src/main/resources/images/" + (photo_idx) + ".jpg");
        Image image = new Image(fileStream);
        this.image_view_hang.setImage(image);
    }

    @FXML
    protected void start() throws Exception {
        String id = this.getSplashScreen().getActiveDictId();
        clearWord();
        this.getSplashScreen().setTotalTries(0);
        this.getSplashScreen().setSuccessTries(0);
        this.getSplashScreen().getGame().newRound();
        this.splashScreen.getSplashController().updateGrid(id);
        this.splashScreen.getSplashController().getMainScreen().getMainController().updateGrid(id);
        this.splashScreen.getSplashController().getMainScreen().getMainController().updateImageAndChancesText();
        this.splashScreen.getSplashController().getMainScreen().getMainController().updateWord();
    }

    @FXML
    protected void btnToMenuCreate() throws IOException {

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
        Stage stage = (Stage) dictionary_text.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void solutionShow() throws Exception{
        this.getSplashScreen().getGame().setChances_remaining(0);
        updateGrid(this.getSplashScreen().getActiveDictId());
        updateImageAndChancesText();
        String loss = "LOSS..";
        for (int i = 0 ; i < 6; i++) {
            charsAt.get(i).setText(String.valueOf(loss.charAt(i)));
            charsAt.get(i).setStyle("-fx-text-fill: red;");
        }
        if (charsAt.size() > 6) {
            for (int i = 0; i < charsAt.size(); i++){
                charsAt.get(i).setText(String.valueOf('.'));
                charsAt.get(i).setStyle("-fx-text-fill: #ffffff;");
            }
        }
        this.hidden_word.setText(this.getSplashScreen().getGame().getWord());
        this.insert_btn.setDisable(true);
        alert_txt.setVisible(false);
        return;
    }

    @FXML
    public void updateGrid(String id){
        int idx = this.getSplashScreen().getGame().getLoaded_dicts_ids().indexOf(id);
        if(idx == -1) {
            this.dictionary_text.setText("Not Found");
        }
        else {
            this.dictionary_text.setText(Directory.getFilenameWithId(Integer.parseInt(this.splashScreen.getGame().getLoaded_dicts_ids().get(idx))).replace("_", " ").replaceAll("[0-9]", ""));
        }
        this.words_text.setText(String.valueOf(this.getSplashScreen().getGame().getWords().size()));
        this.score.setText(String.valueOf(this.getSplashScreen().getGame().getPoints()));
        if(this.getSplashScreen().getTotalTries() == 0){
            this.words_left.setText("0%");
        }
        else {
            this.words_left.setText(((this.getSplashScreen().getSuccessTries() / (double) this.getSplashScreen().getTotalTries())*100) + "%");
        }
        this.hidden_word.setText("?");

        String style = "-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00;";
        this.dictionary_text.setStyle(style);
        this.words_text.setStyle(style);
        this.score.setStyle(style);
        this.words_left.setStyle(style);
        this.hidden_word.setStyle(style);
    }

    @FXML
    public void updateWord() {
        int idx = this.splashScreen.getGame().getLoaded_dicts_ids().indexOf(this.getSplashScreen().getActiveDictId());
        if (idx == -1) {
            return;
        }
        String style1 = "-fx-control-inner-background:#ffffff; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00;";
        String style = "-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: #00ff00; -fx-highlight-text-fill: #000000; -fx-text-fill: #00ff00;";
        String word = this.splashScreen.getGame().getWord();
        char[] wordarr = word.toCharArray();
        int counter = 1;
        hbox_word_chars.getChildren().clear();

        for (char i : wordarr) {
            VBox vbox = new VBox();

            StackPane stackPane = new StackPane();
            TextArea textArea = new TextArea();
            textArea.setText("_");
            textArea.setFont(new Font(18));
            textArea.setPrefHeight(23);
            textArea.setPrefWidth(10);
            textArea.setStyle(style1);
            textArea.setEditable(false);
            textArea.setId("char_at_index" + counter);
            this.charsAt.add(textArea);
            stackPane.getChildren().add(textArea);
            stackPane.setStyle("-fx-padding: 5px");
            StackPane.setMargin(stackPane, new Insets(15, 2, 15, 2));

            StackPane stackPane1 = new StackPane();
            TextArea textArea1 = new TextArea();
            textArea1.setText(String.valueOf(counter));
            counter++;
            textArea1.setFont(new Font(18));
            textArea1.setPrefHeight(23);
            textArea1.setPrefWidth(10);
            textArea1.setStyle(style);
            textArea1.setEditable(false);
            stackPane1.getChildren().add(textArea1);
            stackPane1.setStyle("-fx-padding: 5px");
            StackPane.setMargin(stackPane1, new Insets(15, 2, 15, 2));

            vbox.getChildren().add(stackPane);
            vbox.getChildren().add(stackPane1);

            hbox_word_chars.getChildren().add(vbox);
        }
    }

    @FXML
    protected void clearWord() {
        this.charsAt.clear();
        hbox_word_chars.getChildren().clear();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null || strNum=="" || strNum==" ") {
            return false;
        }
        try {
            Integer d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @FXML
    protected Boolean checkForWin() {
        String myWord = this.getSplashScreen().getGame().getWord();
        char[] wordarr = myWord.toCharArray();
        int index = 0;
        for (char c : wordarr){
            if(c != charsAt.get(index).getText().charAt(0)){
                return false;
            }
            index++;
        }
        String win = "YOUWON";
        for (int i = 0 ; i < 6; i++) {
            charsAt.get(i).setText(String.valueOf(win.charAt(i)));
            charsAt.get(i).setStyle("-fx-text-fill: red;");
        }
        if (charsAt.size() > 6) {
            for (int i = 0; i < charsAt.size(); i++){
                charsAt.get(i).setText(String.valueOf('!'));
                charsAt.get(i).setStyle("-fx-text-fill: #ffffff;");
            }
        }
        this.insert_btn.setDisable(true);
        return true;
    }

    @FXML
    protected void insertLetter() throws Exception {
        String style = "-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: red; -fx-highlight-text-fill: #ffffff; -fx-text-fill: red;";
        String text = insert_index.getText();
        if (isNumeric(text)) {
            Integer index = Integer.parseInt(text);
            String letter = insert_letter.getText().toUpperCase();
            if(index < 1 || index > this.getSplashScreen().getGame().getWord().length()){
                return;
            }
            if( !this.charsAt.get(index-1).getText().equals("_")) {
                alert_txt.setVisible(true);
                alert_txt.setStyle(style);
                alert_txt.setText("Try another index.");
                return;
            }
            if( Character.toString( this.getSplashScreen().getGame().getWord().toCharArray()[index-1]).equals(letter)) {
                this.charsAt.get(index - 1).setText(letter);
                int old_points = this.getSplashScreen().getGame().getPoints();
                int new_points = old_points + 30;
                this.getSplashScreen().getGame().setPoints(new_points);
                score.setText(String.valueOf(new_points));
                this.getSplashScreen().addSuccessTries();
                this.getSplashScreen().addTotalTries();
                updateGrid(this.getSplashScreen().getActiveDictId());
                alert_txt.setVisible(false);
                checkForWin();
                return;
            }
            else {
                this.getSplashScreen().addTotalTries();
                int new_chances = this.getSplashScreen().getGame().getChances_remaining() - 1;
                this.getSplashScreen().getGame().setChances_remaining(new_chances);
                int old_points = this.getSplashScreen().getGame().getPoints();
                int new_points = old_points - 15;
                if(new_points < 0){new_points = 0;}
                this.getSplashScreen().getGame().setPoints(new_points);
                updateGrid(this.getSplashScreen().getActiveDictId());
                updateImageAndChancesText();
                if (new_chances == 0){
                    String loss = "LOSS..";
                    for (int i = 0 ; i < 6; i++) {
                        charsAt.get(i).setText(String.valueOf(loss.charAt(i)));
                        charsAt.get(i).setStyle("-fx-text-fill: red;");
                    }
                    if (charsAt.size() > 6) {
                        for (int i = 6; i < charsAt.size(); i++){
                            charsAt.get(i).setText(String.valueOf('.'));
                            charsAt.get(i).setStyle("-fx-text-fill: red;");
                        }
                    }
                    this.hidden_word.setText(this.getSplashScreen().getGame().getWord());
                    this.insert_btn.setDisable(true);
                    alert_txt.setVisible(false);
                    return;
                }
                else {
                    alert_txt.setVisible(false);
                    updateGrid(this.getSplashScreen().getActiveDictId());
                    updateImageAndChancesText();
                    return;
                }

            }
        }
        else {
            alert_txt.setVisible(true);
            alert_txt.setStyle(style);
            alert_txt.setText("Insert an integer as index.");
            return;
        }
    }
}
