package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import common.network.Network;
import logging.LoggerBlueprint;


public class ServerNetwork implements Network {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket clientSocket;
    private ServerSocket serverSocket;
    private final int port;
    private boolean connectedToClient = false;
		private final LoggerBlueprint logger;

    public ServerNetwork(int port, LoggerBlueprint logger) {
        this.port = port;
        this.logger = logger;
    }

    public void connect() throws IOException {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("waiting for connection...");
            
            clientSocket = serverSocket.accept();
            logger.info("new connection: " + clientSocket.getInetAddress());
            
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.flush();
            
            ois = new ObjectInputStream(clientSocket.getInputStream());
            
            connectedToClient = true;
            
        } catch (IOException e) {
            logger.error("connection error: ", e);
            throw e;
        }
    }

    public void write(Object object) throws IOException {
        if (!connectedToClient || oos == null) {
            throw new IOException("Not connected");
        }
        oos.writeObject(object);
        oos.flush();
    }

    public Object read() throws IOException, ClassNotFoundException {
        if (!connectedToClient || ois == null) {
            throw new IOException("Not connected");
        }
        try {
            return ois.readObject();
        } catch (SocketException e) {
            logger.error("client disconnected");
            connectedToClient = false;
            throw e;
        }
    }

    public void close() throws IOException {
        connectedToClient = false;
        ois.close();
        oos.close();
        serverSocket.close();
    }
}