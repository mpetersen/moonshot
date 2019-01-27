package de.moritzpetersen.moonshot.diagram;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

import java.awt.BasicStroke;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

public class MoonSunDiagramMain {
  public static void main(String[] args) throws IOException {
    final boolean useDefaults = args.length <= 2;
    if (useDefaults) {
      System.out.println("Not enough arguments, required: <moon data file> <sun data file> <output file>");
      System.out.println("Using defaults");
    }
    final Path moonDataFile = Paths.get(useDefaults ? "data/moon_data.txt" : args[0]);
    final Path sunDataFile = Paths.get(useDefaults ? "data/sun_data.txt" : args[1]);
    final Path outputFile = Paths.get(useDefaults ? "data/diagram.png" : args[2]);

    MoonSunDiagramMain main = new MoonSunDiagramMain();
    main.run(moonDataFile, sunDataFile, outputFile);
    System.exit(0);
  }

  private void run(final Path moonDataFile, final Path sunDataFile, final Path outputFile) throws IOException {
    Diagram diagram = new Diagram();
    fillDiagram(diagram, moonDataFile, "Moon", Diagram.DASHED, MoonData::new);
    fillDiagram(diagram, sunDataFile, "Sun", Diagram.SOLID, SunData::new);
    diagram.write(outputFile);
  }

  private void fillDiagram(final Diagram diagram, final Path dataFile, final String name, final BasicStroke stroke, final Function<String, AstroData> dataMapper) throws IOException {
    TimeSeries riseSeries = new TimeSeries(name + "rise");
    TimeSeries setSeries = new TimeSeries(name + "set");
    Files.lines(dataFile).map(dataMapper).filter(AstroData::isMatch).forEach(data -> {
        riseSeries.add(new Day(data.getDay(), data.getMonth(), data.getYear()), data.getRiseTime());
        setSeries.add(new Day(data.getDay(), data.getMonth(), data.getYear()), data.getSetTime());
    });
    diagram.addTimeSeries(riseSeries, Diagram.GREEN, stroke);
    diagram.addTimeSeries(setSeries, Diagram.RED, stroke);
  }
}
