package ch.fhnw.energiesammler;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AppConfig {
  // Private constructor to prevent instantiation
  private AppConfig() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  @SuppressWarnings("unchecked")
  public static <T> T getValue(String key, T defaultValue) {
    String value;
    try {
      ResourceBundle resourceBundle = ResourceBundle.getBundle("app");
      value = resourceBundle.getString(key);
    } catch (MissingResourceException e) {
      System.err.println("Used default value and couldn't find or load app.properties");
      return defaultValue;
    }

    Class<?> defaultValueClass = defaultValue.getClass();
    if (defaultValueClass == String.class) {
      return (T) value;
    } else if (defaultValueClass == Integer.class) {
      try {
        return (T) Integer.valueOf(value);
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    } else if (defaultValueClass == Boolean.class) {
      return (T) Boolean.valueOf(value);
    } else if (defaultValueClass == Long.class) {
      try {
        return (T) Long.valueOf(value);
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    } else if (defaultValueClass == Double.class) {
      try {
        return (T) Double.valueOf(value);
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    } else if (defaultValueClass == Float.class) {
      try {
        return (T) Float.valueOf(value);
      } catch (NumberFormatException e) {
        return defaultValue;
      }
    } else {
      throw new UnsupportedOperationException("Unsupported type: " + defaultValueClass.getName());
    }
  }
}
