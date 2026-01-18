package net.runelite.rs.api;

import net.runelite.mapping.Import;

import java.net.Socket;

public interface RSBufferedNetSocket extends RSAbstractSocket {
    @Import("socket")
    Socket getSocket();

    @Import("socket")
    void setSocket(Socket socket);

}
