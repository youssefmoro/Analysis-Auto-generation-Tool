package logs_analysis_tool.example.analysis_tool.pdf.charts;

import logs_analysis_tool.example.analysis_tool.logs_repo.LogsRepo;
import lombok.Getter;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
@Getter
public class DevicesChart extends PieChartImp{

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    private final List<Double> values = new ArrayList<>();

    private final LogsRepo logsRepo;

    @Autowired
    public DevicesChart(LogsRepo logsRepo) {
        this.logsRepo = logsRepo;
    }


    @Override
    public File chartBuilder() throws IOException {
        List<String> categories = new ArrayList<>();
        categories.add("Mobile Browser");
        categories.add("Web Browser");


        Long mobileBrowserCount = logsRepo.mobileBrowserPerMonth(month);
        Long webBrowserCount = logsRepo.webBrowserPerMonth(month);

        Long totalCount = mobileBrowserCount + webBrowserCount;


        values.add((double) ((mobileBrowserCount / totalCount) * 100));
        values.add((double) ((webBrowserCount / totalCount) * 100));

        // Create PieChart
        Chart chart = buildChart("Device Chart",null,null);

       customizeChart(chart);

        // Add data to chart
        for (int i = 0; i < categories.size(); i++) {
            ((PieChart)chart).addSeries(categories.get(i), values.get(i));
        }

        File tempFile = new File("devicesChart.png");

        BitmapEncoder.saveBitmapWithDPI(chart,String.valueOf(tempFile), BitmapEncoder.BitmapFormat.PNG,300);


        return tempFile;
    }
}
