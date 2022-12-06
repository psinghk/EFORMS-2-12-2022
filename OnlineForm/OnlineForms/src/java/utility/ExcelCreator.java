package utility;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelCreator {

    public void createWorkbook(ArrayList userList) throws Exception {
        String excelFileName = "E:\\error.xls";//name of excel file

        String sheetName = "Sheet1";//name of sheet

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);

        HSSFRow row = sheet.createRow(0);

        int i = 0;
        for (Object r : userList) {
            HSSFCell cell = row.createCell(i);

            cell.setCellValue(r.toString());

            i++;
        }

        //iterating r number of rows
        /*for (int r=0;r < 5; r++ )
		{
			HSSFRow row = sheet.createRow(r);
	
			//iterating c number of columns
			for (int c=0;c < 5; c++ )
			{
				HSSFCell cell = row.createCell(c);
				
				cell.setCellValue("Cell "+r+" "+c);
			}
		}*/
        FileOutputStream fileOut = new FileOutputStream(excelFileName);

        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
    }

    public void createWorkbookHash(ArrayList arr, String excelFileName, String sheetName) throws Exception {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);

        HSSFRow row = sheet.createRow(0);

        if (arr != null && arr.size() > 0) // if block added by pr on 28thjan2020
        {
        
        HashMap fieldHM = (HashMap) arr.get(0); // to get the keys as fields of teh excel

        int i = 0;

        for (Object k : fieldHM.keySet())// iterating through the first hashmap element of arraylist
        {
            //String key = fieldHM.get(k).toString();

            HSSFCell cell = row.createCell(i);

            cell.setCellValue(k.toString());// create cells with key as titles

            i++;
        }

        i = 1;

        HashMap valueHM = null;

        for (Object r : arr)// iterating through the complete arraylist for all teh hashmaps
        {
            row = sheet.createRow(i);// start from 1 because 0th rows has the details abt the field names or column names

            valueHM = (HashMap) r;// travesring the elements of arraylist i.e each hashmap one by one

            int j = 0;

            for (Object k : valueHM.keySet())// iterating through the first and all hashmap element of arraylist
            {
                HSSFCell cell = row.createCell(j);

                String key = "", val = "";

                if (k != null) {
                    key = k.toString();

                    if (valueHM.get(k.toString()) != null) {
                        val = valueHM.get(k.toString()).toString();
                    }

                }

                if (!val.equals("")) {
                    cell.setCellValue(val);
                }

                j++;
            }

            i++;
        }

        FileOutputStream fileOut = new FileOutputStream(excelFileName);

        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        
        }
    }

    public void createCSV(ArrayList arr, String csvFileName, String sheetName) {
        try {
            HashMap fieldHM = (HashMap) arr.get(0); // to get the keys as fields of teh excel
            CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFileName), ',');

            List<String> title = new ArrayList<String>();
            for (Object k : fieldHM.keySet())// iterating through the first hashmap element of arraylist
            {
                title.add((String) k);
            }

            String[] header = new String[title.size()];
            header = title.toArray(header);
            csvWriter.writeNext(header);  // add title

            // add data
            HashMap valueHM = null;
            List<String[]> allData = new ArrayList<String[]>();
            List<String> rowDATA = new ArrayList<String>();
            for (Object r : arr)// iterating through the complete arraylist for all teh hashmaps
            {
                valueHM = (HashMap) r;// travesring the elements of arraylist i.e each hashmap one by one
                rowDATA.clear();
                for (Object k : valueHM.keySet())// iterating through the first and all hashmap element of arraylist
                {
                    String val = "";
                    if (k != null) {                       
                        if (valueHM.get(k.toString()) != null) {
                            val = valueHM.get(k.toString()).toString();
                        }
                    }
                  //  if (!val.equals("")) {
                        rowDATA.add(val);
                //    }
                }
                String[] rowcontent = new String[rowDATA.size()];
                rowcontent = rowDATA.toArray(rowcontent);
                allData.add(rowcontent);
            }
            csvWriter.writeAll(allData);
            csvWriter.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
