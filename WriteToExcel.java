import java.io.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
  
public class WriteToExcel {
  
    // any exceptions need to be caught
    public static void main(String[] args) throws Exception
    {
        File file = new File("userData.xlsx");
        FileInputStream fip = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(fip);
  
     // spreadsheet object
        XSSFSheet spreadsheet
            = workbook.createSheet("user Data");
        
        // creating a row object
        XSSFRow row;
  
        // This data needs to be written (Object[])
        Map<String, Object[]> userData
            = new TreeMap<String, Object[]>();
  
        userData.put(
            "1",
            new Object[] { "ID", "Username", "Password" });
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username");
        String username = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        
        sc.close();
        
        userData.put("2", new Object[] {"101", username, password});
        Set<String> keyid = userData.keySet();
  
        int rowid = 0;
  
        // writing the data into the sheets...
  
        for (String key : keyid) {
  
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = userData.get(key);
            int cellid = 0;
  
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }
  
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(
            new File("userData.xlsx"));
  
        workbook.write(out);
        out.close();
    }
}