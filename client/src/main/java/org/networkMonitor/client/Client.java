package org.networkMonitor.client;

import org.networkMonitor.client.domain.Netstat;
import org.networkMonitor.client.service.FileLoader;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException {
        final ArrayList<String> blacklist = new ArrayList<String>() {{
            add("Active");
            add("LOCAL");
            add("(UNIX)");
            add("domain");
            add("sockets");
            add("Address");
            add("Type");
            add("Recv-Q");
            add("Send-Q");
            add("Inode");
            add("Conn");
            add("Refs");
            add("Nextref");
            add("Addr");
        }};

        StringBuilder line = new StringBuilder();
        try (Scanner scanner = new Scanner(FileLoader.getInstance().getFile())) {
            while (scanner.hasNextLine()) {
                line.append(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> result = Arrays.asList(line.toString().split(" "));
        List<Netstat> payload = new ArrayList<>();
        int i = 0;
        try {
            while (i < result.size() + 1) {
                if (!blacklist.contains(result.get(i))) {
                    Netstat ns = Netstat.builder()
                            .address(result.get(i))
                            .type(result.get(i + 1))
                            .recvQ(result.get(i + 2))
                            .sendQ(result.get(i + 3))
                            .iNode(result.get(i + 4))
                            .conn(result.get(i + 5))
                            .refs(result.get(i + 6))
                            .nextref(result.get(i + 7))
                            .build();
                    if (!result.get(i+8).startsWith("7")) {
                        if (!result.get(i + 9).startsWith("7")) {
                            ns.setAddr(result.get(i + 8) + " " + result.get(i + 9));
                            i += 10;
                        } else {
                            ns.setAddr(result.get(i + 8));
                            i += 9;
                        }
                    } else {
                        ns.setAddr("");
                        i += 8;
                    }
                    System.out.println(ns.toString());
                    payload.add(ns);
                } else {
                    i++;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        System.out.println("payload length: " + payload.size());
    }
}
