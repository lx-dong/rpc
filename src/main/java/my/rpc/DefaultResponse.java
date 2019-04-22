package my.rpc;

import java.io.Serializable;

public class DefaultResponse implements Response, Serializable {
    private static final long serialVersionUID = -1123371282183622021L;

    private long requestId;
    private Object data;

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

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DefaultResponse{" +
                "requestId=" + requestId +
                ", data=" + data +
                '}';
    }
}