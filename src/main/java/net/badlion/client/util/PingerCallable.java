package net.badlion.client.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import net.minecraft.client.multiplayer.ServerAddress;

public class PingerCallable
{
    private SocketAddress address;
    private String rawAddress;

    public PingerCallable(String address)
    {
        ServerAddress serveraddress = ServerAddress.func_78860_a(address);
        this.address = new InetSocketAddress(serveraddress.getIP(), serveraddress.getPort());
        this.rawAddress = address;
    }

    public String getRawAddress()
    {
        return this.rawAddress;
    }

    public SocketAddress getAddress()
    {
        return this.address;
    }

    public Long getPing()
    {
        try (Socket socket = new Socket())
        {
            long i = System.currentTimeMillis();
            socket.connect(this.address, 1000);
            return Long.valueOf(System.currentTimeMillis() - i);
        }
        catch (IOException ioexception)
        {
            return Long.valueOf(-1L);
        }
    }
}
