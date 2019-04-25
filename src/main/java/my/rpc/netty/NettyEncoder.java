package my.rpc.netty;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import my.rpc.codec.NettyCodec;

/**
 * Created by lx-dong on 2019/4/24.
 */
public class NettyEncoder extends MessageToByteEncoder {
    private NettyCodec codec;

    @Override
    protected void encode(ChannelHandlerContext ctx, Object message, ByteBuf buf) throws Exception {
        codec.encode(ctx.channel(), buf, message);
    }
}
