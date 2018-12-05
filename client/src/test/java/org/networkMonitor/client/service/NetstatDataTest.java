package org.networkMonitor.client.service;

import com.networkmonitor.proto.NetstatObj;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class NetstatDataTest {

    @Test
    public void parse() {
        String result = new NetstatData().testParse(new FileLoader("file.txt").getFile());
        assertEquals("7d1907ef1506d507 stream 0 0 0 7d1907ef1506c17f 0 0 /var/run/mDNSResponder", result);
    }

    @Test
    public void processFileWithAddr() {
        File f = new FileLoader("filewithaddr.txt").getFile();
        List<NetstatObj> ns = NetstatData.process(f);
        assertEquals(4, ns.size());
    }

    @Test
    public void processFileWithAddrContainingSpace() {
        File f = new FileLoader("filewithaddrcontainingspace.txt").getFile();
        List<NetstatObj> ns = NetstatData.process(f);
        assertEquals(2, ns.size());
    }

    @Test
    public void processFileWithAddrWithoutSlash() {
        File f = new FileLoader("filewithaddrwithoutslash.txt").getFile();
        List<NetstatObj> ns = NetstatData.process(f);
        assertEquals(2, ns.size());
    }

    @Test
    public void processFileWithoutAddr() {
        File f = new FileLoader("filewithoutaddr.txt").getFile();
        List<NetstatObj> ns = NetstatData.process(f);
        assertEquals(2, ns.size());
    }

    @Test
    public void processFileWithBlacklist() {
        File f = new FileLoader("filewithblacklist.txt").getFile();
        List<NetstatObj> ns = NetstatData.process(f);
        assertEquals(0, ns.size());
    }
}