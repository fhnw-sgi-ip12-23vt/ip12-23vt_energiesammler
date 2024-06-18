package ch.fhnw.energiesammler.utils;

import processing.core.PApplet;

public class FPSAverage {
  private final PApplet parent; // Referenz auf das Haupt-PApplet
  private final int measurementInterval;
  private long lastMeasurementTime;
  private int frameCountSinceLastMeasurement;
  private float fpsSum;

  public FPSAverage(PApplet parent, int measurementIntervalMillis) {
    this.parent = parent;
    this.measurementInterval = measurementIntervalMillis;
    reset();
  }

  public void update() {
    frameCountSinceLastMeasurement++;
    fpsSum += parent.frameRate; // Zugriff auf die frameRate des PApplet

    if (parent.millis() - lastMeasurementTime >= measurementInterval) {
      float averageFps = fpsSum / frameCountSinceLastMeasurement;
      PApplet.println("Durchschnittliche FPS: " + averageFps);
      reset();
    }
  }

  private void reset() {
    lastMeasurementTime = parent.millis();
    frameCountSinceLastMeasurement = 0;
    fpsSum = 0;
  }
}
