package logs_analysis_tool.example.analysis_tool.pdf.tables;

import logs_analysis_tool.example.analysis_tool.logs_repo.LogsRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Component
public class OrdersTable implements Tables {

    private final Logger LOG = LoggerFactory.getLogger(OrdersTable.class);

    private final LogsRepo logsRepo;

    @Value("#{T(java.time.LocalDate).now().minusMonths(3).getMonthValue()}")
    private int month;

    @Value("${ordersTablePage:5}")
    private int ordersTablePage;

    @Autowired
    public OrdersTable(LogsRepo logsRepo) {
        this.logsRepo = logsRepo;
    }


    @Override
    public void createTable(PDDocument document, PDType0Font font) throws IOException {
        LOG.info("DRAWING ORDERS TABLE");
        PDPage orderPage = document.getPage(ordersTablePage);
        try (PDPageContentStream contentStream = new PDPageContentStream(document,orderPage,
                PDPageContentStream.AppendMode.APPEND, false)) {
            monthDaysDrawer(contentStream,font);
            successfulOrdersDrawer(contentStream,font);
            failedOrdersDrawer(contentStream,font);
        }
        LOG.info("ORDERS TABLE CREATED");
    }

    private void monthDaysDrawer(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        LOG.info("DRAWING Days COLUMN");
        int numberOfDays = Month.of(month).length(LocalDate.now().isLeapYear());
        for (int i = 0; i < 15; i++) {
            float x = 43;
            float y = 429f;
            writeText(contentStream,x,y,String.valueOf(i+1),i,font);
        }

        for (int j = 15; j < numberOfDays; j++) {
            float x = 312;
            float y = 429f;
            if(j == 30)
            {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(x, 10);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText(String.valueOf(j + 1));
                contentStream.endText();
            }else
            {
                writeText(contentStream,x,y,String.valueOf(j),j,font);
            }
        }
    }

    private void successfulOrdersDrawer(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        LOG.info("DRAWING Successful Orders COLUMN");
        List<Long> successfulOrder = logsRepo.successOrders(month);
        int numberOfDays = Month.of(month).length(LocalDate.now().isLeapYear());
        for (int i = 0; i < 15; i++) {
            float x = 131;
            float y = 429f;
            writeText(contentStream,x,y,String.valueOf(successfulOrder.get(i)),i,font);
        }
        for (int j = 15; j < numberOfDays; j++) {
            float x = 400;
            float y = 429f;
            if(j == 30)
            {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(x, 10);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText(String.valueOf(successfulOrder.get(j)));
                contentStream.endText();
            }
            else
            {
                writeText(contentStream,x,y,String.valueOf(successfulOrder.get(j)),j-15,font);
            }
        }
    }

    private void failedOrdersDrawer(PDPageContentStream contentStream, PDType0Font font) throws IOException {
        LOG.info("DRAWING Failed Orders COLUMN");
        List<Long> failedOrders = logsRepo.failedOrders(month);
        int numberOfDays = Month.of(month).length(LocalDate.now().isLeapYear());

        for (int i = 0; i < 15; i++) {
            float x = 235;
            float y = 429f;
            writeText(contentStream,x,y,String.valueOf(failedOrders.get(i)),i,font);
        }
        for (int j = 15; j < numberOfDays; j++) {
            float x = 504;
            float y = 429f;
            if(j == 30)
            {
                contentStream.beginText();
                contentStream.setFont(font, 12);
                contentStream.newLineAtOffset(x, 10);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.showText(String.valueOf(failedOrders.get(j)));
                contentStream.endText();
            }
            else
            {
                writeText(contentStream,x,y,String.valueOf(failedOrders.get(j)),j-15,font);
            }
        }
    }

    private void writeText(PDPageContentStream contentStream,float x,float y,String s,int i, PDType0Font font) throws IOException {

        float differenceBetweenValues = 28.5f;

        contentStream.beginText();
        contentStream.setFont(font, 12);

        contentStream.newLineAtOffset(x, y - (i * differenceBetweenValues));
        contentStream.setNonStrokingColor(Color.BLACK);
        contentStream.showText(s);
        contentStream.endText();
    }
}