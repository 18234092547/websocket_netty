package com.xyhj;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import net.RedisUtil;


public final class WebSocketServer {

    private static final int PORT = 8083;

    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .option(ChannelOption.SO_SNDBUF,1024)
                    .option(ChannelOption.SO_RCVBUF,1024)
                    .option(ChannelOption.SO_KEEPALIVE,true)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.ERROR))
                    .childHandler(new WebSocketServerInitializer());

            Channel ch = b.bind(PORT).sync().channel();
            RedisUtil.StartRedis();
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
