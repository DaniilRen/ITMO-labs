package managers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import common.network.NetworkManager;


public class ClientNetworkManager implements NetworkManager {
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Socket socket;

    public ClientNetworkManager(String address, int port) {
        try {
            socket = new Socket(address, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {}

    }

    public void writeObject(Object object) throws IOException {
        oos.writeObject(object);
    }

    public Object read() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    public void close() throws IOException {
        ois.close();
        oos.close();
        socket.close();
    }
}
