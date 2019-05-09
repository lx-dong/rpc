package my.rpc;

import java.lang.reflect.Method;

public class DefaultProvider implements Provider{
    private Object serviceImpl;

    public <T> DefaultProvider(T newInstance) {
        this.serviceImpl = newInstance;
    }


    @Override
    public Response call(Request request) throws Exception {
        DefaultResponse response = new DefaultResponse(request.getRequestId());
        Method method = serviceImpl.getClass().getMethod(request.getMethodName(), request.getParamTypes());
        Object data = method.invoke(serviceImpl, request.getParams());
        response.setData(data);
        return response;

    }
}
