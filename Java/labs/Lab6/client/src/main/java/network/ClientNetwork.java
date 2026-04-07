package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.network.Network;

public class ClientNetwork implements Network {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;
    private final String address;
    private final int port;
    private boolean connected = false;

    public ClientNetwork(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connect() throws IOException {
        try {
            socket = new Socket(address, port);
            System.out.println("Connected to server");
            
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            
            ois = new ObjectInputStream(socket.getInputStream());
            
            connected = true;            
        } catch (IOException e) {
            System.err.println("Connection failed: " + e.getMessage());
            throw e;
        }
    }

    public void writeObject(Object object) throws IOException {
        if (!connected || oos == null) {
            throw new IOException("Not connected");
        }
        oos.writeObject(object);
        oos.flush();
    }

    public Object read() throws IOException, ClassNotFoundException {
        if (!connected || ois == null) {
            throw new IOException("Not connected");
        }
        Object obj = ois.readObject();
        return obj;
    }

    public void close() throws IOException {
        connected = false;
        if (ois != null) {
            try { ois.close(); } catch (IOException e) {}
        }
        if (oos != null) {
            try { oos.close(); } catch (IOException e) {}
        }
        if (socket != null) {
            try { socket.close(); } catch (IOException e) {}
        }
    }
}