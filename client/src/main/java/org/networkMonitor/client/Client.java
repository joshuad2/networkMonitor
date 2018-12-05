package org.networkMonitor.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.networkMonitor.client.service.FileLoader;
import org.networkMonitor.client.service.NetstatData;
import org.networkMonitor.message.grpc.NetstatObj;
import org.networkMonitor.message.grpc.NetstatRequest;
import org.networkMonitor.message.grpc.NetstatServiceGrpc;

import java.util.List;

public class Client {
    public static void main(String[] args) {
        List<NetstatObj> payload = NetstatData.process(new FileLoader("file.txt").getFile());
        System.out.println("payload length: " + payload.size());
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        NetstatServiceGrpc.NetstatServiceBlockingStub stub = NetstatServiceGrpc.newBlockingStub(channel);
        stub.sendNetstat(NetstatRequest.newBuilder()
                .addAllObj(payload)
                .build());
        channel.shutdown();
    }
}
