package logs_analysis_tool.example.analysis_tool.utils;

import lombok.Getter;

//please note that heap size if set less than 4096MB you may encounter memory dying inside your app issues
public class Config {
    private static String passedDirectoryPath = "D:\\task2 AnalysisTool 3-5-2024\\logs\\platformAudit_cleared.log1 - TEST";

    public static String getPassedDirectoryPath() {
        return passedDirectoryPath;
    }

    public static void setPassedDirectoryPath(String passedDirectoryPath) {
        Config.passedDirectoryPath = passedDirectoryPath;
    }

}
