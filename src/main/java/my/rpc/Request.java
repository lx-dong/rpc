package my.rpc;

public interface Request {

    String getContextId();

    String getClassName();

    String getMethodName();

    Object[] getParams();
}
