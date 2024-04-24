package logs_analysis_tool.example.analysis_tool.pdf.tables;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;

public interface Tables {

    public void createTable(PDDocument document, PDType0Font font) throws IOException;
}
