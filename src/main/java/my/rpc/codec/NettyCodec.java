package my.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.io.IOException;

/**
 * Created by lx-dong on 2019/4/25.
 */
public interface NettyCodec {
    Object decode(Channel channel, ByteBuf byteBuf) throws IOException;

    void encode(Channel channel, ByteBuf byteBuf, Object message) throws IOException;

    enum DecodeResult{
        NEED_MORE_INPUT
    }
}
