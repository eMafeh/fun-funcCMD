package socket.config;

public enum  IOPortConfig {
    GETPORT(4044);



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
