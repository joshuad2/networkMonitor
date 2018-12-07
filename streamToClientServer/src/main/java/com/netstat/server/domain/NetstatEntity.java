package com.netstat.server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class NetstatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String proto;
    private String recvQ;
    private String sendQ;
    private String localAddress;
    private String foreignAddress;
    private String state;

    public NetstatEntity() {
    }

    public NetstatEntity(String proto, String recvQ, String sendQ, String localAddress, String foreignAddress, String state) {
        this.proto = proto;
        this.recvQ = recvQ;
        this.sendQ = sendQ;
        this.localAddress = localAddress;
        this.foreignAddress = foreignAddress;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProto() {
        return proto;
    }

    public void setProto(String proto) {
        this.proto = proto;
    }

    public String getRecvQ() {
        return recvQ;
    }

    public void setRecvQ(String recvQ) {
        this.recvQ = recvQ;
    }

    public String getSendQ() {
        return sendQ;
    }

    public void setSendQ(String sendQ) {
        this.sendQ = sendQ;
    }

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getForeignAddress() {
        return foreignAddress;
    }

    public void setForeignAddress(String foreignAddress) {
        this.foreignAddress = foreignAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "NetstatEntity{" +
                "id=" + id +
                ", proto='" + proto + '\'' +
                ", recvQ='" + recvQ + '\'' +
                ", sendQ='" + sendQ + '\'' +
                ", localAddress='" + localAddress + '\'' +
                ", foreignAddress='" + foreignAddress + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetstatEntity that = (NetstatEntity) o;
        return id == that.id &&
                Objects.equals(proto, that.proto) &&
                Objects.equals(recvQ, that.recvQ) &&
                Objects.equals(sendQ, that.sendQ) &&
                Objects.equals(localAddress, that.localAddress) &&
                Objects.equals(foreignAddress, that.foreignAddress) &&
                Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, proto, recvQ, sendQ, localAddress, foreignAddress, state);
    }
}
