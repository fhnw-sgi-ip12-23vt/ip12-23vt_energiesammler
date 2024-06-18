package ch.fhnw.energiesammler.utils;

import java.nio.file.FileSystems;
import java.nio.file.Paths;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Utility class for loading images.
 */
public class ImageLoader {
  private static final String SRC_RESOURCES_PATH = "src/main/resources";
  private static final String SRC_RESOURCES_IMG_PATH = SRC_RESOURCES_PATH + "/img";
  private static final String SRC_RESOURCES_FILES_PATH = SRC_RESOURCES_PATH + "/files";
  private static final String CURRENT_DIRECTORY = System.getProperty("user.dir");
  private static final String OS = System.getProperty("os.name").toLowerCase();

  /**
   * Loads an image using the provided PApplet and filename.
   *
   * @param applet   The PApplet instance.
   * @param filename The filename of the image.
   * @return The loaded PImage.
   */
  public static PImage loadImage(PApplet applet, String filename) {
    String imagePath = getImagePath(filename);
    return applet.loadImage(imagePath);
  }

  /**
   * Gets the path of the image based on the operating system.
   *
   * @param filename The filename of the image.
   * @return The Path object representing the image path.
   */
  public static String getImagePath(String filename) {
    if (OS.contains("win")) {
      // Windows path
      return FileSystems.getDefault().getPath(CURRENT_DIRECTORY, SRC_RESOURCES_IMG_PATH, filename).toString();
    } else if (OS.contains("mac")) {
      // MacOS path
      return FileSystems.getDefault().getPath(CURRENT_DIRECTORY, SRC_RESOURCES_IMG_PATH, filename).toString();
    } else if (OS.contains("nix") || OS.contains("nux")) {
      // Unix path (Maven project structure)
      return ImageLoader.class.getResource("/img/" + filename).toExternalForm();
    } else {
      // Default path
      return ImageLoader.class.getResource("/img/" + filename).toExternalForm();
    }
  }

  public static String loadFile(String filename) {
    return getFilePath(filename);
  }

  /**
   * Gets the full path for a file based on the operating system.
   *
   * @param filename The filename of the file.
   * @return The Path object representing the full file path.
   */
  private static String getFilePath(String filename) {
    if (OS.contains("win")) {
      // Windows path
      return Paths.get(CURRENT_DIRECTORY, SRC_RESOURCES_FILES_PATH, filename).toString();
    } else if (OS.contains("mac")) {
      // MacOS path
      return Paths.get(CURRENT_DIRECTORY, SRC_RESOURCES_FILES_PATH, filename).toString();
    } else if (OS.contains("nix") || OS.contains("nux")) {
      // Unix path (Maven project structure)
      return ImageLoader.class.getResource("/files/" + filename).toExternalForm();
    } else {
      // Default path
      return ImageLoader.class.getResource("/files/" + filename).toExternalForm();
    }
  }
}
