package de.moritzpetersen.moonshot.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;

abstract class DataCsvConverter {
  private static final DateTimeFormatter DATE_OUTPUT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  class OutputData {
    private final LocalDate date;
    private final LocalTime rise;
    private final LocalTime set;

    OutputData(LocalDate date, LocalTime rise, LocalTime set) {
      this.date = date;
      this.rise = rise;
      this.set = set;
    }
  }

  void parse(final Path path) throws IOException {
    Files.lines(path)
      .map(this::parseLine)
      .filter(Matcher::matches)
      .map(this::toArray)
      .map(this::process)
      .forEach(this::doOutput);
  }

  private void doOutput(final OutputData outputData) {
    System.out.printf("%s\t%s\t%s\t%d\t%d%n",
      outputData.date.format(DATE_OUTPUT),
      outputData.rise,
      outputData.set,
      toMinuteOfDay(outputData.rise),
      toMinuteOfDay(outputData.set));
  }

  private int toMinuteOfDay(final LocalTime time) {
    return time == null ? 0 : time.getHour() * 60 + time.getMinute();
  }

  protected abstract Matcher parseLine(final String line);

  protected abstract OutputData process(String[] data);

  private String[] toArray(Matcher m) {
    final String[] data = new String[m.groupCount()];
    for (int i = 0; i < data.length; i++) {
      int g = i + 1;
      data[i] = m.group(g);
    }
    return data;
  }
}
