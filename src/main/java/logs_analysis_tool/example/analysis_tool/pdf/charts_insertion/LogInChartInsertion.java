package logs_analysis_tool.example.analysis_tool.pdf.charts_insertion;

import logs_analysis_tool.example.analysis_tool.pdf.charts.LogsChart;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class LogInChartInsertion implements ChartsInsertion{

    private final Logger LOG = LoggerFactory.getLogger(LogInChartInsertion.class);
    private final LogsChart logsChart;

    @Autowired
    public LogInChartInsertion(LogsChart logsChart) {
        this.logsChart = logsChart;
    }

    @Value("${logInChartPage:2}")
    private int logInChartPage;

    @Override
    public void insertChart(PDDocument document) throws IOException{
        LOG.info("INSERTING LOGIN CHART");
        addLoggingChart(document);
        LOG.info("LOGIN CHART ADDED");
    }

    private void addLoggingChart(PDDocument document) throws IOException {
        PDPage requestChartPage = document.getPage(logInChartPage);

        // Create a PDPageContentStream object for drawing
        try (PDPageContentStream contentStream = new PDPageContentStream(document, requestChartPage, PDPageContentStream.AppendMode.APPEND, false)) {

            File tempFile = logsChart.chartBuilder();

            // Insert the chart image
            PDImageXObject chartImage = PDImageXObject.createFromFileByContent(tempFile, document);
            float imageWidth = chartImage.getWidth() * 72f / 300; // Convert width to points
            float imageHeight = chartImage.getHeight() * 72f / 300; // Convert height to points

            contentStream.drawImage(chartImage, 30, 509, imageWidth, imageHeight); // Adjust the coordinates as needed
        }
    }
}
