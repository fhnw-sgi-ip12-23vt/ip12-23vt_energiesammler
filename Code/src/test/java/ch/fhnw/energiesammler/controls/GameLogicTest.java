package ch.fhnw.energiesammler.controls;

import java.util.ArrayList;

import ch.fhnw.energiesammler.entities.Door;
import ch.fhnw.energiesammler.entities.Enemy;
import ch.fhnw.energiesammler.entities.EnergySource;
import ch.fhnw.energiesammler.entities.Player;
import ch.fhnw.energiesammler.entities.Sprite;
import ch.fhnw.energiesammler.gamelogic.GameLogic;
import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.ui.Drawer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import processing.core.PApplet;
import processing.core.PImage;


class GameLogicTest {
  private PApplet pApplet;
  private Level level;
  private Player player;
  private GameLogic gameLogic;

  @BeforeEach
  void setUp() {
    // Mock-Objekte initialisieren
    Platformer platformer = mock(Platformer.class);
    pApplet = mock(PApplet.class);
    level = mock(Level.class);
    final Drawer drawer = mock(Drawer.class);

    // Realistische Objekte erstellen
    player = new Player(platformer, level, new PImage(), 1,
        "female"); // einfacher da mehrere Sprite Methode aufgerufen werden

    // Mock-Verhalten definieren
    when(platformer.getPlayer()).thenReturn(player);
    when(pApplet.millis()).thenReturn(1000);

    // GameLogic-Instanz erstellen
    gameLogic = new GameLogic(platformer, pApplet, level, drawer);
    // Energy ist 3 bei Begin, maxEnergy ist 6
  }

  // Test Energy collect and reduce
  @Test
  void testCollectEnergy() {
    EnergySource energySource = new EnergySource(pApplet, new PImage(), 1.0f, 3, "lightning.png");
    ArrayList<Sprite> energyList = new ArrayList<>();
    energyList.add(energySource);

    when(level.getEnergySources()).thenReturn(energyList);
    when(gameLogic.checkCollisionList(player, level.getEnergySources())).thenReturn(energyList);

    // Action
    gameLogic.collectEnergy();

    // Verify
    assertEquals(6, gameLogic.collectedEnergy);
    assertTrue(gameLogic.displayLostWinEnergy);
  }

  @Test
  void testReduceEnergy() {
    assertEquals(3, gameLogic.collectedEnergy);

    gameLogic.reduceEnergy(1);
    assertEquals(2, gameLogic.collectedEnergy);

    gameLogic.reduceEnergy(1);
    assertEquals(1, gameLogic.collectedEnergy);
  }

  @Test
  void testNoNegativeEnergy() {
    assertEquals(3, gameLogic.collectedEnergy);

    // Energie auf 0 reduzieren
    gameLogic.reduceEnergy(3);

    // Verify
    assertEquals(0, gameLogic.collectedEnergy);

    // Energie weiter reduzieren und sicherstellen, dass sie nicht negativ wird
    gameLogic.reduceEnergy(5);
    assertEquals(0, gameLogic.collectedEnergy);
  }

  @Test
  void testMaxEnergy() {
    assertEquals(6, gameLogic.maxEnergy);

    EnergySource energySource = new EnergySource(pApplet, new PImage(), 1.0f, 30, "lightning.png");
    ArrayList<Sprite> energyList = new ArrayList<>();
    energyList.add(energySource);

    when(level.getEnergySources()).thenReturn(energyList);
    when(gameLogic.checkCollisionList(player, level.getEnergySources())).thenReturn(energyList);

    // Action
    gameLogic.collectEnergy();

    // Verify
    assertEquals(6, gameLogic.collectedEnergy);

    gameLogic.maxEnergy = 12;
    assertEquals(6, gameLogic.collectedEnergy);

    energyList.add(energySource);
    gameLogic.collectEnergy();
    assertEquals(12, gameLogic.collectedEnergy);
  }

  // Test Game Over and Game Start
  @Test
  void testFallOffCliff() {
    when(level.getLevelLow()).thenReturn(50f);

    assertFalse(gameLogic.isGameOver);

    player.setBottom(100f);
    gameLogic.fallOffCliff();

    assertTrue(gameLogic.isGameOver);
    assertEquals(1, gameLogic.getCurrentMap());
  }

  @Test
  void testCollideWithEnemyGameOver() {
    Enemy enemy = new Enemy(pApplet, new Platformer(), level, new PImage(), 0f, -6f, 1f, "oven");

    assertFalse(gameLogic.isGameOver);

    ArrayList<Sprite> enemies = new ArrayList<>();
    enemies.add(enemy);

    when(level.getEnemies()).thenReturn(enemies);
    when(gameLogic.checkCollisionList(player, level.getEnemies())).thenReturn(enemies);

    // Kollision Simulation
    gameLogic.collideWithEnemy();

    assertTrue(gameLogic.isGameOver);
    assertEquals(1, gameLogic.getCurrentMap());
  }

  @Test
  void testEnterDoorGameOver() {
    Door door = new Door(pApplet, new Platformer(), new PImage(), 0, " ", 6);

    assertFalse(gameLogic.isGameOver);

    ArrayList<Sprite> doors = new ArrayList<>();
    doors.add(door);

    when(level.getLevelDoors()).thenReturn(doors);
    when(gameLogic.checkCollisionList(player, level.getLevelDoors())).thenReturn(doors);

    gameLogic.enterDoor();

    assertTrue(gameLogic.isGameOver);
    assertEquals(1, gameLogic.getCurrentMap());
  }
}
