package logs_analysis_tool.example.analysis_tool.pdf.charts;

import logs_analysis_tool.example.analysis_tool.logs_repo.LogsRepo;
import logs_analysis_tool.example.analysis_tool.models.DateObjectCombination;
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
import java.util.*;

@Component
public class OrdersChart extends XYChartImp{

    private final LogsRepo logsRepo;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    @Autowired
    public OrdersChart(LogsRepo logsRepo) {
        this.logsRepo = logsRepo;
    }
    @Override
    public File chartBuilder() throws IOException {


        int[] xData = getXAxis();

        int[] yData = getYAxis();

        // Create Chart
        Chart chart = buildChart("Orders Chart","DAY","Orders Per Day");

        // Customize Chart
        customizeChart(chart);

        ((XYChart)chart).addSeries(" ", xData, yData);
        File tempFile = new File("ordersChart.png");

        BitmapEncoder.saveBitmapWithDPI(chart,String.valueOf(tempFile), BitmapEncoder.BitmapFormat.PNG,300);

        // Convert ByteArrayOutputStream
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

    private int[] getYAxis() {
        List<DateObjectCombination> yAxis = logsRepo.totalOrders(month);

        int numberOfDays = Month.of(month).length(LocalDate.now().isLeapYear());

        // Create a map to store orders per date for easier lookup
        Map<Integer, Long> ordersMap = new HashMap<>();
        yAxis.forEach(combination -> ordersMap.put(combination.getLocalDate().getDayOfMonth(), combination.getOrdersPerDate()));

        // Populate yAxis with entries for each day of the month
        for (int i = 1; i <= numberOfDays; i++) {
            if (!ordersMap.containsKey(i)) {
                yAxis.add(new DateObjectCombination(LocalDate.now().withMonth(month).withDayOfMonth(i), 0L));
            }
        }

        // Sort the yAxis list based on date
        yAxis.sort(Comparator.comparing(DateObjectCombination::getLocalDate));

        // Extract orders count from DateObjectCombination objects and convert to int array
        return yAxis.stream().mapToInt(combination -> combination.getOrdersPerDate().intValue()).toArray();
    }

}
