package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import console.IOConsole;

public class ClientNetwork extends AbstractClientNetwork {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;
    
    private final int MAX_RETRIES = 3;
    private final int RETRY_DELAY_MS = 2000;
    private final int CONNECTION_TIMEOUT_MS = 5000;

    public ClientNetwork(String address, int port, IOConsole console) {
        super(address, port, console);
    }

    public void connect() throws IOException {
        int attempts = 0;
        int delay = RETRY_DELAY_MS;
        
        while (attempts < MAX_RETRIES) {
            try {
                socket = new Socket();
                socket.connect(new java.net.InetSocketAddress(address, port), CONNECTION_TIMEOUT_MS);
                oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(socket.getInputStream());
                connected = true;
                return;
            } catch (ConnectException e) {
                attempts++;
                console.printConnectionError("connection refused (attempt " + attempts + "/" + MAX_RETRIES + ")");
                if (attempts >= MAX_RETRIES) {
                    throw new IOException("Cannot connect to server after " + MAX_RETRIES + " attempts", e);
                }
                try { Thread.sleep(delay); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                delay = Math.min(delay * 2, 30000);
            } catch (IOException e) {
                attempts++;
                console.printConnectionError("Connection failed (attempt " + attempts + "/" + MAX_RETRIES + "): " + e.getMessage());
                if (attempts >= MAX_RETRIES) {
                    throw new IOException("Cannot connect to server after " + MAX_RETRIES + " attempts", e);
                }
                try { Thread.sleep(delay); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                delay = Math.min(delay * 2, 30000);
            }
        }
    }

    public void write(Object object) throws IOException {
        checkConnection();
        try {
            oos.writeObject(object);
            oos.flush();
        } catch (SocketException e) {
            connected = false;
            throw new IOException("Connection lost: " + e.getMessage(), e);
        }
    }

    public Object read() throws IOException, ClassNotFoundException {
        checkConnection();
        try {
            return ois.readObject();
        } catch (SocketException e) {
            connected = false;
            throw new IOException("Connection lost: " + e.getMessage(), e);
        }
    }

    public boolean isConnected() {
        if (!connected) return false;
        if (socket == null || socket.isClosed()) return false;
        if (oos == null || ois == null) return false;
        
        try {
            socket.getOutputStream().flush();
        } catch (IOException e) {
            connected = false;
            return false;
        }
        
        return true;
    }
    
    private void checkConnection() throws IOException {
        if (!isConnected()) {
            connected = false;
            throw new IOException("Not connected to server");
        }
    }

    public void close() {
        connected = false;
        try { if (ois != null) ois.close(); } catch (IOException e) {}
        try { if (oos != null) oos.close(); } catch (IOException e) {}
        try { if (socket != null) socket.close(); } catch (IOException e) {}
    }
}