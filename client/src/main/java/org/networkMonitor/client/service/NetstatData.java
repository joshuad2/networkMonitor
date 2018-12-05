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
                    String test = result.get(i).substring(0, 2);
                    String pAddr = result.get(i + 8);
                    if (pAddr.length() != 16 && !pAddr.startsWith(test)) {
                        int nextIndex = i + 9;
                        if (nextIndex < result.size()) {
                            if (!result.get(nextIndex).startsWith(test)) {
                                ns.setAddr(pAddr + " " + result.get(nextIndex));
                                i += 10;
                            } else {
                                ns.setAddr(pAddr);
                                i += 9;
                            }
                        } else {
                            ns.setAddr(pAddr);
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
        try (Scanner scanner = new Scanner(file)) {
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
