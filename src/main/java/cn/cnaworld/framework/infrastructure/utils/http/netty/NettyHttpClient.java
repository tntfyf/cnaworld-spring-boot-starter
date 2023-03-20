package cn.cnaworld.framework.infrastructure.utils.http.netty;


import cn.cnaworld.framework.infrastructure.common.statics.enums.RestFulBaseType;
import cn.cnaworld.framework.infrastructure.common.statics.enums.RestFulEntityType;
import cn.cnaworld.framework.infrastructure.utils.http.CnaHttpClientUtil;
import cn.cnaworld.framework.infrastructure.utils.log.CnaLogUtil;
import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import javax.net.ssl.SSLException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Lucifer
 * @date 2023/3/12
 * @since 1.0.0
 */
@Slf4j
public class NettyHttpClient {

    private static volatile Bootstrap bootstrapSsl = null;

    private static volatile Bootstrap bootstrap = null;

    private static final NettyHttpClientHandler HTTP_CLIENT_HANDLER = new NettyHttpClientHandler();

    protected String sendHttpRequestByNetty(String url , Object dto, Map<String,Object> paramsMap , Map<String,String> headerMap, RestFulEntityType restFulEntityType){
        NettyHttpClientHold nettyHttpClientHold;
        try {
            nettyHttpClientHold = initClientHold(url,paramsMap);
            CnaLogUtil.debug(log,"准备发送{}请求，url : {} , dto : {}, paramsMap : {} , headerMap : {}",restFulEntityType,url,dto,paramsMap,headerMap);
            sendHttpRequest(nettyHttpClientHold,dto,headerMap,restFulEntityType);
            String response = getResponse(url, nettyHttpClientHold);
            CnaLogUtil.debug(log,"{}请求发送完毕，url : {} , paramsMap : {} , headerMap : {} , response : {}", restFulEntityType, url, paramsMap, headerMap,response);
            return response;
        } catch (MalformedURLException | InterruptedException | URISyntaxException | UnknownHostException e) {
            CnaLogUtil.error(log,"CnaNettyHttpClientUtil 发送异常：{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    protected String sendHttpRequestByNetty(String url , Map<String,Object> paramsMap , Map<String,String> headerMap, RestFulBaseType restFulBaseType){
        NettyHttpClientHold nettyHttpClientHold;
        try {
            nettyHttpClientHold = initClientHold(url,paramsMap);
            CnaLogUtil.debug(log,"准备发送{}请求，url : {} , paramsMap : {} , headerMap : {}",restFulBaseType,url,paramsMap,headerMap);
            sendHttpRequest(nettyHttpClientHold,headerMap,restFulBaseType);
            String response = getResponse(url, nettyHttpClientHold);
            CnaLogUtil.debug(log,"{}请求发送完毕，url : {} , paramsMap : {} , headerMap : {} , response : {}", restFulBaseType, url, paramsMap, headerMap,response);
            return response;
        } catch (MalformedURLException | InterruptedException | URISyntaxException | UnknownHostException e) {
            CnaLogUtil.error(log,"CnaNettyHttpClientUtil 发送异常：{}",e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    private String getResponse(String url, NettyHttpClientHold nettyHttpClientHold) throws InterruptedException {
        long stime = System.currentTimeMillis();
        while (nettyHttpClientHold.getChannelPromise() == null) {
            if ( System.currentTimeMillis()-stime < 20000) {
                try {
                    TimeUnit.MILLISECONDS.sleep(1);
                } catch (InterruptedException e) {
                    CnaLogUtil.error(log,"请求超时，无法得到服务端响应，请重试 ： {}", url);
                    throw new RuntimeException("请求超时，无法得到服务端响应，请重试 ：" + url);
                }
            }else {
                CnaLogUtil.error(log,"请求超时，无法得到服务端响应，请重试 ： {}", url);
                throw new RuntimeException("请求超时，无法得到服务端响应，请重试 ：" + url);
            }
        }
        ChannelPromise promise = nettyHttpClientHold.getChannelPromise();
        promise.await(3, TimeUnit.SECONDS);
        return nettyHttpClientHold.getResult();
    }

    private void sendHttpRequest(NettyHttpClientHold nettyHttpClientHold, Map<String,String> headerMap, RestFulBaseType restFulBaseType) throws InterruptedException {
        processed(nettyHttpClientHold,null,headerMap,getHttpMethod(restFulBaseType),false);
    }

    private void sendHttpRequest(NettyHttpClientHold nettyHttpClientHold, Object dto , Map<String,String> headerMap, RestFulEntityType restFulEntityType) throws InterruptedException {
        processed(nettyHttpClientHold,dto,headerMap,getHttpMethod(restFulEntityType),true);
    }

    private void processed(NettyHttpClientHold nettyHttpClientHold, Object dto , Map<String,String> headerMap , HttpMethod httpMethod, boolean entityType) throws InterruptedException {
        ChannelFuture future = nettyHttpClientHold.getBootstrap().connect(nettyHttpClientHold.getInetAddress()).sync();
        Channel client = future.channel();
        handleClientHold(nettyHttpClientHold, client);
        FullHttpRequest request = getFullHttpRequest(nettyHttpClientHold, dto, headerMap, httpMethod, entityType);
        client.writeAndFlush(request).sync();
        client.closeFuture().sync();
    }

    private NettyHttpClientHold initClientHold(String urlStr, Map<String,Object> paramsMap) throws MalformedURLException, UnknownHostException, URISyntaxException {
        NettyHttpClientHold nettyHttpClientHold = new NettyHttpClientHold();
        InetSocketAddress inetAddress;
        boolean isSsl = urlStr.contains("https");
        URL url = new URL(urlStr);
        String host = url.getHost();
        InetAddress address = InetAddress.getByName(host);
        if (!host.equalsIgnoreCase(address.getHostAddress())) {
            //域名连接,https默认端口是443，http默认端口是80
            inetAddress = new InetSocketAddress(address, isSsl ? 443 : 80);
        } else {
            //ip+端口连接
            int port = url.getPort();
            inetAddress = InetSocketAddress.createUnresolved(host, port);
        }
        nettyHttpClientHold.setInetAddress(inetAddress);
        urlStr = CnaHttpClientUtil.processedUrl(urlStr,paramsMap);
        for(int i = 0; i < 3; i++){
            urlStr = urlStr.substring(urlStr.indexOf("/")+1);
        }
        URI uri = new URI("/"+urlStr);
        nettyHttpClientHold.setUri(uri);
        if (!isSsl) {
            if(bootstrap == null) {
                //防止并发创建多个对象
                synchronized (CnaNettyHttpClientUtil.class) {
                    //防止加锁之前并发的线程过了第一个判断导致出现多实例
                    if(bootstrap == null) {
                        bootstrap =  initBootstrap(false);
                    }
                }
            }
            nettyHttpClientHold.setBootstrap(bootstrap);
        }else {
            if(bootstrapSsl == null) {
                //防止并发创建多个对象
                synchronized (CnaNettyHttpClientUtil.class) {
                    //防止加锁之前并发的线程过了第一个判断导致出现多实例
                    if(bootstrapSsl == null) {
                        bootstrapSsl = initBootstrap(true);
                    }
                }
                nettyHttpClientHold.setBootstrap(bootstrapSsl);
            }
        }
        return nettyHttpClientHold;
    }

    private Bootstrap initBootstrap(boolean isSsl){
        NioEventLoopGroup group = new NioEventLoopGroup();
        return new Bootstrap().group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.ERROR))
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws SSLException {
                        SocketChannel socketChannel = (SocketChannel) channel;
                        socketChannel.config().setKeepAlive(true);
                        socketChannel.config().setTcpNoDelay(true);
                        if (isSsl) {
                            SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                            channel.pipeline().addLast(context.newHandler(channel.alloc()));
                        }
                        channel.pipeline().addLast(new HttpClientCodec());
                        channel.pipeline().addLast(new HttpObjectAggregator(65536));
                        channel.pipeline().addLast(new HttpContentDecompressor());
                        channel.pipeline().addLast(NettyHttpClient.HTTP_CLIENT_HANDLER);
                    }
                });
    }

    private FullHttpRequest getFullHttpRequest(NettyHttpClientHold nettyHttpClientHold, Object dto, Map<String, String> headerMap, HttpMethod httpMethod, boolean entityType) {
        FullHttpRequest request;
        if (entityType && ObjectUtils.isNotEmpty(dto)) {
            ByteBuf byteBuf = Unpooled.wrappedBuffer(JSON.toJSONString(dto).getBytes());
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, httpMethod, nettyHttpClientHold.getUri().toASCIIString(),byteBuf);
        }else {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, httpMethod, nettyHttpClientHold.getUri().toASCIIString());
        }
        request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        request.headers().add(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
        request.headers().add(HttpHeaderNames.CONTENT_TYPE,"application/json");
        if(headerMap != null && headerMap.size()>0) {
            for(Map.Entry<String,String> entry : headerMap.entrySet()){
                request.headers().add(entry.getKey(),entry.getValue());
            }
        }
        return request;
    }

    private void handleClientHold(NettyHttpClientHold nettyHttpClientHold, Channel channel) {
        String channelId = channel.id().asLongText();
        nettyHttpClientHold.setChannelId(channelId);
        nettyHttpClientHold.setBootstrap(null);
        HTTP_CLIENT_HANDLER.getClientHoldMap().put(channelId, nettyHttpClientHold);
    }

    private HttpMethod getHttpMethod(RestFulBaseType restFulBaseType){
        HttpMethod httpMethod;
        switch (restFulBaseType) {
            case DELETE:
                httpMethod= HttpMethod.DELETE;
                break;
            case GET:
            default:
                httpMethod= HttpMethod.GET;
                break;
        }
        return httpMethod;
    }

    private HttpMethod getHttpMethod(RestFulEntityType restFulEntityType){
        HttpMethod httpMethod;
        switch (restFulEntityType) {
            case PUT:
                httpMethod= HttpMethod.PUT;
                break;
            case PATCH:
                httpMethod= HttpMethod.PATCH;
                break;
            case POST:
            default:
                httpMethod= HttpMethod.POST;
                break;
        }
        return httpMethod;
    }

}
