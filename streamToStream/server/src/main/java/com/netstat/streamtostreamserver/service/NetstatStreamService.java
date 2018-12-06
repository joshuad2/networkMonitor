package com.netstat.streamtostreamserver.service;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@GRpcService
public class NetstatStreamService extends NetstatServiceGrpc.NetstatServiceImplBase {
    private final NetstatServiceGrpc.NetstatServiceStub stub;


    private static Logger log = LoggerFactory.getLogger(NetstatStreamService.class);
    private final ConcurrentMap<Integer, List<NetstatRequest>> netstatObjs = new ConcurrentHashMap<>();
    private Integer id = 0;

    public NetstatStreamService() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6566).usePlaintext().build();
        this.stub = NetstatServiceGrpc.newStub(channel);
    }

    @Override
    public StreamObserver<NetstatRequest> sendNetstatS2S(StreamObserver<NetstatResponse> responseObserver) {
        log.info("received a request");

        return new StreamObserver<NetstatRequest>() {
            @Override
            public void onNext(NetstatRequest value) {
                log.info("onNext called with value: {}", value);
                List<NetstatRequest> req = getOrCreateRequestList(value.getObjList());
                List<NetstatResponse> res = new ArrayList<>();
                req.forEach(prevReq -> {
                    List<NetstatObj> prevReqObjList = prevReq.getObjList();
                    prevReqObjList.forEach(nso -> {
                        NetstatResponse response = NetstatResponse.newBuilder().setMessage(
                                "Successfully received netstat object with: " +
                                        nso.getProto() + " " +
                                        nso.getRecvQ() + " " +
                                        nso.getSendQ() + " " +
                                        nso.getLocalAddress() + " " +
                                        nso.getForeignAddress() + " " +
                                        nso.getState()
                        ).build();
                        res.add(response);
                    });
                });
                NetstatResponse resp = NetstatResponse.newBuilder().setMessage("this is a message").build();
                responseObserver.onNext(resp);
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


    private List<NetstatRequest> getOrCreateRequestList(List<NetstatObj> objList) {
        List<NetstatRequest> req = Collections.synchronizedList(new ArrayList<>());
        List<NetstatRequest> prevReq = netstatObjs.putIfAbsent(id, req);
        id++;
        return prevReq != null ? prevReq : req;
    }
}
