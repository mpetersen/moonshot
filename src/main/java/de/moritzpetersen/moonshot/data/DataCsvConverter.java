package de.moritzpetersen.moonshot.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.regex.Matcher;

abstract class DataCsvConverter {
  protected void parse(final Path path) throws IOException {
    Files.lines(path)
      .map(this::parseLine)
      .filter(Matcher::matches)
      .map(this::toArray)
      .forEach(this::process);
  }

  protected abstract Matcher parseLine(final String line);

  protected abstract void process(String[] data);

  private String[] toArray(Matcher m) {
    final String[] data = new String[m.groupCount()];
    for (int i = 0; i < data.length; i++) {
      int g = i + 1;
      data[i] = m.group(g);
    }
    return data;
  }

  protected static long toSecondOfDay(LocalTime time) {
    return time == null ? 0 : (time.getHour() * 60 + time.getMinute()) * 60 + time.getSecond();
  }
}
