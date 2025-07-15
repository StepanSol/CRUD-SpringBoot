package org.example.readerManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ReaderManager {
    public static BufferedReader reader;

    private ReaderManager(){
    }

    public static BufferedReader getReader(){
        if (reader == null){
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        return reader;
    }
}
