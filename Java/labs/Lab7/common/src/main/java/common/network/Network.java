package common.network;

import java.io.IOException;

/**
 * Абстракция сетевого общения
 * @author Septyq
 */
public interface Network {
    void connect() throws IOException;
    void write(Object object) throws IOException;
    Object read() throws IOException, ClassNotFoundException;
    void close() throws IOException;
}
