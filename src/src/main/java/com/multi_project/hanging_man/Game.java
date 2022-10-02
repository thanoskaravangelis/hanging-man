package com.multi_project.hanging_man;

import com.multi_project.hanging_man.exceptions.GameOverException;
import com.multi_project.hanging_man.exceptions.MyCharException;

import java.util.*;

public class Game {

    private Integer moves;
    private Integer points;
    private Integer chances_remaining;
    private Integer round;
    private Boolean win;
    private String word;
    private String prevWord;
    private Boolean playing;
    private Float prob;
    private ArrayList<ArrayList<String>> prevRounds;
    private List<String> words;
    private List<String> words_left;
    private List<Integer> shown_indexes;
    private ArrayList<String> loaded_dicts_ids;
    private Map<Integer, List<Character>> available_chars;

    private class CharPossibility implements  Comparable<CharPossibility> {
        private char c;
        private float p;

        public CharPossibility(char c, float p) {
            this.c = c;
            this.p = p;
        }

        public char getC() {
            return c;
        }
        public float getP() {
            return p;
        }

        // sorting in reverse
        @Override
        public int compareTo(CharPossibility position) {
            return (this.p >= position.getP()) ? -1 : 1;
        }
    }


    Game(List<String> words) {
        this.words = words;
        this.round = 0;
        this.moves = 0;
        this.points = 0;
        this.chances_remaining = 6;
        this.win = false;
        this.prevRounds = new ArrayList<ArrayList<String>>();
        this.loaded_dicts_ids = new ArrayList<String>();
        this.shown_indexes = new ArrayList<Integer>();
        this.words_left = new ArrayList<String>(words);
    }

    //Getters
    public List<Integer> getShown_indexes() {        return shown_indexes;    }
    public String getWord() {        return word;    }
    public Boolean getPlaying() {        return playing;    }
    public Boolean getWin() {        return win;    }
    public Float getProb() {        return prob;    }
    public Integer getChances_remaining() {        return chances_remaining;    }
    public Integer getMoves() {        return moves;    }
    public Integer getPoints() {        return points;    }
    public Integer getRound() {        return round;    }
    public ArrayList<ArrayList<String>> getPrevRounds() {        return prevRounds;    }
    public List<String> getWords() {        return words;    }
    public List<String> getWords_left() {        return words_left;    }
    public Map<Integer, List<Character>> getAvailable_chars() {        return available_chars;    }
    public String getPrevWord() {        return prevWord;    }
    public ArrayList<String> getLoaded_dicts_ids() {        return loaded_dicts_ids;    }

    //Setters

    public void setLoaded_dicts_ids(ArrayList<String> loaded_dicts_ids) {        this.loaded_dicts_ids = loaded_dicts_ids;    }
    public void setShown_indexes(List<Integer> shown_indexes) {        this.shown_indexes = shown_indexes;    }
    public void setWord(String word) {        this.word = word;    }
    public void setAvailable_chars(Map<Integer, List<Character>> available_chars) {        this.available_chars = available_chars;    }
    public void setChances_remaining(Integer chances_remaining) {        this.chances_remaining = chances_remaining;    }
    public void setMoves(Integer moves) {        this.moves = moves;    }
    public void setPlaying(Boolean playing) {        this.playing = playing;    }
    public void setPoints(Integer points) {        this.points = points;    }
    public void setPrevRounds(ArrayList<ArrayList<String>> prevRounds) {        this.prevRounds = prevRounds;    }
    public void setPrevWord(String prevWord) {        this.prevWord = prevWord;    }
    public void setProb(Float prob) {        this.prob = prob;    }
    public void setRound(Integer round) {        this.round = round;    }
    public void setWin(Boolean win) {        this.win = win;    }
    public void setWords(List<String> words) {        this.words = words;    }
    public void setWords_left(List<String> words_left) {   this.words_left = words_left;    }

    public void addDict(String dict_id, ArrayList<String> words) {
        this.loaded_dicts_ids.add(dict_id);
        Set<String> loaded_words = new HashSet<String>(this.words!=null ? this.words : new ArrayList<>());
        for (String word : words) {
            if (!loaded_words.contains(word)) {
                this.words.add(word);
            }
        }
        this.words_left = new ArrayList<>(this.words);
        //this.updateAvailableChars();
    }

    public void newRound() {
        this.prevWord=null;
        this.initRound(this.words);
        this.round++;
        this.playing = true;
    }

    private void pickWord() {
        Random ran = new Random();
        int index = ran.nextInt(this.words.size());
        String word = this.words.get(index);
        this.word = word;
        System.out.println("Picked word: " + this.word);
    }

    private boolean isWordOk(String word) {
        int length = this.word.length();
        if (word.length() != length) return false;
        // if (word.equals(this.word)) return false;
        for (int index: this.getShown_indexes()) {
            if (! (word.charAt(index) == this.word.charAt(index)) ) {
                return false;
            }
        }
        return true;
    }

    private void filterWordsBySizeAndLetters() {
        List<String> temp = new ArrayList<>(this.words_left);

        this.words_left = new ArrayList<String>();

        if (this.isWordOk(word)) {
            this.words_left.add(word);
        }
    }

