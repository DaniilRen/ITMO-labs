package network.handlers;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.RecursiveAction;

import network.MultiThreadNetwork;
import logging.LoggingManager;

public class ClientHandler extends RecursiveAction {
    private final Socket clientSocket;
    private final LoggingManager logger;
    private final MultiThreadNetwork server;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private volatile boolean connected;

    public ClientHandler(Socket clientSocket, LoggingManager logger, MultiThreadNetwork server) {
        this.clientSocket = clientSocket;
        this.logger = logger;
        this.server = server;
        this.connected = true;
    }

    public void init() throws IOException {
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    protected void compute() {
        try {
            init();
            
            while (connected && !Thread.currentThread().isInterrupted()) {
                try {
                    Object received = ois.readObject();
                    
                    if (received != null && server.getProcessingPool() != null) {
                        server.getProcessingPool().submit(() -> {
                            if (server.getMessageCallback() != null) {
                                server.getMessageCallback().onMessageReceived(this, received);
                            }
                        });
                    }
                } catch (EOFException e) {
                    logger.info("Client disconnected (EOF): " + clientSocket.getInetAddress());
                    break;
                } catch (SocketException e) {
                    logger.info("Client disconnected: " + clientSocket.getInetAddress());
                    break;
                } catch (ClassNotFoundException e) {
                    logger.error("Unknown object type from " + clientSocket.getInetAddress(), e);
                } catch (IOException e) {
                    logger.error("IO Error reading from " + clientSocket.getInetAddress() + ": " + e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            if (connected) {
                logger.error("Error in client handler: ", e);
            }
        } finally {
            close();
            server.removeClient(clientSocket);
        }
    }

    public void write(Object object) throws IOException {
        if (!connected || oos == null) {
            throw new IOException("Not connected to client");
        }
        
        Thread sendThread = new Thread(() -> {
            synchronized (oos) {
                try {
                    oos.writeObject(object);
                    oos.flush();
                } catch (IOException e) {
                    logger.error("Failed to send response: " + e.getMessage());
                    connected = false;
                }
            }
        });
        sendThread.start();
    }

    public void close() {
        connected = false;
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            logger.error("Error closing client connection: ", e);
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public boolean isConnected() {
        return connected && !clientSocket.isClosed();
    }
}