package common.network;

import java.io.IOException;

public interface Network {
    void connect() throws IOException;
    void writeObject(Object object) throws IOException;
    Object read() throws IOException, ClassNotFoundException;
    void close() throws IOException;
}
