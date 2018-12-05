package org.networkMonitor.client.service;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FileLoaderTest {

    @Test
    public void getFileNoArg() {
        assertNotNull(new FileLoader("file.txt").getFile());
    }

    @Test
    public void getFileWithArg() {
        assertNotNull(new FileLoader("filewithaddr.txt").getFile());
    }

    @Test
    public void hasCorrectContent() {
        File f = new FileLoader("file.txt").getFile();
        try (Scanner scanner = new Scanner(f)) {
            while(scanner.hasNextLine()) {
                assertEquals(
                        "7d1907ef1506d507 stream 0 0 0 7d1907ef1506c17f 0 0 /var/run/mDNSResponder",
                        scanner.nextLine()
                );
            }
        } catch (FileNotFoundException e) {
            fail();
        }
    }
}