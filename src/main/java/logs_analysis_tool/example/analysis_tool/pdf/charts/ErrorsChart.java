package logs_analysis_tool.example.analysis_tool.pdf.charts;

import logs_analysis_tool.example.analysis_tool.logs_repo.LogsRepo;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
public class ErrorsChart extends XYChartImp{

    private final LogsRepo logsRepo;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    @Autowired
    public ErrorsChart(LogsRepo logsRepo) {
        this.logsRepo = logsRepo;
    }

    @Override
    public File chartBuilder() throws IOException {


        int[] xData = getXAxis();

        int[] yData = getYAxis();

        // Create Chart
        Chart chart = buildChart("Errors Chart","DAY","Errors Per Day");

        // Customize Chart
        customizeChart(chart);

        ((XYChart)chart).addSeries(" ", xData, yData);
        File tempFile = new File("errorsChart.png");

        BitmapEncoder.saveBitmapWithDPI(chart,String.valueOf(tempFile), BitmapEncoder.BitmapFormat.PNG,300);

        return tempFile;
    }

    private int[] getXAxis()
    {
        ArrayList<Integer> xAxis = new ArrayList<>();
        int numberOfDays = Month.of(month).length(LocalDate.now().isLeapYear());

        for (int i = 0; i < numberOfDays; i++) {
            xAxis.add(i+1);
        }

        return xAxis
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private int[] getYAxis()
    {

        List<Long> yAxis = logsRepo.totalErrors(month);

        return yAxis
                .stream()
                .mapToInt(Long::intValue)
                .toArray();
    }
}
