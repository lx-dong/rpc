package my.rpc;

import my.rpc.exception.RpcException;

public interface Response {
    long getRequestId();

    Object getData();

    boolean hasException();

    RpcException getException();

}
