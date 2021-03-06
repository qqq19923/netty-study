package com.jesper.netty.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by jiangyunxiong on 2018/6/21.
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;//转换为netty的ByteBuf
        byte[] req = new byte[buf.readableBytes()];//读字节数创建byte数组
        buf.readBytes(req);//将缓存区字节数组复制到byte数组
        String body = new String(req, "UTF-8");//构造请求消息
        System.out.println("The time server receive order : " + body);
        // 对请求消息进行判断
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);//异步发送应答消息给客户端
    }

    /**
     * 将消息发送队列中的消息写入到SocketChannel中发送给对方
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 异常时关闭
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

        ctx.close();
    }
}
