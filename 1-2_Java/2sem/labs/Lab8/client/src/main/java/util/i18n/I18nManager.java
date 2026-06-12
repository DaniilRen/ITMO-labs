package util.i18n;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public final class I18nManager {
  public static final Locale RU = Locale.forLanguageTag("ru-RU");
  public static final Locale SK = Locale.forLanguageTag("sk-SK");
  public static final Locale IT = Locale.forLanguageTag("it-IT");
  public static final Locale ES_DO = Locale.forLanguageTag("es-DO");

  private static final I18nManager INSTANCE = new I18nManager();

  private final List<Consumer<Locale>> listeners = new ArrayList<>();
  private Locale locale = RU;
  private ResourceBundle bundle = loadBundle(RU);

  private I18nManager() {}

  public static I18nManager get() {
    return INSTANCE;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale newLocale) {
    this.locale = newLocale;
    this.bundle = loadBundle(newLocale);
    listeners.forEach(listener -> listener.accept(newLocale));
  }

  public String get(String key) {
    try {
      return bundle.getString(key);
    } catch (MissingResourceException e) {
      return key;
    }
  }

  public String format(String key, Object... args) {
    return String.format(locale, get(key), args);
  }

  public NumberFormat numberFormat() {
    return NumberFormat.getNumberInstance(locale);
  }

  public DateFormat dateFormat() {
    return DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
  }

  public DateTimeFormatter dateTimeFormatter() {
    return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
  }

  public void addListener(Consumer<Locale> listener) {
    listeners.add(listener);
  }

  public static Locale[] supportedLocales() {
    return new Locale[] {RU, SK, IT, ES_DO};
  }

  public static String localeLabel(Locale locale) {
    if (locale.equals(RU)) return "Русский";
    if (locale.equals(SK)) return "Slovenčina";
    if (locale.equals(IT)) return "Italiano";
    if (locale.equals(ES_DO)) return "Español (DO)";
    return locale.getDisplayName(locale);
  }

  private ResourceBundle loadBundle(Locale loc) {
    return ResourceBundle.getBundle("i18n.messages", loc);
  }
}
