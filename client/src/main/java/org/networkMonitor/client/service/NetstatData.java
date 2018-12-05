package org.networkMonitor.client.service;

import com.networkmonitor.proto.NetstatObj;
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

    public static List<NetstatObj> process(File file) {
        List<String> result = Arrays.asList(parse(file).split(" "));
        List<NetstatObj> payload = new ArrayList<>();
        int i = 0;
        try {
            while (i < result.size() + 1) {
                if (!blacklist.contains(result.get(i))) {
                    String test = result.get(i).substring(0, 2);
                    StringBuilder addr = new StringBuilder();
                    Netstat ns = Netstat.builder()
                            .address(result.get(i))
                            .type(result.get(i + 1))
                            .recvQ(result.get(i + 2))
                            .sendQ(result.get(i + 3))
                            .iNode(result.get(i+ 4))
                            .conn(result.get(i + 5))
                            .refs(result.get(i + 6))
                            .nextref(result.get(i + 7))
                            .build();
                    if (i + 8 < result.size()) {
                        String pAddr = result.get(i + 8);
                        if (pAddr.length() != 16 && !pAddr.startsWith(test) && i + 8 < result.size()) {
                            int nextIndex = i + 9;
                            if (nextIndex < result.size()) {
                                if (!result.get(nextIndex).startsWith(test)) {
                                    addr.append(pAddr).append(result.get(nextIndex));
                                    i += 10;
                                } else {
                                    addr.append(pAddr);
                                    i += 9;
                                }
                            } else {
                                addr.append(pAddr);
                                i += 9;
                            }
                        } else {
                            addr.append(pAddr);
                            i += 8;
                        }
                    } else {
                        i += 8;
                    }
                    NetstatObj nso = NetstatObj.newBuilder()
                            .setAddress(ns.getAddress())
                            .setType(ns.getType())
                            .setRecvQ(ns.getRecvQ())
                            .setSendQ(ns.getSendQ())
                            .setINode(ns.getINode())
                            .setConn(ns.getConn())
                            .setRefs(ns.getRefs())
                            .setNextref(ns.getNextref())
                            .setAddr(addr.toString())
                            .build();
                    System.out.println(nso.toString());
                    payload.add(nso);
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
