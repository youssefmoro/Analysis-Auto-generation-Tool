package logs_analysis_tool.example.analysis_tool.pdf.charts_insertion;

import logs_analysis_tool.example.analysis_tool.pdf.charts.RequestsChart;
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
public class RequestsChartInsertion implements ChartsInsertion{
    private final Logger LOG = LoggerFactory.getLogger(RequestsChartInsertion.class);

    private final RequestsChart requestsChart;

    @Autowired
    public RequestsChartInsertion(RequestsChart requestsChart) {
        this.requestsChart = requestsChart;
    }

    @Value("${requestsChartPage:1}")
    private int requestsChartPage;

    @Override
    public void insertChart(PDDocument document) throws IOException {
        LOG.info("INSERTING REQUESTS CHART");
        addRequestsChart(document);
        LOG.info("REQUESTS CHART ADDED");
    }

    private void addRequestsChart(PDDocument document) throws IOException {
        PDPage requestChartPage = document.getPage(requestsChartPage);

        // Create a PDPageContentStream object for drawing
        try (PDPageContentStream contentStream = new PDPageContentStream(document, requestChartPage, PDPageContentStream.AppendMode.APPEND, false)) {

            File tempFile = requestsChart.chartBuilder();

            // Insert the chart image
            PDImageXObject chartImage = PDImageXObject.createFromFileByContent(tempFile,document);
            float imageWidth = chartImage.getWidth() * 72f / 300; // Convert width to points
            float imageHeight = chartImage.getHeight() * 72f / 300; // Convert height to points

            contentStream.drawImage(chartImage, 30, 509, imageWidth, imageHeight); // Adjust the coordinates as needed
        }
    }
}
