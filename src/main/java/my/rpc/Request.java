package my.rpc;

public interface Request{

    long getRequestId();

    String getClassName();

    String getMethodName();

    Object[] getParams();

    Class[] getParamTypes();
}
