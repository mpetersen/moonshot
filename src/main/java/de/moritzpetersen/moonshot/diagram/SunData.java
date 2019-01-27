package de.moritzpetersen.moonshot.diagram;

import java.util.regex.Pattern;

public class SunData extends AstroData {
  private static final Pattern REGEX = Pattern.compile("(\\d{1,2}\\s\\w{3}\\s\\d{4})\\s+Rise\\s+:\\s+(\\d{1,2})h(\\d{1,2}).*Set\\s+:\\s+(\\d{1,2})h(\\d{1,2}).*");

  public SunData(final String str) {
    super(str, REGEX);
  }
}
