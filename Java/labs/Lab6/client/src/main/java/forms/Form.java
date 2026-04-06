package forms;

import common.exceptions.InvalidFormException;


/**
 * Абстрактный класс формы для ввода пользовательских данных.
 * @param <T> создаваемый объект
 * @author Septyq
 */
public abstract class Form<T> {
  public abstract T build() throws InvalidFormException;
}