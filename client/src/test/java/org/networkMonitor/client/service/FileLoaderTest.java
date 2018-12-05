package org.networkMonitor.client.service;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FileLoaderTest {

    @Test
    public void getFile() {
        assertNotNull(FileLoader.getInstance().getFile());
    }

    @Test
    public void hasCorrectContent() {
        File f = FileLoader.getInstance().getFile();
        try (Scanner scanner = new Scanner(f)) {
            while(scanner.hasNextLine()) {
                assertEquals(
                        scanner.nextLine(),
                        "7d1907ef1506d507 stream 0 0 0 7d1907ef1506c17f 0 0 /var/run/mDNSResponder"
                );
            }
        } catch (FileNotFoundException e) {
            fail();
        }
    }
}