package models;
//KARLAAAAAAAAAAAAAAAAA!
public class DownLinkMessage {

    private String cmd;
    private String EUI;
    private long port;
    private boolean confirmed;
    private String data;

    public DownLinkMessage()
    {

    }


    public DownLinkMessage(String cmd, String eui, long port, boolean confirmed, String data) {
        this.cmd = cmd;
        EUI = eui;
        this.port = port;
        this.confirmed = confirmed;
        this.data = data;
    }

    public DownLinkMessage( String eui, boolean confirmed, String data) {
        this.cmd = "tx";
        EUI = eui;
        this.port = 1;
        this.confirmed = confirmed;
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

    public long getPort() {
        return port;
    }

    public void setPort(long port) {
        this.port = port;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DownLinkMessage{" +
                "cmd='" + cmd + '\'' +
                ", EUI='" + EUI + '\'' +
                ", port=" + port +
                ", confirmed=" + confirmed +
                ", data='" + data + '\'' +
                '}';
    }
}
