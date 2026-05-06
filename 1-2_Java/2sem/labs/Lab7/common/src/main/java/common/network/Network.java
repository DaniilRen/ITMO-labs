package common.network;

import java.io.IOException;

/**
 * Абстракция сетевого взаимодействия
 * @author Septyq
 */
public interface Network {
    void connect() throws IOException;
    void close() throws IOException;
}
