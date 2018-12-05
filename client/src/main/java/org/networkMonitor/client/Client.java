package org.networkMonitor.client;

import org.networkMonitor.client.domain.Netstat;
import org.networkMonitor.client.service.FileLoader;
import org.networkMonitor.client.service.NetstatData;

import java.util.List;

public class Client {
    public static void main(String[] args) {
        List<Netstat> payload = NetstatData.process(new FileLoader("file.txt").getFile());
        System.out.println("payload length: " + payload.size());

    }
}
