package com.netstat.server.service;

import com.netstat.server.domain.NetstatEntity;
import com.netstat.server.repository.NetstatEntityRepository;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.netstat.client.grpc.NetstatObj;
import org.netstat.client.grpc.NetstatRequest;
import org.netstat.client.grpc.NetstatResponse;
import org.netstat.client.grpc.NetstatServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GRpcService
public class NetstatServiceImpl extends NetstatServiceGrpc.NetstatServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(NetstatServiceImpl.class);
    private final NetstatEntityRepository netstatEntityRepository;

    @Autowired
    public NetstatServiceImpl(NetstatEntityRepository netstatEntityRepository) {
        this.netstatEntityRepository = netstatEntityRepository;
    }

    @Override
    public void sendNetstat(NetstatRequest request, StreamObserver<NetstatResponse> responseObserver) {
        log.info("server received {}", request);
        List<NetstatObj> requestList = request.getObjList();
        requestList.forEach(rl -> {
            NetstatEntity nse = new NetstatEntity();
            nse.setProto(rl.getProto());
            nse.setRecvQ(rl.getRecvQ());
            nse.setSendQ(rl.getSendQ());
            nse.setLocalAddress(rl.getLocalAddress());
            nse.setForeignAddress(rl.getForeignAddress());
            nse.setState(rl.getState());
            netstatEntityRepository.save(nse);
        });
        NetstatResponse response = NetstatResponse.newBuilder().setMessage("Successfully received and saved data").build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
