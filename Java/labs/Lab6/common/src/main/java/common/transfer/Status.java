package common.transfer;

import java.io.Serializable;

/**
 * Статусы запросов, ответов.
 * @author Septyq
 */
public enum Status implements Serializable {
    OK,
    ERROR,
    EXIT;
}
