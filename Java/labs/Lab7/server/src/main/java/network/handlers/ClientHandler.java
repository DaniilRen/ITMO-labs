package network.handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import network.MultiThreadNetwork;
import util.logging.AbstractLogger;


public class ClientHandler implements Runnable {
        private final Socket clientSocket;
        private final AbstractLogger logger;
        private final MultiThreadNetwork server;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private volatile boolean connected;

        public ClientHandler(Socket clientSocket, AbstractLogger logger, MultiThreadNetwork server) {
            this.clientSocket = clientSocket;
            this.logger = logger;
            this.server = server;
            this.connected = true;
        }

        @Override
        public void run() {
            try {
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(clientSocket.getInputStream());
                
                while (connected && !Thread.currentThread().isInterrupted()) {
                    try {
                        Object received = ois.readObject();
                        
                        if (server.getMessageCallback() != null) {
                            server.getMessageCallback().onMessageReceived(this, received);
                        }
                    } catch (SocketException e) {
                        logger.info("Client disconnected: " + clientSocket.getInetAddress());
                        break;
                    } catch (ClassNotFoundException e) {
                        logger.error("Unknown object type from " + clientSocket.getInetAddress(), e);
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
            synchronized (oos) {
                oos.writeObject(object);
                oos.flush();
            }
        }

        public Object read() throws IOException, ClassNotFoundException {
            if (!connected || ois == null) {
                throw new IOException("Not connected to client");
            }
            return ois.readObject();
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