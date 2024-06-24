package ch.fhnw.energiesammler.gamelogic;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fhnw.energiesammler.AppConfig;
import ch.fhnw.energiesammler.controls.GPIOInputHandler;
import ch.fhnw.energiesammler.controls.InputManager;
import ch.fhnw.energiesammler.entities.Door;
import ch.fhnw.energiesammler.entities.Enemy;
import ch.fhnw.energiesammler.entities.MenuBar;
import ch.fhnw.energiesammler.entities.Player;
import ch.fhnw.energiesammler.gamelogic.states.GameState;
import ch.fhnw.energiesammler.gamelogic.states.GameStateBase;
import ch.fhnw.energiesammler.gamelogic.states.HomeScreen;
import ch.fhnw.energiesammler.ui.Drawer;
import ch.fhnw.energiesammler.ui.GameMenu;
import ch.fhnw.energiesammler.utils.FPSAverage;
import ch.fhnw.energiesammler.utils.ImageLoader;
import ch.fhnw.energiesammler.utils.LatencyMonitor;
import ch.fhnw.energiesammler.utils.SystemUtils;
import ch.fhnw.energiesammler.utils.TextEmLocale;
import ch.fhnw.energiesammler.utils.TextLocale;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

/**
 * Hauptklasse des Jump n Run Spiels. Das ganze Spiel wird hier behandelt.
 */
public class Platformer extends PApplet {
  public static final int DOORCOUNTPERLEVEL = 2;
  //Konstanten für Animation
  public static final int NEUTRAL_FACING = 0;
  public static final int RIGHT_FACING = 1;
  public static final int LEFT_FACING = 2;
  private static final int GAME_FPS = 30;
  private static final DecimalFormat DF = new DecimalFormat("#.###");
  private final Map<Enemy, Integer> enemiesTurnedOff = new HashMap<>();
  public MenuBar menuBarPlay;
  public MenuBar menuBarLanguage;
  public MenuBar menuBarContinue;
  public MenuBar menuBarHomeScreen;
  public MenuBar menuMale;
  public MenuBar menuFemale;

  //Characterwahl
  public boolean characterChoice;
  public boolean isPaused;
  //Physics
  public PFont pixelFont;
  public float view_x = 0;
  public float view_y = displayHeight;
  public GameStateBase gameState;
  public InputManager inputManager;
  public GameMenu gameMenu;
  public Pictures pictures;
  public Drawer drawer;
  public Level level;
  FPSAverage fpsAverage;
  private Player player;
  private LatencyMonitor latencyMonitor;
  private String gameLanguage;
  private TextLocale textLocale;
  private TextEmLocale textEmLocale;
  private GPIOInputHandler gpioInputHandler;

  /**
   * Einstellungen für grösse des Bildschirms.
   */
  @Override
  public void settings() {
    fullScreen();
  }

  /**
   * Erstellen der Platformen und des Spielers.
   */
  @Override
  public void setup() {
    gameLanguage = AppConfig.getValue("startLanguage", "de");
    textLocale = new TextLocale(gameLanguage);
    textEmLocale = new TextEmLocale(gameLanguage);

    pixelFont = createFont(ImageLoader.loadFile("fonts/pixel.ttf"), 20);
    textFont(pixelFont);
    pictures = new Pictures(this, gameLanguage);
    pictures.menuBackground.resize(displayWidth, displayHeight);
    pictures.backgroundCharacterauswahl.resize(displayWidth, displayHeight);

    level = new Level(this, this, pictures);
    drawer = new Drawer(this, this, level, pictures, textLocale);
    setupMenuBar();

    frameRate(GAME_FPS); // set game FPS
    fpsAverage = new FPSAverage(this, 5000);
    latencyMonitor = new LatencyMonitor();

    inputManager = new InputManager();
    if (SystemUtils.isArmArchitecture()) {
      gpioInputHandler =
          new GPIOInputHandler(inputManager); // Funktioniert nur bei ARM Prozessoren (Raspberry Pi)
    }
    gameMenu = new GameMenu(this, textLocale, textEmLocale);
    gameState = new HomeScreen(this, inputManager, level);
  }

  /**
   * Beenden des Spiels.
   */
  @Override
  public void exit() {
    if (gpioInputHandler != null) {
      gpioInputHandler.shutdown();
    }
    super.exit(); // Normal Shutdown
  }

  private void setupMenuBar() {
    menuBarContinue =
        new MenuBar(this, pictures.menuBarUnchoosen, displayWidth / 920f, displayWidth / 2,
            2 * (displayHeight / 4));
    menuBarHomeScreen =
        new MenuBar(this, pictures.menuBarUnchoosen, displayWidth / 920f, displayWidth / 2,
            3 * (displayHeight / 4));
    menuBarPlay =
        new MenuBar(this, pictures.menuBarUnchoosen, displayWidth / 920f, displayWidth / 2,
            2 * (displayHeight / 4));
    menuBarLanguage =
        new MenuBar(this, pictures.menuBarUnchoosen, displayWidth / 920f, displayWidth / 2,
            3 * (displayHeight / 4));
    menuMale =
        new MenuBar(this, pictures.characterMale, displayWidth / 250f, displayWidth / 3,
        4 * (displayHeight / 8));
    menuFemale =
        new MenuBar(this, pictures.characterFemale, displayWidth / 250f, 2 * (displayWidth / 3),
            4 * (displayHeight / 8));
  }

