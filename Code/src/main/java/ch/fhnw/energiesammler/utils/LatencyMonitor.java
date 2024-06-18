package ch.fhnw.energiesammler.utils;

import java.util.LinkedList;
import java.util.Queue;

public class LatencyMonitor {
  private static final int MAX_SAMPLES = 100; // Maximale Anzahl von gespeicherten Latenzwerten
  private final Queue<Long> latencies;

  public LatencyMonitor() {
    latencies = new LinkedList<>();
  }

  public void recordLatency(long startTime, long endTime) {
    long latency =
        (endTime - startTime) / 1_000_000; // Umwandlung von Nanosekunden in Millisekunden
    if (latencies.size() >= MAX_SAMPLES) {
      latencies.poll(); // Entferne den ältesten Wert, wenn das Limit erreicht ist
    }
    latencies.add(latency);
    System.out.println("Aktuelle Latenz: " + latency + " ms");
  }

  public double getAverageLatency() {
    return latencies.stream().mapToLong(Long::longValue).average().orElse(0.0);
  }
}
