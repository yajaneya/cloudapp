package yajaneya.client.service.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.File;
import java.io.IOException;

public class NettySendFileService {

    private SocketChannel channel;

    private static final String HOST = "localhost";
    private int port;
    private boolean isReady;

    private NioEventLoopGroup workerGroup;

    public NettySendFileService(int port) {
        this.port = port;
    }


    public void start() {
        Thread t = new Thread(() -> {
            workerGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workerGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(
                                        new ChunkedWriteHandler()
                                );
                            }
                        });
                ChannelFuture future = b.connect(HOST, port).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
            }
        });
        t.start();
    }

    public void sendFile (File file) {

        ChannelFuture future = null;

        try {
            future = channel.writeAndFlush(new ChunkedFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        future.addListener((ChannelFutureListener) channelFuture -> {

        });
    }

    public void stop() {
        workerGroup.shutdownGracefully();
    }
}
