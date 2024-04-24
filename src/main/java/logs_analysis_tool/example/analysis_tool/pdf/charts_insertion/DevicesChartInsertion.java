package logs_analysis_tool.example.analysis_tool.pdf.charts_insertion;

import logs_analysis_tool.example.analysis_tool.pdf.charts.DevicesChart;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class DevicesChartInsertion implements ChartsInsertion{

    private final Logger LOG = LoggerFactory.getLogger(ErrorsChartInsertion.class);


    @Value("${devicesChartPage:10}")
    private int devicesChartPage;

    private final DevicesChart devicesChart;

    @Autowired
    public DevicesChartInsertion(DevicesChart devicesChart) {
        this.devicesChart = devicesChart;
    }

    @Override
    public void insertChart(PDDocument document) throws IOException {
        LOG.info("INSERTING DEVICES CHART");

        addDevicesChart(document);

        LOG.info("DEVICES CHART ADDED");
    }

    private void addDevicesChart(PDDocument document) throws IOException
    {

        try (PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(devicesChartPage), PDPageContentStream.AppendMode.APPEND, false)) {

            File tempFile = devicesChart.chartBuilder();

            // Insert the chart image
            PDImageXObject chartImage = PDImageXObject.createFromFileByContent(tempFile, document);
            float imageWidth = chartImage.getWidth() * 72f / 300; // Convert width to points
            float imageHeight = chartImage.getHeight() * 72f / 300; // Convert height to points

            contentStream.drawImage(chartImage, 30, 509, imageWidth, imageHeight); // Adjust the coordinates as needed

            List<Double> values = devicesChart.getValues();

            drawPercentage(document,values);
        }
    }

    private void drawPercentage(PDDocument document, List<Double> values) throws IOException {
        try (PDPageContentStream contentStream = new PDPageContentStream(document, document.getPage(devicesChartPage), PDPageContentStream.AppendMode.APPEND, false)) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 24);

            contentStream.newLineAtOffset(48, 650.34f);
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.showText(values.get(0) + "%");
            contentStream.endText();

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 24);

            contentStream.newLineAtOffset(490.52f, 482.54f);
            contentStream.setNonStrokingColor(Color.BLACK);
            contentStream.showText(values.get(1) + "%");
            contentStream.endText();
        }
    }
}
