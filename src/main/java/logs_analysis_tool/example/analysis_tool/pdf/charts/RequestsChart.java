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
public class RequestsChart extends XYChartImp {

    private final LogsRepo logsRepo;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    @Autowired
    public RequestsChart(LogsRepo logsRepo) {
        this.logsRepo = logsRepo;
    }

    public File chartBuilder() throws IOException {

        int[] xData = getXAxis();

        int[] yData = getYAxis();

        // Create Chart
        Chart chart = buildChart("Request Chart", "DAY", "Requests Per Day");

        // Customize Chart
        customizeChart(chart);

        ((XYChart) chart).addSeries(" ", xData, yData);
        File tempFile = new File("requestsChart.png");

        BitmapEncoder.saveBitmapWithDPI(chart, String.valueOf(tempFile), BitmapEncoder.BitmapFormat.PNG, 300);

        return tempFile;
    }

    private int[] getXAxis() {
        ArrayList<Integer> xAxis = new ArrayList<>();
        int numberOfDays = Month.of(month).length(LocalDate.now().isLeapYear());

        for (int i = 0; i < numberOfDays; i++) {
            xAxis.add(i + 1);
        }

        return xAxis
                .stream()
                .mapToInt(Integer::intValue)
                .toArray();
    }

    private int[] getYAxis() {

        List<Long> yAxis = logsRepo.requestsPerMonth(month);

        return yAxis
                .stream()
                .mapToInt(Long::intValue)
                .toArray();
    }
}
