package logs_analysis_tool.example.analysis_tool.vaildation;

import java.io.File;
public class PathsValidation {
    public static boolean isValidDirectoryPath(String path) {
        File directory = new File(path);
        if (directory.exists() && directory.isDirectory()){
            return true;
        } else {
            return false;
        }
    }
}
