package com.netstat.streamtostreamclient2.util;

public interface GrpcCallback {
    void execute(String stream) throws InterruptedException;
}
