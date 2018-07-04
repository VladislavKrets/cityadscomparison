package online.omnia.comparison;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * Created by lollipop on 04.09.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException, InvalidFormatException, ParseException {
        HSSFWorkbook myExcelBook = (HSSFWorkbook) WorkbookFactory.create(new File("C:\\Users\\lollipop\\Downloads\\Telegram Desktop\\excel\\Conversion postback (20).xls"));
        MySQLDaoImpl mySQLDao = MySQLDaoImpl.getInstance();
        PostbackHandler handler = new PostbackHandler();
        HSSFSheet sheet = myExcelBook.getSheet("Report");
        int size = sheet.getLastRowNum();
        HSSFRow row;
        String url;
        String date;
        Date createdDate;
        Date utcDate;
        PostBackEntity postBackEntity;
        HttpMethodsUtils httpMethodsUtils = new HttpMethodsUtils();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat timeZoneFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        timeZoneFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        for (int i = 1; i < size; i++) {
            row = sheet.getRow(i);
                url = row.getCell(4).getStringCellValue().replaceAll(" ", "%20");
                date = row.getCell(5).getStringCellValue();
                createdDate = dateFormat.parse(date);
                utcDate = timeZoneFormat.parse(date);
                postBackEntity = handler.fillPostback(handler.getPostbackParameters(url));
                if (!mySQLDao.isClickidInDb(postBackEntity.getClickId())) {
                    FileWorkingUtils.writeComparison(new java.sql.Date(createdDate.getTime()), new Time(createdDate.getTime()), url);
                    /*httpMethodsUtils.getMethod("http://pb.liverkt.com/" + url, new HashMap<>());
                    mySQLDao.updateDateTime(postBackEntity.getClickId(), new java.sql.Date(utcDate.getTime()), new Time(utcDate.getTime()));
                    */
                    System.out.println("No in db " + postBackEntity.getClickId());
                }


        }
        MySQLDaoImpl.getMasterDbSessionFactory().close();
    }
}
