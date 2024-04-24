package logs_analysis_tool.example.analysis_tool.pdf.charts_insertion;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.IOException;

public interface ChartsInsertion {

    public void insertChart(PDDocument document) throws IOException;

}