  /**
   * Homescreen vorbereiten.
   */
  public void homeScreenSetup() {
    menuBarPlay.loadChooseImages(pictures.menuBarChoosen);
    menuBarLanguage.loadChooseImages(pictures.menuBarChoosen);
  }

  /**
   * Character-screen vorbereiten.
   */
  public void characterScreenSetup() {
    menuMale.loadChooseImages(pictures.characterMaleSelected);
    menuFemale.loadChooseImages(pictures.characterFemaleSelected);
  }

  /**
   * Variablen neu erstellen für neuen Spieldurchgang.
   */
  public void gameScreenSetup() {
    String charGender;
    if (characterChoice) {
      charGender = "male";
    } else {
      charGender = "female";
    }

    // List of which Enemies can be turned off
    List<String> enemyNames = List.of("bulb", "mixer", "oven");
    for (String name : enemyNames) {
      // Add every enemy to the enemiesTurnedOff Map
      enemiesTurnedOff.put(
        new Enemy(this, this, this.level, pictures.oven, this.level.energyScale * 1.5f,
          AppConfig.getValue("dmg." + name, -3.0f), AppConfig.getValue("speed." + name, 1.0f), name, 0, 0), 0);
    }

    pictures.tutorial1.resize(displayWidth, displayHeight);
    pictures.tutorial2.resize(displayWidth, displayHeight);
    pictures.tutorial3.resize(displayWidth, displayHeight);
    pictures.tutorial4.resize(displayWidth, displayHeight);
    pictures.background.resize(displayWidth, displayHeight);

    // Game Reset
    // Diese Reihefolge muss in der aktuellen Implementierung zwingend eingehalten werden
    PImage p = ImageLoader.loadImage(this, charGender + "/" + charGender + "_runRight1.png");
    player = new Player(this, level, p, (float) (displayHeight / 20) / 35, charGender);
    drawer = new Drawer(this, this, level, pictures, textLocale);
    level.reset();
  }

  public void pauseScreenSetup() {
    menuBarContinue.loadChooseImages(pictures.menuBarChoosen);
    menuBarHomeScreen.loadChooseImages(pictures.menuBarChoosen);
  }

  /**
   * Spiel ausführen durch Zeichnen der Komponenten.
   */
  @Override
  public void draw() {
    if (!AppConfig.getValue("testModus", false)) {
      drawer.draw(gameState);
    } else {
      long startTime = System.nanoTime();
      drawer.draw(gameState);

      long endTime = System.nanoTime(); // Endzeit nach dem Zeichnen
      latencyMonitor.recordLatency(startTime, endTime); // Latenz aufzeichnen

      if (frameCount % GAME_FPS == 0) { // Jede Sekunde, wenn X FPS
        System.out.println(
            "Durchschnittliche Latenz: " + latencyMonitor.getAverageLatency() + " ms");
      }
    }
  }

  public void drawGameScreen() {
    drawer.drawGameScreen();
    if (AppConfig.getValue("fpsCounter", false)) {
      fpsAverage.update();
      gameMenu.displayFPS("FPS: " + PApplet.nf(frameRate, 1, 2), 10, 20);
    }
  }

  public void drawHomeScreen() {
    drawer.drawHomeScreen();
  }

  public void drawPauseScreen(GameState menuChoice) {
    drawer.drawPauseScreen(menuChoice);
  }

  public Map<Enemy, Integer> getEnemiesTurnedOff() {
    return enemiesTurnedOff;
  }

  /**
   * @param e Merkt sich wie oft ein Gegner desselben typen besiegt wurde
   */
  public void setEnemiesTurnedOff(Enemy e) {
    e.center_x = 0;
    e.center_y = 0;
    enemiesTurnedOff.put(e, enemiesTurnedOff.getOrDefault(e, 0) + 1);
  }

  public void drawCharacterScreen() {
    drawer.drawCharacterScreen();
  }

  public DecimalFormat getDf() {
    return DF;
  }

  public GameLogic getUpdater() {
    return drawer.getUpdater();
  }

  //Spieler bewegen auf drücken einer Taste
  @Override
  public void keyPressed() {
    inputManager.handleKeyboardInput(keyCode, true);
  }

  //Spieler bewegung abbrechen wenn Taste nicht mehr gedrückt wird
  @Override
  public void keyReleased() {
    inputManager.handleKeyboardInput(keyCode, false);
  }

  public String getGameLanguage() {
    return gameLanguage;
  }

  public void setGameLanguage(String gameLanguage) {
    this.gameLanguage = gameLanguage;
    gameLanguageChanges();
  }

  private void gameLanguageChanges() {
    textLocale.setLanguage(gameLanguage);
    textEmLocale.setLanguage(gameLanguage);
    pictures.loadTutorialImages(gameLanguage);
    gameMenu = new GameMenu(this, textLocale, textEmLocale);
  }

  public void addAllDoors(Door door) {
    drawer.getUpdater().addAllDoors(door);
  }

  //Liste aller Türen zurückgeben
  public ArrayList<Door> getUsedDoors() {
    return drawer.getUpdater().getUsedDoors();
  }

  //Liste aller verwendeten Türen zurückgeben
  public ArrayList<Door> getAllDoors() {
    return drawer.getUpdater().getAllDoors();
  }

  public void movePlayerX(int dir) {
    getPlayer().change_x = drawer.getUpdater().getSpeed() * dir;
  }

  public void movePlayerY(int dir) {
    getPlayer().change_y = drawer.getUpdater().getJump() * dir;
  }

  public Player getPlayer() {
    return player;
  }
}
