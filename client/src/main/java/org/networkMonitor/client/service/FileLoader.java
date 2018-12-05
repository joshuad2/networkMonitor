package org.networkMonitor.client.service;

import lombok.Data;

import java.io.File;
import java.util.Objects;

@Data
public class FileLoader {

    private static final String filename = "file.txt";
    private File file;

    private static FileLoader ourInstance = new FileLoader();

    public static FileLoader getInstance() {
        return ourInstance;
    }

    private FileLoader() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        this.file = new File(Objects.requireNonNull(classLoader.getResource(filename)).getFile());
    }
}
