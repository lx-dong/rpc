package my.rpc.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import my.rpc.codec.NettyCodec;

import java.util.List;


/**
 * Created by lx-dong on 2019/4/24.
 */
public class NettyDecoder extends ByteToMessageDecoder {
    private NettyCodec codec;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        int readerIndex = buf.readerIndex();
        Object result = codec.decode(ctx.channel(), buf);

        if (result == null) {
            return;
        }
        else if (result == NettyCodec.DecodeResult.NEED_MORE_INPUT) {
            buf.readerIndex(readerIndex);
        } else {
            list.add(result);
        }
    }
}
