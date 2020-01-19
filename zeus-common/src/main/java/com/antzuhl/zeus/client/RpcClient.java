package com.antzuhl.zeus.client;

import com.alibaba.fastjson.JSONArray;
import com.antzuhl.zeus.entity.request.RpcRequest;
import com.antzuhl.zeus.entity.response.RpcResponse;
import com.antzuhl.zeus.handler.NettyClientHandler;
import com.antzuhl.zeus.utils.JSONDecoder;
import com.antzuhl.zeus.utils.JSONEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.SynchronousQueue;

@Component
@Slf4j
public class RpcClient {

    private static RpcClient rpcClient;

    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private EventLoopGroup group = new NioEventLoopGroup(1);

    private Bootstrap bootstrap = new Bootstrap();

    private RpcClient() {
        bootstrap.group(group).
                channel(NioSocketChannel.class).
                option(ChannelOption.TCP_NODELAY, true).
                option(ChannelOption.SO_KEEPALIVE,true).
                handler(new ChannelInitializer<SocketChannel>() {
                    //创建NIOSocketChannel成功后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络IO事件
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new IdleStateHandler(0, 0, 30));
                        pipeline.addLast(new JSONEncoder());
                        pipeline.addLast(new JSONDecoder());
                        pipeline.addLast("handler",new NettyClientHandler());
                    }
                });
//        Object o  = send(new RpcRequest("1", "com.antzuhl.zeusdemo2.service.impl.DoSomething", "doHello", null, null));
    }

    public static RpcClient getInstance() {
        if (rpcClient != null) {
            return rpcClient;
        }
        rpcClient = new RpcClient();
        return rpcClient;
    }

    public void doConnect(String addr, Integer port) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(addr,port);
        Channel channel = future.sync().channel();
        channels.add(channel);
    }

    public void doConnect(String addr) throws InterruptedException {
        ChannelFuture future = bootstrap.connect(addr,18868);
        Channel channel = future.sync().channel();
        channels.add(channel);
    }


    @PreDestroy
    public void destroy(){
        group.shutdownGracefully();
    }

    public Object send(RpcRequest request) throws InterruptedException{

        Channel channel = null;
        for (Channel ch: channels) {
            channel = ch;
        }

        if (channel != null && channel.isActive()) {
            SynchronousQueue<Object> queue = new NettyClientHandler().sendRequest(request,channel);
            Object result = queue.take();
            return JSONArray.toJSONString(result);
        } else {
            RpcResponse res = new RpcResponse();
            res.setCode(1);
            res.setError_msg("未正确连接到服务器.请检查相关配置信息!");
            return JSONArray.toJSONString(res);
        }
    }

}
