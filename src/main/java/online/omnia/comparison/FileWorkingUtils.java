package online.omnia.comparison;


import java.io.*;
import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lollipop on 12.07.2017.
 */
public class FileWorkingUtils {
    private static FileWriter comparisonURLWriter;
    private static FileWriter errorcomparisonURLWriter;
    private static BufferedReader fileReader;

    static {
        try {
            File postbackDirectory = new File("comparison");
            if (!postbackDirectory.exists()) postbackDirectory.mkdir();
            File file = new File("postback/comparison.log");
            if (!file.exists()) file.createNewFile();

            comparisonURLWriter = new FileWriter(file, true);
            file = new File("postback/error_comparison.log");
            if (!file.exists()) file.createNewFile();
            errorcomparisonURLWriter = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static synchronized Map<String, String> iniFileReader() {
        Map<String, String> properties = new HashMap<>();
        try {
            fileReader = new BufferedReader(new FileReader("configuration.ini"));
            String property;
            String[] propertyArray;
            while ((property = fileReader.readLine()) != null) {
                if (property.trim().isEmpty() || property.trim().startsWith("#")) continue;
                propertyArray = property.split("=");
                properties.put(propertyArray[0], propertyArray[1]);
            }
        } catch (IOException e) {
        }
        return properties;
    }
    public static synchronized void writeComparison(Date date, Time time, String fullUrl){
        String line = buildLine(date, time, fullUrl);
        try {
            comparisonURLWriter.write(line);
            comparisonURLWriter.flush();
        } catch (IOException e) {
        }
    }

    public static synchronized void writeErrorComparison(Date date, Time time, String fullUrl) {
        String line = buildLine(date, time, fullUrl);
        try {
            errorcomparisonURLWriter.write(line);
            errorcomparisonURLWriter.flush();
        } catch (IOException e) {
        }
    }
    private static String buildLine(Date date, Time time, String fullUrl) {
        StringBuilder lineBuilder = new StringBuilder();
        lineBuilder.append(date.toString())
                .append(" ")
                .append(time.toString())
                .append(" ")
                .append(fullUrl)
                .append("\n");
        return lineBuilder.toString();
    }
}
