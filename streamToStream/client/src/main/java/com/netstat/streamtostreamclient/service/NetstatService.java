package com.netstat.streamtostreamclient.service;

import com.netstat.streamtostreamclient.util.StreamProcessor;
import org.netstat.client.grpc.NetstatObj;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Service
public class NetstatService {
    private static String os = System.getProperty("os.name").toLowerCase();

    public List<String> execute() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("netstat", "-p", "tcp");
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert process != null;
        List<String> result = new ArrayList<>();
        StreamProcessor sp = new StreamProcessor(process.getInputStream(), result::add);
        result.forEach(System.out::println);
        Executors.newSingleThreadExecutor().submit(sp);
        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert exitCode == 0;
        return result;
    }

    public List<NetstatObj> convert(List<String> result) {
        List<NetstatObj> converted = new ArrayList<>();
        IntStream.range(0, result.size()).forEach(idx -> {
            if (idx > 1) {
                List<String> split = Arrays.asList(result.get(idx).replaceAll(" +", " ").split(" "));
                NetstatObj nso;
                if (os.contains("win")) {
                    nso = NetstatObj.newBuilder()
                            .setProto(split.get(0))
                            .setRecvQ("")
                            .setSendQ("")
                            .setLocalAddress(split.get(1))
                            .setForeignAddress(split.get(2))
                            .setState(split.get(3))
                            .build();
                } else {
                    nso = NetstatObj.newBuilder()
                            .setProto(split.get(0))
                            .setRecvQ(split.get(1))
                            .setSendQ(split.get(2))
                            .setLocalAddress(split.get(3))
                            .setForeignAddress(split.get(4))
                            .setState(split.get(5))
                            .build();
                }
                converted.add(nso);
            }
        });
        return converted;
    }
}