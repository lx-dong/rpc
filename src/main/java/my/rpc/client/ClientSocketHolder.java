package my.rpc.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSocketHolder {
    private static final ClientSocketHolder instance = new ClientSocketHolder();
    private Map<String, Socket> cache;

    public ClientSocketHolder() {
        cache = new ConcurrentHashMap<>();
    }

    public static ClientSocketHolder getInstance() {
        return instance;
    }

    public void add(final String host, final int port, Socket socket) {
        if (host == null || socket == null) {
            throw new IllegalArgumentException();
        }
        String key = buildKey(host, port);
        Socket old = cache.putIfAbsent(key, socket);
        if (old != socket) {
            if (old.isClosed()) {
                cache.put(key, socket);
            } else {
                throw new RuntimeException("multi sockets not allowed, on one host and one port can build only one socket!");
            }
        }
    }

    public Socket getOrBuild(final String host, final int port) throws IOException {
        String key = buildKey(host, port);
        Socket socket = cache.get(key);
        if (socket == null || socket.isClosed()) {
            socket = new Socket(host, port);
            cache.put(key, socket);
        }
        return socket;
    }

    private String buildKey(final String host, final int port){
        return host + ":" + port;
    }

}
