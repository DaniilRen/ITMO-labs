package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.network.Network;
import network.callback.AbstractCallback;
import network.handlers.ClientHandler;
import util.logging.AbstractLogger;

public class MultiThreadNetwork implements Network {
    private ServerSocket serverSocket;
    private final int port;
    private final AbstractLogger logger;
    private volatile boolean isRunning;
    private ExecutorService clientHandlerPool;
    private final int maxThreads;
    private final Map<Socket, ClientHandler> activeClients;
    private AbstractCallback messageCallback;

    public MultiThreadNetwork(int port, AbstractLogger logger) {
        this(port, logger, 10);
    }

    public MultiThreadNetwork(int port, AbstractLogger logger, int maxThreads) {
        this.port = port;
        this.logger = logger;
        this.maxThreads = maxThreads;
        this.activeClients = new ConcurrentHashMap<>();
        this.isRunning = false;
    }

    public void setMessageCallback(AbstractCallback callback) {
        this.messageCallback = callback;
    }

    public AbstractCallback getMessageCallback() {
        return messageCallback;
    }

    public void connect() throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            clientHandlerPool = Executors.newFixedThreadPool(maxThreads);
            isRunning = true;
            
            logger.info("Started on port " + port);
            
            clientHandlerPool.submit(() -> {
                while (isRunning) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        logger.info("Client connected: " + clientSocket.getInetAddress());
                        
                        ClientHandler handler = new ClientHandler(clientSocket, logger, this);
                        activeClients.put(clientSocket, handler);
                        
                        if (messageCallback != null) {
                            messageCallback.onClientConnected(handler);
                        }
                        
                        clientHandlerPool.submit(handler);
                        
                    } catch (IOException e) {
                        if (isRunning) {
                            logger.error("Error accepting connection: ", e);
                        }
                    }
                }
            });
            
        } catch (IOException e) {
            logger.error("Server startup error: ", e);
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


 

    public void close() throws IOException {
        isRunning = false;
        
        for (ClientHandler handler: activeClients.values()) {
            handler.close();
        }
        activeClients.clear();
        
        if (clientHandlerPool != null) {
            clientHandlerPool.shutdown();
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