package my.rpc;


import my.rpc.constant.CommonConstant;
import my.rpc.serialization.JavaBeanSerializer;
import my.rpc.serialization.Serializer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaBeanInputStreamHandler{
    private Serializer serializer = new JavaBeanSerializer();
    private DataInputStream dataInputStream;

    public Request readRequest() throws IOException {
        try {

            byte type = dataInputStream.readByte();
            long requestId = dataInputStream.readLong();
            int dataLength = dataInputStream.readInt(); // request byte length, 长度暂时不校验

            if (type == CommonConstant.REQUEST_TYPE) {
                byte[] data = new byte[dataLength];
                dataInputStream.read(data);
                Request request = serializer.deserilize(data, DefaultRequest.class);
                return request;
            } else {
                throw new RpcException("type is not request, type=" + type);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public Response readResponse() throws IOException {
        System.out.println("readResponse,,");
        try {
            byte type = dataInputStream.readByte();
            long requestId = dataInputStream.readLong();
            int dataLength = dataInputStream.readInt();

            if (type == CommonConstant.RESPONSE_TYPE) {
                byte[] data = new byte[dataLength];
                dataInputStream.read(data);
                DefaultResponse response = serializer.deserilize(data, DefaultResponse.class);
                return response;
            } else {
                throw new RpcException("type is not response, type=" + type);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean available() throws IOException {
        int available = dataInputStream.available();
        return available > 0;
    }

    public JavaBeanInputStreamHandler(InputStream inputStream) throws IOException {
        this.dataInputStream = new DataInputStream(inputStream);
    }


    public void shutdown() {
        System.out.println("dataInputStream shutdown ..");
        if (this.dataInputStream != null) {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
