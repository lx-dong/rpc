package my.rpc.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientContextHolder {

    private Map<Long, ClientContext> map = new ConcurrentHashMap<>();


    public void add(ClientContext context) {
        map.put(context.getRequestId(), context);
    }

    public ClientContext remove(Long requestId) {
        return map.remove(requestId);
    }

    public ClientContext get(Long requestId) {
        return get(requestId);
    }

}
