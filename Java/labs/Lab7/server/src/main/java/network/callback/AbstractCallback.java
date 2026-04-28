package network.callback;

import network.handlers.ClientHandler;

public interface AbstractCallback {
    void onMessageReceived(ClientHandler client, Object message);
    void onClientConnected(ClientHandler client);
    void onClientDisconnected(ClientHandler client);
}