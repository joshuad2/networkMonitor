package org.networkMonitor.client;

import org.networkMonitor.client.service.FileLoader;
import org.networkMonitor.client.service.NetstatData;

import java.io.IOException;

public class Client {
    public static void main(String[] args) throws IOException {
        NetstatData.process(FileLoader.getInstance().getFile());
    }
}
