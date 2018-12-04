package org.networkMonitor.client.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Netstat {
    private String address;
    private String type;
    private String recvQ;
    private String sendQ;
    private String iNode;
    private String conn;
    private String refs;
    private String nextref;
    private String addr;
}
