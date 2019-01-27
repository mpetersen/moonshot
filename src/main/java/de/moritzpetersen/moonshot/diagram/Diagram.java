package de.moritzpetersen.moonshot.diagram;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

public class Diagram {
  static final BasicStroke SOLID = new BasicStroke(1.5f, 0, 0, 1f);
  static final BasicStroke DASHED = new BasicStroke(1.5f, 0, 0, 1f, new float[]{5f, 5f}, 0f);
  static final Color RED = new Color(255, 0, 0);
  static final Color GREEN = new Color(0, 204, 0);
  private static final Font FONT = new Font("Open Sans", Font.PLAIN, 12);
  private final TimeSeriesCollection dataset = new TimeSeriesCollection();
  private final XYPlot plot;
  private final JFreeChart chart;

  public Diagram() {
    chart = ChartFactory.createTimeSeriesChart(
      "",
      "",
      "",
      dataset
    );
    chart.getLegend().setItemFont(FONT);

    plot = (XYPlot) chart.getPlot();
    plot.setBackgroundPaint(Color.WHITE);
    plot.setDomainGridlinePaint(Color.GRAY);
    plot.setRangeGridlinePaint(Color.GRAY);

    NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
    rangeAxis.setRange(0f, 24f);
    rangeAxis.setTickUnit(new NumberTickUnit(2f));
    rangeAxis.setTickLabelFont(FONT);

    ValueAxis domainAxis = plot.getDomainAxis();
    domainAxis.setTickLabelFont(FONT);
  }

  public void write(final Path outputFile) throws IOException {
    BufferedImage bufferedImage = chart.createBufferedImage(1200, 1024);
    String fileName = outputFile.getFileName().toString();
    String type = fileName.substring(fileName.lastIndexOf('.') + 1);
    ImageIO.write(bufferedImage, type, outputFile.toFile());
  }

  public void addTimeSeries(final TimeSeries series, final Paint paint, final Stroke stroke) {
    dataset.addSeries(series);
    XYItemRenderer renderer = plot.getRenderer();
    int seriesNumber = dataset.getSeriesCount() - 1;
    renderer.setSeriesPaint(seriesNumber, paint);
    renderer.setSeriesStroke(seriesNumber, stroke, false);
  }
}
