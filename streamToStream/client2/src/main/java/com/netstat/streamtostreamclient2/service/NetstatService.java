package com.netstat.streamtostreamclient2.service;

import com.netstat.streamtostreamclient2.util.GrpcCallback;
import com.netstat.streamtostreamclient2.util.StreamProcessor;
import org.netstat.client.grpc.NetstatObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Service
public class NetstatService {
    private Logger log = LoggerFactory.getLogger(NetstatService.class);
    private static String os = System.getProperty("os.name").toLowerCase();

    public int execute(GrpcCallback callback) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("netstat", "-p", "tcp", "-w", "1s");
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            log.error("Unable to execute netstat");
            log.error(e.getMessage());
        }
        assert process != null;
        StreamProcessor sp = new StreamProcessor(process.getInputStream(), stream -> {
            try {
                callback.execute(stream);
            } catch (InterruptedException e) {
                log.error("Failed executing grpc callback");
                log.error(e.getMessage());
            }
        });
        Executors.newSingleThreadExecutor().submit(sp);
        int exitCode = 0;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            log.error("Netstat process failed with error");
            log.error(e.getMessage());
        }
        return exitCode;
    }

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

    public NetstatObj convert(String input) {
        List<String> split = Arrays.asList(input.replaceAll(" +", " ").split(" "));
        NetstatObj nso;
        log.info("stream")
        if (!split.get(0).startsWith("input") || !split.get(0).startsWith("packets")) {
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
                        .setRecvQ("")
                        .setSendQ("")
                        .setLocalAddress(split.get(1))
                        .setForeignAddress(split.get(2))
                        .setState(split.get(3))
                        .build();
            }
        } else {
            return null;
        }
        return nso;
    }
}