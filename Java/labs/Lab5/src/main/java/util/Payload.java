package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный класс для запросов и ответов.
 * @author Septyq
 */
public abstract class Payload<T> {
    protected List<T> body = new ArrayList<>();

    public void setBody(List<T> body) {
        this.body = body;
    }

    public List<T> getBody() {
        return this.body;
    };
}
