package org.netstat.client.controller;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.netstat.client.grpc.NetstatObj;
import org.netstat.client.grpc.NetstatRequest;
import org.netstat.client.grpc.NetstatResponse;
import org.netstat.client.grpc.NetstatServiceGrpc;
import org.netstat.client.service.NetstatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/v1")
public class NetstatControllerV1 {

    private final NetstatService netstatService;

    @Autowired
    public NetstatControllerV1(NetstatService netstatService) {
        this.netstatService = netstatService;
    }

    @GetMapping("/netstatStream")
    public ResponseEntity getNetstat() {
        List<String> payload = netstatService.execute();
        payload.forEach(System.out::println);
        List<NetstatObj> body = netstatService.convert(payload);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        NetstatServiceGrpc.NetstatServiceBlockingStub stub = NetstatServiceGrpc.newBlockingStub(channel);
        Iterator<NetstatResponse> resp=stub.streamNetStatResponse(NetstatRequest.newBuilder()
                .addAllObj(body)
                .build());
        StringBuilder sb=new StringBuilder();
        resp.forEachRemaining(s->{sb.append(s.getMessage());});
        return new ResponseEntity<>(sb.toString(), HttpStatus.OK);
    }
}
