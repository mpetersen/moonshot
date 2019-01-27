package de.moritzpetersen.moonshot.data;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public class MoonDataCsvConverter extends DataCsvConverter {
  private static final Pattern MOON_RISE_SET_PATTERN = Pattern.compile("(\\d{1,2}\\s\\w{3}\\s\\d{4})\\s*Rise:\\s*(\\d{1,2})h(\\d{1,2})m.*Transit:\\s*(\\d{1,2})h(\\d{1,2})m.*Set:\\s*(\\d{1,2})h(\\d{1,2})m.*");
  private static final String DATA_FILE = "data/moon_data.txt";
  private static final DateTimeFormatter DATE_INPUT =
    DateTimeFormatter.ofPattern("d MMM yyyy", Locale.US);

  public static void main(String[] args) throws IOException {
    String dataFile = args.length > 0 ? args[1] : DATA_FILE;

    Path path = Paths.get(dataFile);

    new MoonDataCsvConverter().parse(path);
  }

  @Override
  protected Matcher parseLine(final String line) {
    Matcher m = MOON_RISE_SET_PATTERN.matcher(line);
    if (!m.matches()) {
      System.err.println("No match: " + line);
    }
    return m;
  }

  @Override
  protected OutputData process(final String[] data) {
    LocalDate date = LocalDate.parse(data[0], DATE_INPUT);
    LocalTime rise = toLocalTime(data, 1);
    LocalTime set = toLocalTime(data, 5);
    return new OutputData(date, rise, set);
  }

  private LocalTime toLocalTime(final String[] data, final int start) {
    int i = start;
    return LocalTime.of(parseInt(data[i++]), parseInt(data[i]));
  }
}
