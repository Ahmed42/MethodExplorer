package com.methodexplorer;

import org.repodriller.persistence.PersistenceMechanism;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


/**
 * Created by Admin on 2/10/2018.
 */

// I wrote this because the CSVFile that comes with RepoDriller insists on putting inconsistent " " around methods signatures
// ^ I now know why (thanks to Github). Commas in methods' signatures interfere with the CSV format, hence the quotes around more-than-a-single param methods.
public class CSVEntryWriter implements PersistenceMechanism {
    private String filePath;

    public CSVEntryWriter(String filePath) {
        this.filePath = filePath;
    }

    // Assumes passed objects are Strings
    @Override
    public void write(Object... objects) {
        String hash = (String) objects[0];
        String oldSignature = (String) objects[1];
        String newSignature = (String) objects[2];

        String entry = "\n" + hash + ", \"" + oldSignature + "\", \"" + newSignature + "\"";
        try {
            Files.write(Paths.get(filePath), entry.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException ex) {
            // do nothing ...
        }
    }

    @Override
    public void close() {

    }
}
