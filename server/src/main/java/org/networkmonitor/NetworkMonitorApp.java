package org.networkmonitor;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import org.networkMonitor.message.grpc.NetstatRequest;
import org.networkMonitor.message.grpc.NetstatResponse;
import org.networkMonitor.message.grpc.NetstatServiceGrpc;
import org.networkMonitor.message.grpc.NetstatServiceGrpc.NetstatServiceImplBase;

import io.grpc.stub.StreamObserver;

public class NetworkMonitorApp extends  NetstatServiceImplBase {
	
	@Override
    public void sendNetstat(NetstatRequest request,
            StreamObserver<NetstatResponse> responseObserver) {
          asyncUnimplementedUnaryCall(NetstatServiceGrpc.METHOD_SEND_NETSTAT, responseObserver);
        }

}
