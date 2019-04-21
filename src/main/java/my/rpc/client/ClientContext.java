package my.rpc.client;

import my.rpc.Request;
import my.rpc.Response;

public class ClientContext {
    private Long requestId;
    private Request request;
    private Response response;

    public ClientContext() {
    }

    public ClientContext(Request request) {
        this.requestId = request.getRequestId();
        this.request = request;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
