package network;


import java.io.IOException;

import console.IOConsole;

public abstract class AbstractClientNetwork {
    protected final String address;
    protected final int port;
    protected boolean connected = false;
    protected final IOConsole console;

    public AbstractClientNetwork(String address, int port, IOConsole console) {
        this.address = address;
        this.port = port;
        this.console = console;
    }

    abstract public void connect() throws IOException;

    abstract public void write(Object object) throws IOException;

    abstract public Object read() throws IOException, ClassNotFoundException;

    abstract public boolean isConnected();

    abstract public void close();
}
