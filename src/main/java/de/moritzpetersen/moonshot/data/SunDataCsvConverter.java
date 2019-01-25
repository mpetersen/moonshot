/*************************************************************************
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 * Copyright 2019 Adobe
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Adobe and its suppliers, if any. The intellectual
 * and technical concepts contained herein are proprietary to Adobe
 * and its suppliers and are protected by all applicable intellectual
 * property laws, including trade secret and copyright laws.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe.
 **************************************************************************/

package de.moritzpetersen.moonshot.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SunDataCsvConverter {
  private static final Pattern SUN_RISE_SET_PATTERN = Pattern.compile(
      "(.*)\\tRise\\s*:\\s*(\\S*)\\s*Set\\s*:\\s*(\\S*).*");
  private static final Pattern TIME_PATTERN = Pattern.compile("(\\d*)h(\\d*)\\.(\\d)m");
  private static final String DATA_FILE = "data/sun_data.txt";
  private static final DateTimeFormatter DATE_INPUT =
      DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);
  private static final DateTimeFormatter DATE_OUTPUT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

  public static void main(String[] args) throws IOException {
    String dataFile = args.length > 0 ? args[1] : DATA_FILE;

    Path path = Paths.get(dataFile);
    Files.lines(path)
        .map(SUN_RISE_SET_PATTERN::matcher)
        .filter(Matcher::matches)
        .map(m -> new String[] {m.group(1), m.group(2), m.group(3)})
        .forEach(data -> {
          LocalDate date = LocalDate.parse(data[0], DATE_INPUT);
          LocalTime rise = toLocalTime(data[1]);
          LocalTime set = toLocalTime(data[2]);
          System.out.println(date + "\t" + rise + "\t" + set + "\t" + toSecondOfDay(rise) + "\t" + toSecondOfDay(set));
        });
  }

  private static long toSecondOfDay(LocalTime time) {
    return time == null ? 0 : (time.getHour() * 60 + time.getMinute()) * 60 + time.getSecond();
  }

  private static LocalTime toLocalTime(String input) {
    Matcher m = TIME_PATTERN.matcher(input);
    if (m.matches()) {
      int hour = Integer.parseInt(m.group(1));
      int minute = Integer.parseInt(m.group(2));
      int second = Integer.parseInt(m.group(3)) * 6;
      return LocalTime.of(hour, minute, second);
    }
    return null;
  }
}
