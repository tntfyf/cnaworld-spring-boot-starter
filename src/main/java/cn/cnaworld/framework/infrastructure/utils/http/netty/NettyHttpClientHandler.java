package cn.cnaworld.framework.infrastructure.utils.http.netty;


import cn.cnaworld.framework.infrastructure.utils.log.CnaLogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lucifer
 * @date 2023/3/12
 * @since 1.0.0
 */
@Slf4j
@Getter
@Setter
@ChannelHandler.Sharable
public class NettyHttpClientHandler extends ChannelInboundHandlerAdapter {

    private final ConcurrentHashMap<String, NettyHttpClientHold> clientHoldMap = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        if(msg instanceof FullHttpResponse){
            FullHttpResponse response = (FullHttpResponse)msg;
            ByteBuf buf = response.content();
            try {
                String result = buf.toString(CharsetUtil.UTF_8);
                String channelId = ctx.channel().id().asLongText();
                if (clientHoldMap.containsKey(channelId)) {
                    NettyHttpClientHold nettyHttpClientHold = clientHoldMap.get(channelId);
                    nettyHttpClientHold.setResult(result);
                    synchronized (nettyHttpClientHold){
                        nettyHttpClientHold.notify();
                    }
                    clientHoldMap.remove(channelId);
                }else {
                    Channel channel = ctx.channel();
                    SocketAddress socketAddress = channel.remoteAddress();
                    CnaLogUtil.error(log,"CnaNettyHttpClientUtil 发送异常, channelId : {} , socketAddress : {} , result  :  {}", channelId , socketAddress, result);
                }
            } finally {
                buf.release();
            }
        }
    }
}
