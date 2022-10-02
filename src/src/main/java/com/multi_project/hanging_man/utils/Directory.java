package com.multi_project.hanging_man.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Directory {

    private static String fileIdx = "C:\\Users\\acer\\IdeaProjects\\hanging_man\\medialab\\hangman_";

    public static void createFile(List words, String title){
        try {
            String fileName = fileIdx + title;
            File myObj = new File(fileName);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            try {
                FileWriter myWriter = new FileWriter(fileName);
                for (int i = 0; i < words.size(); i++) {
                    myWriter.write(words.get(i).toString()+"\n");
                }
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred while writing to file "+ fileName + " .");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static List<String> getDictionaries(){
        File folder = new File(fileIdx.replace("\\hangman_",""));
        File[] listOfFiles = folder.listFiles();

        List<String> toBeReturned = new ArrayList<String>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                toBeReturned.add(listOfFiles[i].getName().replace("hangman_",""));
            }
        }
        return toBeReturned;
    }

    public static Integer getIdOfDict(String fileName) {
        String[] charsOfFile = fileName.split("_");
        return Integer.parseInt(charsOfFile[charsOfFile.length-1]);
    }

    public static Boolean dictExistsWithId(int id) {
        int this_id;
        for (String name : getDictionaries()){
            this_id = getIdOfDict(name);
            if ( id==this_id ){
                return true;
            }
        }
        return false;
    }

    public static Integer getMaxId() {
        int this_id, max_id = -1;
        for (String name : getDictionaries()){
            this_id = getIdOfDict(name);
            if ( this_id > max_id ){
                max_id = this_id;
            }
        }
        return max_id;
    }

    public static String getFilenameWithId(int id){
        for (String name : getDictionaries()){
            int l = name.length();
            String[] namearr = name.split("_");
            if(Integer.parseInt( namearr[namearr.length -1] ) == id) {
                return name;
            }
        }
        return "";
    }


    public static List<String> getWordsOfDict(String fileName) throws IOException{
        // This function returns the words of a dictionary in a list.
        List<String> accepted_words = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileIdx + fileName));
        String line = reader.readLine();
        while (line != null) {
            accepted_words.add(line.replace("\n",""));
            // read next line
            line = reader.readLine();
        }
        reader.close();

        return accepted_words;
    }

    public static Double getPercentageOfCategory(String fileName, Integer category) throws IOException{
        // For this function, I have matched an Integer variable named "category"
        // like this:
        // 1 : getPercentage of words with size 6
        // 2 : getPercentage of words with size 7 - 9
        // 3 : getPercentage of words with size 10 +

        List<String> accepted_words = getWordsOfDict(fileName);

        int counter = 0;
        for (int i = 0; i < accepted_words.size(); i++) {
            if (accepted_words.get(i).toString().length() ==6 && category == 1 ) {
                counter++;
            }
            if (accepted_words.get(i).toString().length() >= 7 && accepted_words.get(i).toString().length() <= 9 && category == 2 ) {
                counter++;
            }
            if (accepted_words.get(i).toString().length() >= 10 && category == 3 ) {
                counter++;
            }
        }

        Double percentage = ((counter / (double) accepted_words.size()) * 100);

        return percentage;
    }

}
