package logs_analysis_tool.example.analysis_tool.pdf;

import logs_analysis_tool.example.analysis_tool.pdf.headlines.DurationOfAnalysis;
import logs_analysis_tool.example.analysis_tool.pdf.headlines.PdfTitle;
import logs_analysis_tool.example.analysis_tool.pdf.charts_insertion.*;
import logs_analysis_tool.example.analysis_tool.pdf.tables.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class GeneratePDF {

    private final Logger LOG = LoggerFactory.getLogger(ErrorsChartInsertion.class);

    @Value("${analysis.title}")
    private String title;

    @Value("${templatePath}")
    private String templatePath;
    @Value("${outputPath}")
    private String outputPath;

    private final RequestsChartInsertion requestsChartInsertion;
    private final LogInChartInsertion logInChartInsertion;
    private final OrdersChartInsertion ordersChartInsertion;
    private final ErrorsChartInsertion errorsChartInsertion;
    private final DurationOfAnalysis durationOfAnalysis;
    private final PdfTitle pdfTitle;
    private final RequestsTables requestsTables;
    private final OrdersTable ordersTable;
    private final ErrorsTable errorsTable;
    private final ErrorsApiTable errorsApiTable;
    private final LogInTables logInTables;
    private final LongestExecutionTimeTable longestExecutionTimeTable;
    private final MostUsedApisTable mostUsedApisTable;
    private final TopFailureWidgetsTable topFailureWidgetsTable;
    private final TopSuccessfulWidgetsTable topSuccessfulWidgetsTable;
    private final TopUsedWidgetsTable topUsedWidgetsTable;
    private final DevicesChartInsertion devicesChartInsertion;
    private final SessionDrawer sessionDrawer;
    private final FontSelection fontSelection;

    @Autowired
    public GeneratePDF(RequestsChartInsertion requestsChartInsertion, LogInChartInsertion logInChartInsertion,
                       OrdersChartInsertion ordersChartInsertion, ErrorsChartInsertion errorsChartInsertion,
                       DurationOfAnalysis durationOfAnalysis, PdfTitle pdfTitle, RequestsTables requestsTables,
                       OrdersTable ordersTable, ErrorsTable errorsTable, ErrorsApiTable errorsApiTable,
                       LogInTables logInTables, LongestExecutionTimeTable longestExecutionTimeTable,
                       MostUsedApisTable mostUsedApisTable, TopFailureWidgetsTable topFailureWidgetsTable,
                       TopSuccessfulWidgetsTable topSuccessfulWidgetsTable, TopUsedWidgetsTable topUsedWidgetsTable,
                       DevicesChartInsertion devicesChartInsertion,SessionDrawer sessionDrawer,FontSelection fontSelection) {
        this.requestsChartInsertion = requestsChartInsertion;
        this.logInChartInsertion = logInChartInsertion;
        this.ordersChartInsertion = ordersChartInsertion;
        this.errorsChartInsertion = errorsChartInsertion;
        this.durationOfAnalysis = durationOfAnalysis;
        this.pdfTitle = pdfTitle;
        this.requestsTables = requestsTables;
        this.ordersTable = ordersTable;
        this.errorsTable = errorsTable;
        this.errorsApiTable = errorsApiTable;
        this.logInTables = logInTables;
        this.longestExecutionTimeTable = longestExecutionTimeTable;
        this.mostUsedApisTable = mostUsedApisTable;
        this.topFailureWidgetsTable = topFailureWidgetsTable;
        this.topSuccessfulWidgetsTable = topSuccessfulWidgetsTable;
        this.topUsedWidgetsTable = topUsedWidgetsTable;
        this.devicesChartInsertion = devicesChartInsertion;
        this.sessionDrawer = sessionDrawer;
        this.fontSelection = fontSelection;
    }

    public void generatePDF()
    {
        File file = new File(templatePath);
        try {
            PDDocument document =PDDocument.load(file);

            PDType0Font mediumFont = fontSelection.CeraMediumFont(document);
            PDType0Font boldFont = fontSelection.CeraBoldFont(document);
            PDType0Font blackFont = fontSelection.CeraBlackFont(document);
            PDType0Font lightFont = fontSelection.CeraLightFont(document);
            PDType0Font regularItalicFont = fontSelection.CeraRegularItalicFont(document);

            //First Page
            durationOfAnalysis.addDuration(document,boldFont);
            durationOfAnalysis.addTotal(document,boldFont);
            pdfTitle.addTitle(document,title,lightFont);

            //Inserting Charts
            requestsChartInsertion.insertChart(document);
            logInChartInsertion.insertChart(document);
            ordersChartInsertion.insertChart(document);
            errorsChartInsertion.insertChart(document);
            devicesChartInsertion.insertChart(document);

            sessionDrawer.sessionDrawer(document,mediumFont);

            //Inserting Tables
            requestsTables.createTable(document,mediumFont);
            logInTables.createTable(document,mediumFont);
            errorsTable.createTable(document,mediumFont);
            errorsApiTable.createTable(document,regularItalicFont);
            longestExecutionTimeTable.createTable(document,regularItalicFont);
            mostUsedApisTable.createTable(document,regularItalicFont);
            ordersTable.createTable(document,mediumFont);
            topFailureWidgetsTable.createTable(document,regularItalicFont);
            topSuccessfulWidgetsTable.createTable(document,regularItalicFont);
            topUsedWidgetsTable.createTable(document,regularItalicFont);

            // Close the document when done
            document.save(outputPath);
            document.close();
        }catch (IOException e)
        {
            LOG.error("Error generating PDF",e);
        }

    }

}
