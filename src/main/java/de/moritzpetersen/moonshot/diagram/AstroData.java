package de.moritzpetersen.moonshot.diagram;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;

public abstract class AstroData {
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("d MMM yyyy");
  private final boolean isValid;
  private int day;
  private int month;
  private int year;
  private Double riseTime;
  private Double setTime;

  public AstroData(final String str, final Pattern regex) {
    Matcher matcher = regex.matcher(str);
    if (isValid = matcher.matches()) {
      LocalDate date = LocalDate.parse(matcher.group(1), FORMATTER);
      day = date.getDayOfMonth();
      month = date.getMonthValue();
      year = date.getYear();
      riseTime = toTime(matcher.group(2), matcher.group(3));
      setTime = toTime(matcher.group(4), matcher.group(5));
    }
  }

  public boolean isValid() {
    return isValid;
  }

  private Double toTime(final String hour, final String minute) {
    try {
      return parseInt(minute) / 60d + parseInt(hour);
    } catch (Exception e) {
      return null;
    }
  }

  public int getDay() {
    return day;
  }

  public int getMonth() {
    return month;
  }

  public int getYear() {
    return year;
  }

  public Double getRiseTime() {
    return riseTime;
  }

  public Double getSetTime() {
    return setTime;
  }
}
