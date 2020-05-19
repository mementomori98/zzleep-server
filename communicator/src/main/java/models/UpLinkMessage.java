package models;
//KARLAAAAAA
public class UpLinkMessage {

    private String cmd;
    private String EUI;
    private long ts;
    private boolean ack;
    private long fcnt;
    private long port;
    private String encdata;
    private String data;


    public UpLinkMessage()
    {

    }

    public UpLinkMessage(String cmd, String eui, long ts, boolean ack, long fcnt, long port, String encdata, String data) {
        this.cmd = cmd;
        EUI = eui;
        this.ts = ts;
        this.ack = ack;
        this.fcnt = fcnt;
        this.port = port;
        this.encdata = encdata;
        this.data = data;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getEUI() {
        return EUI;
    }

    public void setEUI(String EUI) {
        this.EUI = EUI;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public boolean isAck() {
        return ack;
    }

    public void setAck(boolean ack) {
        this.ack = ack;
    }

    public long getFcnt() {
        return fcnt;
    }

    public void setFcnt(long fcnt) {
        this.fcnt = fcnt;
    }

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public String getEncdata() {
        return encdata;
    }

    public void setEncdata(String encdata) {
        this.encdata = encdata;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "UpLinkMessage{" +
                "cmd='" + cmd + '\'' +
                ", EUI='" + EUI + '\'' +
                ", ts=" + ts +
                ", ack=" + ack +
                ", fcnt=" + fcnt +
                ", port=" + port +
                ", encdata='" + encdata + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

}
