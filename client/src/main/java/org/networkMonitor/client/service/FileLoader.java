package org.networkMonitor.client.service;

import lombok.Data;

import java.io.File;
import java.util.Objects;

@Data
public class FileLoader {

    private String filename;
    private File file;

    public FileLoader(String fn) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        this.file = new File(Objects.requireNonNull(cl.getResource(fn)).getFile());
    }

    public File getFile() {
        return this.file;
    }
}
