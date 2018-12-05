package org.networkMonitor.client.service;

import org.junit.Test;
import org.networkMonitor.client.domain.Netstat;

import java.util.List;

import static org.junit.Assert.*;

public class NetstatDataTest {

    @Test
    public void parse() {
        String result = new NetstatData().testParse(new FileLoader("file.txt").getFile());
        assertEquals("7d1907ef1506d507 stream 0 0 0 7d1907ef1506c17f 0 0 /var/run/mDNSResponder", result);
    }

//    @Test
//    public void processFileWithAddr() {
//        List<Netstat> ns = NetstatData.process(new FileLoader("filewithaddr.txt").getFile());
//        assertEquals(4, ns.size());
//    }
}