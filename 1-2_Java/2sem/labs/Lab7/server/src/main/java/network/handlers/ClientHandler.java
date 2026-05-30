package network.handlers;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import network.MultiThreadNetwork;
import logging.LoggingManager;

public class ClientHandler {
    private final Socket clientSocket;
    private final LoggingManager logger;
    private final MultiThreadNetwork server;
    private final ExecutorService commonPool;
    private final ExecutorService processingPool;
    private final ExecutorService sendingPool;
    
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private volatile boolean connected;
    private CompletableFuture<Void> readLoop;

    public ClientHandler(Socket clientSocket, 
                         LoggingManager logger, 
                         MultiThreadNetwork server,
                         ExecutorService commonPool,
                         ExecutorService processingPool) {
        this.clientSocket = clientSocket;
        this.logger = logger;
        this.server = server;
        this.commonPool = commonPool;
        this.processingPool = processingPool;
        this.sendingPool = Executors.newCachedThreadPool();
        this.connected = true;
    }

    private void init() throws IOException {
        oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.flush();
        ois = new ObjectInputStream(clientSocket.getInputStream());
    }

    public void startLoop() {
        readLoop = readAndProcess();
    }
    
    private CompletableFuture<Void> readAndProcess() {
        if (!connected) {
            return CompletableFuture.completedFuture(null);
        }
        
        return CompletableFuture
            .supplyAsync(this::readObject, commonPool)
            .thenCompose(this::handleResult)
            .thenCompose(ignored -> readAndProcess());
    }
    
    private CompletableFuture<Void> handleResult(Object received) {
        if (received == null) {
            return CompletableFuture.completedFuture(null);
        }
        
        return CompletableFuture
            .runAsync(() -> processMessage(received), processingPool);
    }
    
    private Object readObject() {
        try {
            if (ois == null) {
                init();
            }
            return ois.readObject();
        } catch (EOFException | SocketException e) {
            logger.info("Client disconnected: " + clientSocket.getInetAddress());
            connected = false;
            return null;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Read error: " + e.getMessage());
            connected = false;
            return null;
        }
    }
    
    private void processMessage(Object received) {
        if (server.getMessageCallback() != null) {
            server.getMessageCallback().onMessageReceived(this, received);
        }
    }

    public void write(Object object) throws IOException {
        if (!connected || oos == null) {
            throw new IOException("Not connected to client");
        }
        
        CompletableFuture.runAsync(() -> {
            synchronized (oos) {
                try {
                    oos.writeObject(object);
                    oos.flush();
                } catch (IOException e) {
                    logger.error("Failed to send: " + e.getMessage());
                    connected = false;
                }
            }
        }, sendingPool);
    }

    public void close() {
        connected = false;
        
        if (readLoop != null) {
            readLoop.cancel(true);
        }
        
        try {
            if (ois != null) ois.close();
            if (oos != null) oos.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            logger.error("Error closing: ", e);
        }
        
        sendingPool.shutdown();
        server.removeClient(clientSocket);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public boolean isConnected() {
        return connected && !clientSocket.isClosed();
    }
}