    private void filterWordsByNewLetter(int index, char c, boolean afterCorrectGuess) {
        List<String> temp_words_left = new ArrayList<>(this.words_left);
        for (String word : this.words_left) {
            if (afterCorrectGuess && word.toUpperCase(Locale.ROOT).charAt(index)!=c) {
                temp_words_left.remove(word);
            }
            else if (!afterCorrectGuess && word.toUpperCase(Locale.ROOT).charAt(index)==c) {
                temp_words_left.remove(word);
            }
        }
        this.words_left = temp_words_left;
    }

    private void initRound(List<String> words) {
        this.words = words;
        this.shown_indexes = new ArrayList<Integer>();
        this.moves = 0;
        this.points = 0;
        this.chances_remaining = 6;
        this.win = false;

        if (this.available_chars == null) {
            System.out.println("Clearing available chars...");
            this.available_chars = new HashMap<Integer, List<Character>>();
        }
        if (!(this.words==null || this.words.isEmpty())) {
            this.pickWord();
            this.filterWordsBySizeAndLetters();
            this.words_left = new ArrayList<>(this.words);
            this.updateAvailableChars();
        }
        else {
            this.words_left = new ArrayList<String>();
        }

    }

    private void computeProb(int index, char c) {
        this.filterWordsBySizeAndLetters();
        int n = this.words_left.size();
        float m = 0;
        for (String word : this.words_left) {
            if (word.charAt(index) == c) {
                m++;
            }
        }
        this.prob = m/n;
    }

    private void correctGuess(int index, char c) {
        int extra_points = 0;
        if (this.prob>=0.6) {
            extra_points = 5;
        }
        else if (0.4<=this.prob && this.prob<0.6) {
            extra_points = 10;
        }
        else if (0.25<=this.prob && this.prob<0.4) {
            extra_points = 15;
        }
        else if (this.prob<0.25) {
            extra_points = 30;
        }
        this.points += extra_points;
        this.shown_indexes.add(index);
        this.filterWordsByNewLetter(index, c, true);
    }

    private void wrongGuess(int index, char c) {
        this.chances_remaining -= 1;
        if (this.points>15) {
            this.points -= 15;
        }
        else {
            this.points = 0;
        }
        this.filterWordsByNewLetter(index, c, false);
    }

    private void validate(int index, char c) throws MyCharException {
        if (this.shown_indexes.contains(index)) {
            throw new MyCharException();
        }
    }

    private void perform(int index, char c) {
        this.moves ++;
        this.computeProb(index, c);
        if ( c==this.word.charAt(index) ) {
            this.correctGuess(index, c);
        }
        else {
            this.wrongGuess(index, c);
        }
    }

    private void saveGame() {
        /* add this round to the saved */
        ArrayList<String> last_round = new ArrayList<String>();
        last_round.add(this.word);
        last_round.add(Integer.toString(this.moves));
        last_round.add(this.win ? "Player" : "Computer");
        this.prevRounds.add(last_round);
    }

    public void giveUp() throws GameOverException{
        this.playing = false;
        this.win = false;
        this.saveGame();
        throw new GameOverException();
    }

    private void afterCheck() throws GameOverException {
        // game is over...
        if (this.shown_indexes.size()==this.word.length()) {
            this.playing = false;
            this.win = true;
            this.saveGame();
            throw new GameOverException();
        }
        else if (this.chances_remaining==0) {
            this.playing = false;
            this.win = false;
            this.saveGame();
            throw new GameOverException();
        }
        // else...
        else {
            this.updateAvailableChars();
        }
    }

    public void move(int index, char c) throws GameOverException, MyCharException {
        validate(index, c);
        perform(index, c);
        afterCheck();
    }

    private void updateAvailableChars() {
        this.filterWordsBySizeAndLetters();
        this.available_chars = new HashMap<Integer, List<Character>>();

        // for each non found chars of the word
        for (int index=0; index<word.length(); index++) {
            if (!shown_indexes.contains(index)) {
                // count the shows of each char in the hashmap `counter`
                Map counter = new HashMap<Character,  List<Float>>();
                float all = words_left.size();
                for (String word_ : words_left) {
                    char c = word_.charAt(index);
                    if (counter.get(c)!=null) {
                        int x = (int) counter.get(c);
                        counter.put(c, x+1);
                    }
                    else {
                        counter.put(c, 1);
                    }
                }
                // turn the values of the `counter` to probabilities
                for (Object key : counter.keySet()) {
                    int x = (int) counter.get(key);
                    counter.put(key, x/all);
                }

                // turn the items to 'Position' objects, to sort them easily
                ArrayList<Character> chars = new ArrayList<>(counter.keySet());
                ArrayList<Float> ps = new ArrayList<>(counter.values());
                ArrayList<CharPossibility> charPossibilities = new ArrayList<>();
                for (int i=0 ;i <chars.size(); i++) {
                    CharPossibility pos = new CharPossibility(chars.get(i), ps.get(i));
                    charPossibilities.add(pos);
                }
                Collections.sort(charPossibilities);
                // store in an arraylist
                ArrayList<Character> sorted = new ArrayList<>();
                for (CharPossibility pos : charPossibilities) {
                    sorted.add(pos.getC());
                }
                // store in state dictionary
                this.available_chars.put(index, sorted);
            }
        }
    }

}
