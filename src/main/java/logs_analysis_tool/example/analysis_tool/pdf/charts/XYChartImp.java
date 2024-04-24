package logs_analysis_tool.example.analysis_tool.pdf.charts;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.Circle;
import org.knowm.xchart.style.markers.Marker;

import java.awt.*;

public abstract class XYChartImp implements Charts{

    @Override
    public Chart buildChart(String chartTitle, String xAxisTitle, String yAxisTitle) {
        return new XYChartBuilder().width(538).height(246).theme(Styler.ChartTheme.GGPlot2)
                .title(chartTitle).xAxisTitle(xAxisTitle).yAxisTitle(yAxisTitle).build();
    }

    @Override
    public void customizeChart(Chart chart) {
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setLegendVisible(false);

        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setChartFontColor(new Color(64, 64, 64));
        chart.getStyler().setChartTitleFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));

        chart.getStyler().setDecimalPattern("#,###");
        chart.getStyler().setLegendFont(new Font(Font.SERIF, Font.PLAIN, 18));
        chart.getStyler().setSeriesColors(new Color[] {new Color(51,153,255)});
        chart.getStyler().setSeriesMarkers(new Marker[]{new Circle()});
        ((XYChart)chart).getStyler().setPlotGridLinesVisible(false);
        ((XYChart)chart).getStyler().setAxisTickPadding(5);
        ((XYChart)chart).getStyler().setAxisTickMarkLength(5);
        ((XYChart)chart).getStyler().setPlotMargin(0);
        ((XYChart)chart).getStyler().setAxisTitleFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        ((XYChart)chart).getStyler().setAxisTickLabelsFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));

        ((XYChart)chart).getStyler().setYAxisLogarithmic(false);
        ((XYChart)chart).getStyler().setXAxisLabelRotation(0);
    }


}
