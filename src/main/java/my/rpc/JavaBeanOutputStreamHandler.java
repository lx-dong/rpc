package my.rpc;

import my.rpc.constant.CommonConstant;
import my.rpc.serialization.JavaBeanSerializer;
import my.rpc.serialization.Serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class JavaBeanOutputStreamHandler {
    private DataOutputStream dataOutputStream;
    private Serializer serializer = new JavaBeanSerializer();

    public synchronized void writeResponse(Response response) {
        try {
            dataOutputStream.writeByte(CommonConstant.RESPONSE_TYPE);
            dataOutputStream.writeLong(response.getRequestId());
            byte[] data = serializer.serialize(response);
            dataOutputStream.write(data);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeRequest(Request request) {
        try {
            dataOutputStream.writeByte(CommonConstant.REQUEST_TYPE);
            dataOutputStream.writeLong(request.getRequestId());
            byte[] data = serializer.serialize(request);
            dataOutputStream.write(data);
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JavaBeanOutputStreamHandler(OutputStream outputStream) {
        this.dataOutputStream = new DataOutputStream(outputStream);
    }

    public void shutdown() {
        if (this.dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
