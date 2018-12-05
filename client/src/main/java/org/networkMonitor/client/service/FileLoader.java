package org.networkMonitor.client.service;

import java.io.File;
import java.util.Objects;

public class FileLoader {

    private File file;

    public FileLoader(String fn) {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        this.file = new File(Objects.requireNonNull(cl.getResource(fn)).getFile());
    }

    public File getFile() {
        return this.file;
    }
}
