package my.rpc;

import my.rpc.exception.RpcException;
import my.rpc.netty.NettyServiceRegister;

/**
 * Created by lx-dong on 2019/5/6.
 */
public class MessageRouter implements MessageHandler{
    private ServiceRegister serviceRegister;

    public MessageRouter(ServiceRegister serviceRegister) {
        this.serviceRegister = serviceRegister;
    }

    @Override
    public Response handle(Request request) {
        Provider provider = (Provider) serviceRegister.getService(request.getClassName());
        if (provider == null) {
            DefaultResponse response = new DefaultResponse(request.getRequestId());
            response.setException(new RpcException("provider not found! serviceName:" + request.getClassName()));
        }
        return call(provider, request);

    }

    private Response call(Provider provider, Request request) {
        try {
            return provider.call(request);
        } catch (Exception e) {
            DefaultResponse response = new DefaultResponse(request.getRequestId());
            response.setException(new RpcException(e.getMessage()));
            return response;
        }

    }
}
