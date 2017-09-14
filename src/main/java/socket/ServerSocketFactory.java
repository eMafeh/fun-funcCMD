package socket;

import socket.config.IOPortConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class ServerSocketFactory {
    private static final Map<IOPortConfig, ServerSocket> SERVER_SOCKET_MAP = new HashMap<>();

    public static ServerSocket getServerSocket(IOPortConfig port) {
        return SERVER_SOCKET_MAP.computeIfAbsent(port, a -> {
            try {
                return new ServerSocket(a.getPort());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
