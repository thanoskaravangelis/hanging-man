package com.multi_project.hanging_man.utils;

import com.multi_project.hanging_man.exceptions.InvalidCountException;
import com.multi_project.hanging_man.exceptions.InvalidRangeException;
import com.multi_project.hanging_man.exceptions.UnbalancedException;
import com.multi_project.hanging_man.exceptions.UndersizeException;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

/**
 * DictMaker is the class that makes the HTTP requests and the dictionary's filtering
 * @author thanos_karavangelis
 */
public class MyApi {

    /**
     * The connection that will be used.
     */
    private static HttpURLConnection conn;

    /**
     *
     * @param urlContent (is the string containing the URL)
     * @return the response content
     * @throws IOException
     */
    public static StringBuilder get_http_request(String urlContent) throws IOException {
        BufferedReader reader;
        String line;

        StringBuilder responseContent = new StringBuilder();
        String nextUrl = urlContent;
        System.out.println("Trying to create a dictionary from URL: "+nextUrl);
        URL url = new URL(nextUrl);
        conn = (HttpURLConnection) url.openConnection();

        // Request setup
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);// 5000 milliseconds = 5 seconds
        conn.setReadTimeout(5000);

        // Test if the response from the server is successful
        int status = conn.getResponseCode();

        if (status >= 300) {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
        }
        return responseContent;
    }

    /**
     *
     * @param responseBody (string builder containing the response in JSON)
     * @return the description of the book
     */
    public static String parse(StringBuilder responseBody) {
        JsonReader jsonReader = Json.createReader(new StringReader(responseBody.toString()));
        System.out.println(responseBody.toString());
        JsonObject object = jsonReader.readObject();
        String values = "";
        if(object.containsKey("description")) {
            try {
                Boolean valueBoolean = object.getJsonObject("description").containsKey("value");
                if (valueBoolean) {
                    values = object.getJsonObject("description").getString("value");
                }
            }
            catch(Exception e){
                // This exception is used in order to keep JSON objects that only have key "description"
                // and not key "value".
                values = object.getString("description");
            }
        }
        return values;
    }

    /**
     *
     * @param description (in string format)
     * @return a list with all the acceptable words based on their length
     */
    public static List filter_description(String description){
        String all_words[] = description.replaceAll("[^a-zA-Z ]", "").toUpperCase().split("\\s+");
        List<String> all_words_list = new ArrayList<String>();

        for (int i = 0; i < all_words.length; i++ ){
            if(all_words[i].length() >= 6 && !all_words[i].contains("HTTP")) {
                all_words_list.add(all_words[i]);
            }
        }

        Set<String> toBeReturned = new HashSet<>(all_words_list);

        return toBeReturned.stream().toList();
    }

    /**
     *
     * @param dictionary (a list containing all the description's words)
     * @return a string with all the duplicate words in the dictionary
     */
    public static String dictionary_has_duplicates(List<String> dictionary) {
        Set<String> distinctWords = new HashSet<String>();
        Set<String> toBeReturned = new HashSet<String>();
        for(String word : dictionary) {
            if (!distinctWords.add(word)){
                toBeReturned.add(word);
            }
        }
        return toBeReturned.stream().toList().toString();
    }

    /**
     *
     * @param dictionary
     * @return true/false according to if there are words with length less than 6
     */
    public static Boolean dictionary_has_short_words(List dictionary) {
        for (int i = 0; i < dictionary.size(); i++){
            if (dictionary.get(i).toString().length() < 6){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param responseBody
     * @return the title of the book retrieved from the response content
     */
    public static String get_title_of_book(StringBuilder responseBody){
        JsonReader jsonReader = Json.createReader(new StringReader(responseBody.toString()));
        JsonObject object = jsonReader.readObject();
        String title = "" ;
        try {
            title = object.getString("title").replace(" ","_");
        }
        catch(Exception e){
            // Random string generation of size 7 if title is not found, taken from
            // https://www.baeldung.com/java-random-string
            byte[] array = new byte[7];
            new Random().nextBytes(array);
            title = new String(array, Charset.forName("UTF-8"));
        }

        return title;
    }

    /**
     *
     *
     * <ul>
     *     <li>
     *         Counts the percentage of words with size over 9
     *     </li>
     *     <li>
     *         Checks if there are duplicates in the dictionary
     *     </li>
     *     <li>
     *         Checks if one of the four exceptions should be thrown
     *     </li>
     *     <li>
     *         If no exceptions are thrown a dictionary file is created with a specific ID.
     *     </li>
     * </ul>
     *
     *
     *
     * @param providedURL  (the URL we make the request for)
     * @param fileID       (an ascending counter)
     * @param providedFileID  (a provided ID by the user)
     * @throws IOException
     * @throws InvalidCountException
     * @throws UndersizeException
     * @throws InvalidRangeException
     * @throws UnbalancedException
     */

    public static void createDictionaryAndCheckAll(String providedURL, Integer fileID, Integer providedFileID) throws IOException, InvalidCountException, UndersizeException, InvalidRangeException, UnbalancedException {
        StringBuilder responseContent = get_http_request(providedURL);
        String words_from_description = parse(responseContent);
        List accepted_words = filter_description(words_from_description);

        int counter = 0;
        for (int i = 0; i < accepted_words.size(); i++) {
            if (accepted_words.get(i).toString().length() >= 9)
                counter++;
        }

        double percentage = ((counter / (double) accepted_words.size()) * 100);

        // Checking for one of four main exceptions.

        String duplicatesString = dictionary_has_duplicates(accepted_words);
        if (!duplicatesString.equals("[]")) {
            InvalidCountException inv = new InvalidCountException(duplicatesString);
            throw inv;
        }

        if (accepted_words.size() < 20) {
            UndersizeException undersizeException = new UndersizeException();
            throw undersizeException;
        }

        if (dictionary_has_short_words(accepted_words)) {
            InvalidRangeException invalidRangeException = new InvalidRangeException();
            throw invalidRangeException;
        }

        if (percentage < 20) {
            UnbalancedException unbalancedException = new UnbalancedException();
            throw unbalancedException;
        }

        String title = get_title_of_book(responseContent)+"_";
        if(providedFileID == -1) {
            title = title + fileID;
        }
        else if(providedFileID > 0 && !Directory.dictExistsWithId(providedFileID)) {
            title = title + providedFileID;
        }
        else if(providedFileID > 0 && Directory.dictExistsWithId(providedFileID)) {
            title = title + Directory.getMaxId()+1;
        }

        Directory.createFile(accepted_words, title);

        System.out.println("This is file with filename `hangman_" + title + "` ID: " + Directory.getIdOfDict("hangman_" + title));
        System.out.println("Found " + accepted_words.size() + " total words.");
        System.out.println("Found " + counter + " words with length 9 or bigger. That accounts to " + percentage + "% of the words.");
    }

    /**
     *
     * <ul>
     *     <li>
     *         Main function that loops over all the given URLs
     *     </li>
     * </ul>
     *
     * @param args
     */
    public static void main(String[] args) {
        // Get URLs list
        List myUrls = MyUrls.getUrlsList();
        // Getting a ListIterator for my urls' list
        ListIterator<String> urlsIterator = myUrls.listIterator();
        // Counter of file IDs
        int file_counter = 0;

        System.out.println("Found " + myUrls.size() + " urls.");
        while (urlsIterator.hasNext()) {
            try {
                createDictionaryAndCheckAll(urlsIterator.next(), file_counter, -1);
                file_counter++;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
            }
        }
    }
}