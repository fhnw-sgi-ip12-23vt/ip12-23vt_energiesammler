package ch.fhnw.energiesammler.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Util Klasse um Sprach-Dateien im Resources Ordner zu behandeln.
 */
public class TextEmLocale {
  private static final String BASE_NAME = "em";
  private String language;

  /**
   * TextLocale Konstruktor.
   *
   * @param language Sprachkürzel um den Text zu laden.
   */
  public TextEmLocale(String language) {
    this.language = language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Übergibt den Text für den angegebenen Schlüssel in der ausgewählten Sprache.
   *
   * @param key Der Schlüssel des gewünschten Textes.
   * @return Der Text in der ausgewählten Sprache.
   */
  public String getText(String key) {
    ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, new Locale(language));
    return bundle.getString(key);
  }
}
