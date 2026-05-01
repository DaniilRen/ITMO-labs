package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

import common.network.Network;
import network.callback.Callback;
import network.handlers.ClientHandler;
import logging.LoggingManager;

/**
 * Сетевое взаимодействие сервера в режиме мультипоточности
 * @author Septyq
 */
public class MultiThreadNetwork implements Network {
    private ServerSocket serverSocket;
    private final int port;
    private final LoggingManager logger;
    private volatile boolean isRunning;
    private ForkJoinPool readingPool;
    private ExecutorService processingPool;
    private final Map<Socket, ClientHandler> activeClients;
    private Callback messageCallback;

    public MultiThreadNetwork(int port, LoggingManager logger) {
        this(port, logger, 10);
    }

    public MultiThreadNetwork(int port, LoggingManager logger, int maxThreads) {
        this.port = port;
        this.logger = logger;
        this.activeClients = new ConcurrentHashMap<>();
        this.readingPool = new ForkJoinPool();
        this.processingPool = Executors.newFixedThreadPool(maxThreads);
        this.isRunning = false;
    }

    public void setMessageCallback(Callback callback) {
        this.messageCallback = callback;
    }

    public Callback getMessageCallback() {
        return messageCallback;
    }

    public ExecutorService getProcessingPool() {
        return processingPool;
    }

    @Override
    public void connect() throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            isRunning = true;
            
            logger.info("started on port " + port);
            
            Thread acceptorThread = new Thread(() -> {
                while (isRunning) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        logger.info("Client connected: " + clientSocket.getInetAddress());
                        
                        ClientHandler handler = new ClientHandler(clientSocket, logger, this);
                        activeClients.put(clientSocket, handler);
                        
                        if (messageCallback != null) {
                            messageCallback.onClientConnected(handler);
                        }
                        readingPool.submit(handler);
                        
                    } catch (IOException e) {
                        if (isRunning) {
                            logger.error("Error accepting connection: ", e);
                        }
                    }
                }
            });
            acceptorThread.start();
            
        } catch (IOException e) {
            logger.error("connection error: ", e);
            throw e;
        }
    }

    public void write(Object object) throws IOException {
        for (ClientHandler handler : activeClients.values()) {
            try {
                handler.write(object);
            } catch (IOException e) {
                logger.error("Failed to send to client: " + e.getMessage());
            }
        }
    }

    @Override
    public void close() throws IOException {
        isRunning = false;
        
        for (ClientHandler handler : activeClients.values()) {
            handler.close();
        }
        activeClients.clear();
        
        if (processingPool != null) {
            processingPool.shutdown();
        }
        
        if (readingPool != null) {
            readingPool.shutdown();
        }
        
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
        
        logger.info("Server stopped");
    }

    public void removeClient(Socket clientSocket) {
        ClientHandler handler = activeClients.remove(clientSocket);
        if (handler != null && messageCallback != null) {
            messageCallback.onClientDisconnected(handler);
        }
    }
}