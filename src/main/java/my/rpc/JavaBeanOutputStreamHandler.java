package my.rpc;

import my.rpc.constant.CommonConstant;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class JavaBeanOutputStreamHandler {
    public ObjectOutputStream objectOutputStream;

    public synchronized void handle(Response response) {
        try {
            objectOutputStream.writeByte(CommonConstant.RESPONSE_TYPE);
            objectOutputStream.writeUTF(response.getContextId());
            objectOutputStream.writeObject(response.getResponseObject());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void handle(Request request) {
        try {
            objectOutputStream.writeByte(CommonConstant.REQUEST_TYPE);
            objectOutputStream.writeUTF(request.getContextId());
            objectOutputStream.writeUTF(request.getClassName());
            objectOutputStream.writeUTF(request.getMethodName());
            objectOutputStream.writeObject(request.getParams());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JavaBeanOutputStreamHandler(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }
}
