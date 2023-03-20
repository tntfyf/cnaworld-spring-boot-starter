package cn.cnaworld.framework.infrastructure.utils.http.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.net.InetSocketAddress;
import java.net.URI;

/**
 * @author Lucifer
 * @date 2023/3/14
 * @since 1.0.0
 */
@Setter
@Getter
@ToString
public class NettyHttpClientHold {

    private InetSocketAddress inetAddress;

    private String channelId;

    private URI uri;

    private Bootstrap bootstrap;

    private String result;

    private ChannelPromise channelPromise;

}
