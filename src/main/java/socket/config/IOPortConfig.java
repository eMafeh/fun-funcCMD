package socket.config;

public enum  IOPortConfig {
    GETPORT(5122),MESSAGE_PORT(4043);


    int port;
    IOPortConfig(int port){
        this.port=port;
    }
    public int getPort() {
        return port;
    }
    @Override
    public String toString() {
        return "IOPortConfig{" +
                "port=" + port +
                '}';
    }
}
