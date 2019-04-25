package my.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import my.rpc.*;
import my.rpc.constant.CommonConstant;
import my.rpc.serialization.JavaBeanSerializer;
import my.rpc.serialization.Serializer;

import java.io.IOException;

/**
 * Created by lx-dong on 2019/4/25.
 */
public class DefaultCodec implements NettyCodec {
    Serializer serializer = new JavaBeanSerializer();
    /**
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
     * **/
    /**
     * 读操作
     * @param channel
     * @param byteBuf
     * @return
     * @throws IOException
     */
    @Override
    public Object decode(Channel channel, ByteBuf byteBuf) throws IOException {
        byte type = byteBuf.readByte();
        long requestId = byteBuf.readLong();
        int dataLength = byteBuf.readInt();

        if (type == CommonConstant.RESPONSE_TYPE) { // response
            byte[] data = new byte[dataLength];
            byteBuf.readBytes(data);
            DefaultResponse response = serializer.deserilize(data, DefaultResponse.class); // TODO 异常处理，暂时抛出
            return response;
        }
        else if (type == CommonConstant.REQUEST_TYPE) { // request
            byte[] data = new byte[dataLength];
            byteBuf.readBytes(data);
            DefaultRequest request = serializer.deserilize(data, DefaultRequest.class); // TODO 异常处理，暂时抛出，应封装成exception返回
            return request;
        } else { // unknown
            byte[] data = new byte[dataLength];
            byteBuf.readBytes(data);
            data = null; // 释放错误的数据
            throw new RpcException("decode unknown type, type=" + type);
        }
    }

    /**
     * 写操作
     * @param channel
     * @param byteBuf
     * @param message
     */
    @Override
    public void encode(Channel channel, ByteBuf byteBuf, Object message) throws IOException {
        if (message == null) {
            System.out.println("encode message is null, return.");
            return;
        }

        if (message instanceof Request) { // request
            Request request = (Request) message;
            byteBuf.writeByte(CommonConstant.REQUEST_TYPE);
            byteBuf.writeLong(request.getRequestId());

            byte[] data = serializer.serialize(request);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);

        }
        else if (message instanceof Response) { // response
            Response response = (Response) message;
            byteBuf.writeByte(CommonConstant.RESPONSE_TYPE);
            byteBuf.writeLong(response.getRequestId());

            byte[] data = serializer.serialize(response);
            byteBuf.writeInt(data.length);
            byteBuf.writeBytes(data);
        }
        else {
            throw new RpcException("encode known message, message class=" + message.getClass());
        }
    }
}
