package com.netstat.streamtostreamclient2.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.netstat.client.grpc.NetstatObj;
import org.netstat.client.grpc.NetstatRequest;
import org.netstat.client.grpc.NetstatResponse;
import org.netstat.client.grpc.NetstatServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
@GRpcService
public class NetstatServiceGrpcImpl extends NetstatServiceGrpc.NetstatServiceImplBase {

    private final NetstatService netstatService;
    private final ManagedChannel channel;
    private final NetstatServiceGrpc.NetstatServiceStub stub;
    private Logger log = LoggerFactory.getLogger(NetstatServiceGrpcImpl.class);

    @Autowired
    public NetstatServiceGrpcImpl(NetstatService netstatService) {
        this.channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        this.stub = NetstatServiceGrpc.newStub(channel);
        this.netstatService = netstatService;
    }

    @Override
    public StreamObserver<NetstatRequest> sendNetstatS2S(StreamObserver<NetstatResponse> responseObserver) {
        return new StreamObserver<NetstatRequest>() {
            @Override
            public void onNext(NetstatRequest value) {
                log.info("value: {}", value);
            }

            @Override
            public void onError(Throwable t) {
                log.info("error: {}", t.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("completed");
            }
        };
    }

    @Scheduled(fixedRate = 2500)
    private void runProcedure() throws InterruptedException {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<NetstatRequest> requestStreamObserver = stub.sendNetstatS2S(createResponse(finishLatch));
        List<String> execute = netstatService.execute();
        List<NetstatObj> convert = netstatService.convert(execute);
        NetstatRequest request = NetstatRequest.newBuilder().addAllObj(convert).build();
        try {
            requestStreamObserver.onNext(request);
            if (finishLatch.getCount() == 0) {
                return;
            }
        } catch (RuntimeException e) {
            requestStreamObserver.onError(e);
            throw e;
        }
        requestStreamObserver.onCompleted();
        if (!finishLatch.await(1, TimeUnit.MINUTES)) {
            log.warn("sendNetstatS2S cannot finish within 1 minute");
        }
    }

    private StreamObserver<NetstatResponse> createResponse(CountDownLatch finishLatch) {
        return new StreamObserver<NetstatResponse>() {
            @Override
            public void onNext(NetstatResponse value) {
                log.info("received response from server: {}", value.getMessage());
            }

            @Override
            public void onError(Throwable t) {
                log.info("error receiving response from server");
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };
    }
}
