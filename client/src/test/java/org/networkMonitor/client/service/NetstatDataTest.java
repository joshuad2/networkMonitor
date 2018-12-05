package org.networkMonitor.client.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class NetstatDataTest {

    @Test
    public void parse() {
        String result = new NetstatData().testParse(FileLoader.getInstance().getFile());
        assertEquals("7d1907ef1506d507 stream 0 0 0 7d1907ef1506c17f 0 0 /var/run/mDNSResponder", result);
    }


}