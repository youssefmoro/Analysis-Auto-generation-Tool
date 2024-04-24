package logs_analysis_tool.example.analysis_tool.pdf.charts;

import org.knowm.xchart.internal.chartpart.Chart;

import java.io.File;
import java.io.IOException;

public interface Charts{

    public File chartBuilder() throws IOException;

    public Chart buildChart(String chartTitle, String xAxisTitle, String yAxisTitle);

    public void customizeChart(Chart chart);

}
