package model.network;


import java.io.IOException;

import view.View;

public abstract class AbstractClientNetwork {
    protected final String address;
    protected final int port;
    protected boolean connected = false;
    protected final View view;

    public AbstractClientNetwork(String address, int port, View view) {
        this.address = address;
        this.port = port;
        this.view = view;
    }

    abstract public void connect() throws IOException;

    abstract public void write(Object object) throws IOException;

    abstract public Object read() throws IOException, ClassNotFoundException;

    abstract public boolean isConnected();

    abstract public void close();
}
