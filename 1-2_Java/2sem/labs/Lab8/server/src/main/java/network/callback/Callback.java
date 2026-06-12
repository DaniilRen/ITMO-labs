package network.callback;

import network.handlers.ClientHandler;

public interface Callback {
    void onMessageReceived(ClientHandler client, Object message);
    void onClientConnected(ClientHandler client);
    void onClientDisconnected(ClientHandler client);
}