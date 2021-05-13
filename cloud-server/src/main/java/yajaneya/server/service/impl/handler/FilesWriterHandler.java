package yajaneya.server.service.impl.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import yajaneya.server.service.ClientService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class FilesWriterHandler extends ChannelInboundHandlerAdapter {

    private File file;
    private long lengthFile;
    private EventLoopGroup b, w;
    ClientService clientService;


    public FilesWriterHandler(File file, long lengthFile, EventLoopGroup b, EventLoopGroup w, ClientService clientService) {
        this.file = file;
        this.lengthFile = lengthFile;
        this.b = b;
        this.w = w;
        this.clientService = clientService;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object chunkedFile){
        ByteBuf byteBuf = (ByteBuf) chunkedFile;

            try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file, true))) {
                while (byteBuf.isReadable()) {
                    os.write(byteBuf.readByte());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            byteBuf.release();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        if (file.length() == lengthFile) {
            b.shutdownGracefully();
            w.shutdownGracefully();
            System.out.println("Сервер получения файла остановлен");
            clientService.writeCommandResult("endReceiveFile");
        }
    }
}
