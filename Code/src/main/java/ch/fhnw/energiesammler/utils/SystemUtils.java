package ch.fhnw.energiesammler.utils;

/**
 * Helping Class for Utilities.
 */
public class SystemUtils {

  /**
   * No Object, no Constructor
   */
  private SystemUtils() {

  }

  /**
   * Überprüft, ob das aktuelle System eine ARM-Architektur verwendet.
   *
   * @return true, wenn das System eine ARM-Architektur hat, sonst false.
   */
  public static boolean isArmArchitecture() {
    String osArch = System.getProperty("os.arch").toLowerCase();
    return osArch.contains("arm") || osArch.contains("aarch64");
  }
}
