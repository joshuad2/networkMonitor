package com.netstat.streamtostreamserver.service;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.netstat.client.grpc.NetstatObj;
import org.netstat.client.grpc.NetstatRequest;
import org.netstat.client.grpc.NetstatResponse;
import org.netstat.client.grpc.NetstatServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@GRpcService
public class NetstatStreamService extends NetstatServiceGrpc.NetstatServiceImplBase {
    private static Logger log = LoggerFactory.getLogger(NetstatStreamService.class);

    @Override
    public StreamObserver<NetstatRequest> sendNetstatS2S(StreamObserver<NetstatResponse> responseObserver) {
        log.info("received a request");

        return new StreamObserver<NetstatRequest>() {
            @Override
            public void onNext(NetstatRequest value) {
                log.info("onNext called with value count: {}", value.getObjCount());
                IntStream.range(0, value.getObjCount()).forEach(index -> {
                    NetstatObj request = value.getObj(index);
                    responseObserver.onNext(NetstatResponse.newBuilder().setMessage(
                            "Successfully received netstat object with: " +
                                    request.getProto() + " " +
                                    request.getRecvQ() + " " +
                                    request.getSendQ() + " " +
                                    request.getLocalAddress() + " " +
                                    request.getForeignAddress() + " " +
                                    request.getState()
                    ).build());
                });
            }

            @Override
            public void onError(Throwable t) {
                log.warn("sendNetstatS2S canceled");
                responseObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
