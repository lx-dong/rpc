package my.rpc;

import my.rpc.exception.RpcException;

import java.io.Serializable;

public class DefaultResponse implements Response, Serializable {
    private static final long serialVersionUID = -1123371282183622021L;

    private long requestId;
    private Object data;
    private RpcException exception;

    public DefaultResponse(long requestId) {
        this.requestId = requestId;
    }

    @Override
    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public boolean hasException() {
        return exception != null;
    }

    @Override
    public RpcException getException() {
        return exception;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setException(RpcException e) {
        this.exception = e;
    }

    @Override
    public String toString() {
        return "DefaultResponse{" +
                "requestId=" + requestId +
                ", data=" + data +
                '}';
    }
}
