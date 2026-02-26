package util.forms;

import util.exceptions.InvalidFormException;

public abstract class Form<T> {
  public abstract T build() throws InvalidFormException;
}