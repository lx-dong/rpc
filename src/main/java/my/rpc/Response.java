package my.rpc;

public interface Response {
    long getRequestId();

    Object getData();

}
