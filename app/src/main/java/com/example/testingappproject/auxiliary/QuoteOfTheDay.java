package com.example.testingappproject.auxiliary;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class QuoteOfTheDay {
    private final String[] defaultQuotes = new String[]{"\"If you can imagine it, you can do it.\"-Napoleon Hill",
            "\"If you have enthusiasm, you can do anything. Enthusiasm is the basis of any progress.\"-Henry Ford",
            "\"There are no lazy people. There are goals that don't inspire.\"-Tony Robbins",
            "\"When you know what you want and you want it badly enough, you'll find a way to get it.\"-Jim Ron",
            "\"A personal strategic work plan is the most important condition for achieving the set goal.\"-Brian Tracy"
    };
    /*
    этот метод возвращает массив, в котором первая строка является цитату и её автора.
    Логично, что имя автора может быть "null" (а это строка, а не значение null).
    В случае исключения (например, нет интернет-соединения),
    метод возвращает случайно выбранную цитату из списка по умолчанию
     */

    private String[] getDefaultQuote() {
        int randomIndex = (int) Math.round(Math.random() * 4);
        return defaultQuotes[randomIndex].split("-");
    }

    public String[] getQuote() {
        try {
            URL url = new URL("https://motivational-quotes1.p.rapidapi.com/motivation");
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestMethod("POST");
            urlc.setRequestProperty("Content-Type", "application/json");
            urlc.setRequestProperty("X-RapidAPI-Key", "3c90247e11msh9398f381e160a2fp14c4b5jsn48a750805a7d");
            urlc.setAllowUserInteraction(false);
            urlc.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String l;
            String[] quote = new String[2];
            int i = 0;
            while ((l = br.readLine()) != null) {
                l = l.trim();
                l = l.replace("-", "");
                l = l.replaceAll("\"", "");
                l = l.replace("null", "");
                quote[i] = l.trim();
                i++;
            }
            br.close();
            return quote;
        } catch (Exception e) {
            return getDefaultQuote();
        }
    }
}