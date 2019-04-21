package my.rpc;

import java.util.Arrays;

public class DefaultRequest implements Request{
    private long requestId;
    private String className;
    private String methodName;
    private Object[] params;
    private Class[] paramTypes;

    public DefaultRequest(long requestId) {
        this.requestId = requestId;
    }

    public DefaultRequest(long requestId, String className, String methodName, Object[] params) {
        this.requestId = requestId;
        this.className = className;
        this.methodName = methodName;
        this.params = params;

        if (params != null && params.length > 0) {
            Class[] paramTypes = new Class[params.length];
            for (int i = 0; i < params.length; i ++) {
                paramTypes[i] = params[i].getClass();
            }
            this.paramTypes = paramTypes;
        }
    }

    @Override
    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public Object[] getParams() {
        return params;
    }

    @Override
    public Class[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "DefaultRequest{" +
                "requestId=" + requestId +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", params=" + Arrays.toString(params) +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                '}';
    }
}
