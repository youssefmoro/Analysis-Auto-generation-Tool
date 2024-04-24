package logs_analysis_tool.example.analysis_tool.pdf.charts;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.PieSeries;
import org.knowm.xchart.internal.chartpart.Chart;

import java.awt.*;

public abstract class PieChartImp implements Charts{

    @Override
    public Chart buildChart(String chartTitle, String xAxisTitle, String yAxisTitle) {
        return new PieChartBuilder().width(500).height(357).title(chartTitle).build();
    }

    @Override
    public void customizeChart(Chart chart) {
        // Customize chart
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setChartFontColor(new Color(64, 64, 64));
        chart.getStyler().setChartTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        chart.getStyler().setSeriesColors(new Color[] {new Color(11,73,255),new Color(21,250,131)});
        chart.getStyler().setPlotBorderVisible(false);
        ((PieChart)chart).getStyler().setDefaultSeriesRenderStyle(PieSeries.PieSeriesRenderStyle.Donut);
        ((PieChart)chart).getStyler().setDonutThickness(0.5);
        ((PieChart)chart).getStyler().setStartAngleInDegrees(90);
        chart.getStyler().setHasAnnotations(false);

    }
}
