package com.multi_project.hanging_man.utils;

import java.util.Arrays;
import java.util.List;

public class MyUrls {
    private static String[] urlsArray = {
            "https://openlibrary.org/works/OL31390631M.json",
            "https://openlibrary.org/works/OL1418247W.json",
            "https://openlibrary.org/works/OL27268597M.json",
            "https://openlibrary.org/works/OL7572658M.json",
            "https://openlibrary.org/works/OL26869511M.json",
            "https://openlibrary.org/works/OL32713045M.json",
            "https://openlibrary.org/works/OL76837W.json",
            "https://openlibrary.org/works/OL262421W.json",
            "https://openlibrary.org/works/OL76487W.json",
            "https://openlibrary.org/works/OL472814W.json",
            "https://openlibrary.org/works/OL76833W.json",
            "https://openlibrary.org/works/OL1914088W.json",
            "https://openlibrary.org/works/OL46404W.json",
            "https://openlibrary.org/works/OL77004W.json",
            "https://openlibrary.org/works/OL106083W.json",
            "https://openlibrary.org/works/OL14873315W.json",
    };

    private static List urlsList = Arrays.asList(urlsArray);

    public static List getUrlsList() {
        return urlsList;
    };
}
