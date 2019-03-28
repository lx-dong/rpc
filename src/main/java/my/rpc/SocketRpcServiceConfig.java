package my.rpc;

import java.util.Map;

public class SocketRpcServiceConfig implements RpcConfig{
    private int port;
    private int timeoutMill;
    private String name;

    public SocketRpcServiceConfig(int port) {
        this.port = port;
    }



    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeoutMill() {
        return timeoutMill;
    }

    public void setTimeoutMill(int timeoutMill) {
        this.timeoutMill = timeoutMill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
