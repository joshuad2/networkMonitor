package org.networkMonitor.client;

import com.networkmonitor.proto.NetstatObj;
import com.networkmonitor.proto.NetstatRequest;
import com.networkmonitor.proto.NetstatResponse;
import com.networkmonitor.proto.NetstatServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.networkMonitor.client.service.FileLoader;
import org.networkMonitor.client.service.NetstatData;

import java.util.List;

public class Client {
    public static void main(String[] args) {
        List<NetstatObj> payload = NetstatData.process(new FileLoader("file.txt").getFile());
        System.out.println("payload length: " + payload.size());
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        NetstatServiceGrpc.NetstatServiceBlockingStub stub = NetstatServiceGrpc.newBlockingStub(channel);
        NetstatResponse netstatResponse = stub.sendNetstat(NetstatRequest.newBuilder()
                .addAllObj(payload)
                .build());
        channel.shutdown();
    }
}
