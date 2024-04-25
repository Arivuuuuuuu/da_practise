package mini_project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Project {
	
	static WebDriver driver;
	static String dashboard;
	static String jobtitles;
	static String jobs="\n";
	
	        public void WebDriverSetup(String browser_name) {
		     
            String browser=browser_name.toLowerCase();
            switch(browser){
        
		    //Launching chrome Browser
			case "chrome":
				driver = new ChromeDriver();				
				break;
				
		    //Launching Edge browser
			case "edge":
				driver = new EdgeDriver();			
				break;
				
		    //Launching FireFox browser	
			case "firefox":
				 driver = new FirefoxDriver();
				 break;
                }
		
           }

		    public XSSFRow readExcel() throws IOException  {
		        File file = new File("C:\\Users\\2318333\\eclipse-workspace\\maven_practise\\mini_project.xlsx");
		    	
		        FileInputStream fis = new FileInputStream(file);
		        XSSFWorkbook workbook = new XSSFWorkbook(fis);
		        XSSFSheet sheet = workbook.getSheet("Sheet1");
		        XSSFRow row=sheet.getRow(1);
		        XSSFCell cell;
		        workbook.close();
		        fis.close();
		        return row;
		    }

		    public String login( XSSFRow row) throws InterruptedException {
		    	
		        driver.get("https://opensource-demo.orangehrmlive.com/");
		        driver.manage().window().maximize();
		        
		        Thread.sleep(7000);

		        driver.findElement(By.xpath("//input[@placeholder='Username']")).sendKeys(row.getCell(0).toString());
		        driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(row.getCell(1).toString());
		        driver.findElement(By.xpath("//button")).click();

		        String act_url = driver.getCurrentUrl();
		        if (act_url.contains("dashboard")) {
		            dashboard = "Yes!! The current URL contains dashboard ";
		        } else {
		            dashboard= "No!!";
		        }
		        return dashboard + "\n";
		    }

		    public void navigateToAdminTab() throws InterruptedException {
		    	Thread.sleep(5000);
		        driver.findElement(By.xpath("//span[text()='Admin']")).click();
		    }

		    public void navigateToJobTab() throws InterruptedException {
		    	Thread.sleep(3000);
		        driver.findElement(By.xpath("//li[2]//span[@class='oxd-topbar-body-nav-tab-item']")).click();
		        
		    }

		    public String checkJobTitles() throws InterruptedException {
		    	
		        WebElement we = driver.findElement(By.xpath("//li[1]/a[@class='oxd-topbar-body-nav-tab-link']"));
		        Thread.sleep(3000);

		        if (we.isDisplayed()) {
		            jobtitles = "\nYes! JobTitles is there";
		        } else {
		            jobtitles = "Nope! JobTitles is not there";
		        }
		        return  jobtitles;
		    }

		    public String getJobList() throws InterruptedException {
		    	
		        driver.findElement(By.xpath("//li[1]//a[@class='oxd-topbar-body-nav-tab-link']")).click();
		        Thread.sleep(3000);

		        List<WebElement> option = driver.findElements(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[3]/div[1]/div[2]/div/div[1]/div[2]/div[1]"));
		        //System.out.println("size:" + option.size());

		        for (int i = 1; i < option.size(); i++) {
		            WebElement wel = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/div[2]/div[2]/div[1]/div[1]/div[3]/div[1]/div[2]/div[" + i + "]/div[1]/div[2]/div[1]"));
		            jobs += wel.getText()+ "\n";
		        }
				
				return jobs;
		    }

		    public void addJob( XSSFRow row) throws InterruptedException {
		    	Thread.sleep(3000);
		        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/div[1]/div/button")).click();
		        
		        Thread.sleep(7000);
		        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div[2]/div[2]/div/div/form/div[1]/div/div[2]/input")).sendKeys(row.getCell(2).toString());
		        driver.findElement(By.xpath("//button[@type='submit']")).click();
		        driver.findElement(By.xpath("//p[@class='oxd-userdropdown-name']")).click();
		        driver.findElement(By.xpath("//li[4]//a[@class='oxd-userdropdown-link']")).click();
		    }
		    
		    public void closeBrowser() throws InterruptedException {
		    	Thread.sleep(3000);
		    	driver.quit();
		    }
		    
		    public static void main(String[] args) throws FileNotFoundException {
		    	
		    	Scanner sc=new Scanner(System.in);

				System.out.println("Enter the browser name: Chrome /edge/ Firefox");
				String browser=sc.nextLine();	
				
				Project obj=new Project();
				
				PrintStream ps=new PrintStream(new File("C:\\Users\\2318333\\eclipse-workspace\\maven_practise\\result.txt"));
				System.setOut(ps);	
				
		        try {
		        	
		        	obj.WebDriverSetup(browser);
		        	
		        	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
		        	
		            XSSFRow row = obj.readExcel();	            
		            obj.login(row);
		            obj.navigateToAdminTab();
		            obj.navigateToJobTab();
		            obj.checkJobTitles();
		            obj.getJobList();
		            obj.addJob(row);
		            obj.closeBrowser();
		            ps.print(dashboard );
		            ps.print(jobtitles);
		            ps.print(jobs);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }

}
