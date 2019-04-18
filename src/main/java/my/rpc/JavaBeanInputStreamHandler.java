package my.rpc;


import my.rpc.constant.CommonConstant;

import java.io.IOException;
import java.io.ObjectInputStream;

public class JavaBeanInputStreamHandler {
    public ObjectInputStream objectInputStream;

    public void handle() {
        try {
            byte type = objectInputStream.readByte();
            if (type == CommonConstant.REQUEST_TYPE) {
                JavaBeanRequest request = new JavaBeanRequest();
                request.setContextId(objectInputStream.readUTF());
                request.setClassName(objectInputStream.readUTF());
                request.setMathodName(objectInputStream.readUTF());
                request.setParams((Object[]) objectInputStream.readObject());
            } else {
                JavaBeanResponse response = new JavaBeanResponse();
                response.setContextId(objectInputStream.readUTF());
                response.setResponseObject(objectInputStream.readObject());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public JavaBeanInputStreamHandler(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public void setObjectInputStream(ObjectInputStream objectInputStream) {
        this.objectInputStream = objectInputStream;
    }
}
