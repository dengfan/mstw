/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.netty;

import handling.MapleServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 *
 * @author Administrator
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private int channels;

    public ServerInitializer(int channels) {
        this.channels = channels;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipe = channel.pipeline();
        pipe.addLast("decoder", new MaplePacketDecoder());
        pipe.addLast("encoder", new MaplePacketEncoder());
        pipe.addLast("handler", new MapleServerHandler(channels));
    }

}
