package de.moritzpetersen.moonshot.diagram;

import java.util.regex.Pattern;

public class MoonData extends AstroData {
  private static final Pattern REGEX = Pattern.compile("(\\d{1,2}\\s\\w{3}\\s\\d{4})\\sRise:\\s(.{1,2})h(.{1,2})m.*Set:\\s(.{1,2})h(.{1,2})m.*");

  public MoonData(final String str) {
    super(str, REGEX);
  }
}
