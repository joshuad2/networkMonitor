package org.networkMonitor.client.service;

import org.networkMonitor.client.domain.Netstat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class NetstatData {
    private static final ArrayList<String> blacklist = new ArrayList<String>() {{
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

    public static List<Netstat> process(File file) {
        List<String> result = Arrays.asList(parse(file).split(" "));
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
        return payload;
    }

    private static String parse(File file) {
        StringBuilder line = new StringBuilder();
        try (Scanner scanner = new Scanner(new FileLoader("file.txt").getFile())) {
            while (scanner.hasNextLine()) {
                line.append(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line.toString();
    }

    public String testParse(File file) {
        return parse(file);
    }
}
