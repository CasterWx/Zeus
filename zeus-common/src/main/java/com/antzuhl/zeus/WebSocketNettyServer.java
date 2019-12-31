package com.antzuhl.zeus;

import com.antzuhl.zeus.handler.WebSocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebSocketNettyServer {

    public static void main(String[] args) {
        NioEventLoopGroup mainGrp = new NioEventLoopGroup();
        NioEventLoopGroup subGrp = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(mainGrp, subGrp)
                    // 指定通道类型
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new WebSocketChannelInitializer());
            //
            ChannelFuture future = serverBootstrap.bind(9090).sync();

            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
