package ch.fhnw.energiesammler.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Util Klasse um Sprach-Dateien in Resources Ordner zu behandeln.
 */
public class TaskLocaleUtil {
  private static final String BASE_NAME = "task";

  /**
   * No Object, no Constructor
   */
  private TaskLocaleUtil() {

  }

  /**
   * Übergibt alle Task Daten in der ausgewählten Sprache zurück als Map.
   *
   * @param language Sprachkürzel um Task Text zu laden.
   * @param maxDoors Anzahl Türen im Spiel
   * @return Übergibt die Beschreibung und Stromverbrauch Wert der Task.
   */
  public static Map<String, Float> initializeTaskMap(String language, int maxDoors) {
    Map<String, Float> taskMap = new HashMap<>();

    ResourceBundle valueBundle = ResourceBundle.getBundle(BASE_NAME, Locale.ROOT);
    ResourceBundle descriptionBundle = ResourceBundle.getBundle(BASE_NAME, new Locale(language));
    Enumeration<String> bundleKeys = descriptionBundle.getKeys();

    List<String> bundleKeyList = Collections.list(bundleKeys);
    List<String> usedBundleKeys = new ArrayList<>();
    Random random = new Random();

    int i = 0;
    // Das ganze Set mit den Keys (task1 ...) wird in einer Liste gespeichert.
    // Danach generieren wir soviel mal eine Random Zahl, wie es maxDoors im Spiel gibt.
    // Diese bezieht Random Tasks.
    while (i < maxDoors) {
      String randomKey = bundleKeyList.get(random.nextInt(bundleKeyList.size()));
      if (!usedBundleKeys.contains(randomKey)) {
        String description = descriptionBundle.getString(randomKey);
        String value = valueBundle.getString(randomKey);
        usedBundleKeys.add(randomKey);
        taskMap.put(description, Float.valueOf(value));
        i++;
      }
    }
    return taskMap;
  }

  /**
   * Findet alle verfügbaren Sprachen des baseName im Ordner Resources.
   *
   * @param baseName Filtern der ResourceBundle
   * @param currentLanguage Aktuelle definierte Spiel Sprache.
   * @return Key: Sprach Kürzel/Code Value: Sprache in aktuelle Spiel Sprache ausgeschrieben.
   */

  public static Map<String, String> getResourceBundles(String baseName, String currentLanguage) {
    Set<ResourceBundle> resourceBundles = new HashSet<>();

    for (Locale locale : Locale.getAvailableLocales()) {
      try {
        resourceBundles.add(ResourceBundle.getBundle(baseName, locale));
      } catch (MissingResourceException ex) {
        System.err.println("Couldn't access any " + baseName + " in Resource dir");
      }
    }

    Map<String, String> languageMap = new HashMap<>();

    for (ResourceBundle resourceBundle : resourceBundles) {
      Locale locale = resourceBundle.getLocale();
      String localeString = locale.toString();
      String displayLanguage = locale.getDisplayLanguage(new Locale(currentLanguage));
      if (!localeString.isEmpty()) {
        languageMap.put(localeString, displayLanguage);
      }
    }

    return languageMap;
  }
}
