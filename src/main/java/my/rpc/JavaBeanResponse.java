package my.rpc;

public class JavaBeanResponse implements Response {
    private String contextId;
    private Object responseObject;


    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }

    @Override
    public String getContextId() {
        return null;
    }

    @Override
    public Object getResponseObject() {
        return this.responseObject;
    }
}
