package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import common.network.Network;

public class ServerNetwork implements Network {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private final int port;
    private boolean connected = false;

    public ServerNetwork(int port) {
        this.port = port;
    }

    public void connect() throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            System.out.println("Waiting for client connection...");
            
            clientSocket = serverSocket.accept();
            System.out.println("Client connected from " + clientSocket.getInetAddress());
            
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.flush();
            
            ois = new ObjectInputStream(clientSocket.getInputStream());
            
            connected = true;
            System.out.println("Streams initialized successfully");
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
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
        try {
            return ois.readObject();
        } catch (SocketException e) {
            System.err.println("Client disconnected: " + e.getMessage());
            connected = false;
            throw e;
        }
    }

    public void close() throws IOException {
        connected = false;
        if (ois != null) {
            try { ois.close(); } catch (IOException e) {}
        }
        if (oos != null) {
            try { oos.close(); } catch (IOException e) {}
        }
        if (clientSocket != null) {
            try { clientSocket.close(); } catch (IOException e) {}
        }
        if (serverSocket != null) {
            try { serverSocket.close(); } catch (IOException e) {}
        }
    }
    
    public boolean isConnected() {
        return connected && clientSocket != null && !clientSocket.isClosed();
    }
}