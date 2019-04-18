package my.rpc;

public class JavaBeanRequest implements Request{
    private String contextId;
    private String className;
    private String mathodName;
    private Object[] params;


    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMathodName() {
        return mathodName;
    }

    public void setMathodName(String mathodName) {
        this.mathodName = mathodName;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    @Override
    public String getContextId() {
        return null;
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public String getMethodName() {
        return this.mathodName;
    }

    @Override
    public Object[] getParams() {
        return this.params;
    }
}
