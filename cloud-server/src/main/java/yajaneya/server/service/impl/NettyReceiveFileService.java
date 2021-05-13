package yajaneya.server.service.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;
import yajaneya.server.service.ClientService;
import yajaneya.server.service.ServerService;
import yajaneya.server.service.impl.handler.*;

import java.io.File;

public class NettyReceiveFileService implements ServerService {

    private int port;
    ClientService clientService;
    File file;
    long lengthFile;

    public NettyReceiveFileService(int port, ClientService clientService, File file, long lengthFile) {
        this.port = port;
        this.clientService = clientService;
        this.file = file;
        this.lengthFile = lengthFile;
    }

    @Override
    public void startServer() {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new ChunkedWriteHandler(), new FilesWriterHandler(file, lengthFile, bossGroup, workerGroup, clientService));
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Сервер запущен.");
            clientService.writeCommandResult("readyReceiveFile");

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            System.out.println("Сервер упал.");
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("Сервер остановлен.");
        }
    }
}
