import junit.framework.Assert;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.json.JsonOutput;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class TestTempRSHB {
    WebDriver driver;
    WebElement element;
    WebElement slider;
    WebDriverWait driverWait;

    @Test
    public void testCase01() {

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebDriverWait driverWait = new WebDriverWait(driver,30);
        //WebDriverWait driverWait = new WebDriverWait(driver,30).until(ExpectedConditions.visibilityOf(element));
        driver.get("https://www.rshb.ru/");

        /*
        работа со всплывающими окнами
        // Alert alert = driver.switchTo().alert();
        Alert alert = (new WebDriverWait(driver, 10)).until(ExpectedConditions.alertIsPresent());
        alert.dismiss();
        alert accept();
         */

        // ожидаем окно принятие cookies
        System.out.println("Cookie agreement window");
        //createPause();
        element = driver.findElement(By.xpath("//*[@id=\"alert\"]/div/div/div[2]/button"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        System.out.println("Step_01");
        System.out.println("Individuals");
        //createPause();
        // Частным лицам
        // $$("div.b-main-menu-items [href='/natural/']")
        element = driver.findElement(By.cssSelector("div.b-main-menu-items [href='/natural/']"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();
        // driver.findElement(By.xpath("/html/body/div[6]/div[1]/header/nav/div[1]/a[1]")).click();

        System.out.println("Credits");
        // createPause();
        // $$("div.b-page-head [href='/natural/loans/']")
        // "div.b-page-head [href='/natural/loans/']"
        element = driver.findElement(By.cssSelector("div.b-page-head [href='/natural/loans/"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // скрыт alert-ом
        System.out.println("Loan without collateral");
        //createPause();
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div/div[2]/div[5]/div[1]/div[1]/a"));
        driverWait.until(ExpectedConditions.visibilityOf(element));

        // проверим что элемент видимый
        if(element.isDisplayed()){
            System.out.println("Element is Visible");
        } else {
            System.out.println("Element is InVisible");
        }

        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            e.printStackTrace();
            System.out.println("!!! Элемент не кликабельный");
        }

        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        driver.manage().deleteAllCookies();
        // указываем какой скрипт необходимо выполнить. Необходимо почистить все сессионные данные,
        // которые находятся в определенном окне
        javascriptExecutor.executeScript("window.sessionStorage.clear()");
        createPause();

        // ссылка на кнопку понятно
        //button[@class='cookie-consent__submit-button button button__white']
        element = driver.findElement(By.xpath("//button[@class='cookie-consent__submit-button button button__white']"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // двигаем ползунок в сторону 2 500 000 пока не достигнем результата
        String s;
        slider = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[3]/div/div[2]/div[4]"));
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[3]/div/input"));

        s = element.getAttribute("value");
        String s1;
        s1 = determinePositionSlider(s);

        System.out.println("Calculation of monthly payment");
        System.out.println(s);
        System.out.println(s1);
        int sm;
        sm = Integer.parseInt(s1);
        while (sm != 2500000) {
            if (sm < 2500000) {
                slider.sendKeys(Keys.ARROW_RIGHT);
                s = element.getAttribute(("value"));
                s1 = determinePositionSlider(s);
                sm = Integer.parseInt(s1);
            } else {
                slider.sendKeys(Keys.ARROW_LEFT);
                s = element.getAttribute(("value"));
                s1 = determinePositionSlider(s);
                sm = Integer.parseInt(s1);
            }
        }

        // введем количество месяцев для рассчета кредита
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[4]/div/input"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        System.out.println("Enter period");
        element.clear();
        // createPause();
        element.sendKeys("60");

        // Установич checkbox согласно заданию, певый isSelected, второй и третий is Diselected
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[1]/label/span/input"));
        if (!element.isSelected()) {
            element.click();
        }
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[2]/label/span/input"));
        if (element.isSelected()) {
            element.click();
        }
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[3]/label/span/input"));
        if (element.isSelected()) {
            element.click();
        }

        // проверим что ежемесячный платеж составляет 53061 Р
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[2]/div/div[2]/div[1]/div[1]"));
        s = element.getText();
        System.out.println(s);
        Assert.assertEquals(s, "56 118 ₽");

        // проверим что процентная ставка составляет 12.4%
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[2]/div/div[2]/div[2]/div[1]"));
        s = element.getText();
        System.out.println(s);
        Assert.assertEquals(s, "12.4%");

        System.out.println("Test is closed");
        driver.close();
    }

    @Test
    public void testCase02() {

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        WebDriverWait driverWait = new WebDriverWait(driver,30);
        //WebDriverWait driverWait = new WebDriverWait(driver,30).until(ExpectedConditions.visibilityOf(element));
        driver.get("https://www.rshb.ru");

        // кликнуть "Частным лицам"
        System.out.println("Individuals");
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/header/nav/div[1]/a[1]"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // кликнуть "Кредиты"
        System.out.println("Credits");
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div/div[2]/div[1]/nav/a[3]"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // кликнуть "Потребительский кредит без обеспечения"
        System.out.println("consumer credit without collateral");
        element = driver.findElement(By.xpath("/html/body/div[6]/div[1]/div[2]/div/div[2]/div[5]/div[1]/div[1]/a"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // тип платежа выбрать "Аннуитентный"
        System.out.println("Annuity");
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[2]/div[2]/div[2]/div/button[1]"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // установим сумму кредита 2 500 000 р
        System.out.println("Let's choose the loan amount 2 500 000 Р");
        element = driver.findElement(By.xpath(""));


        /*
        // закроем окно принятия cookies
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/button"));
        driverWait.until(ExpectedConditions.visibilityOf(element));
        element.click();

        // Установич checkbox согласно заданию
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[1]/label/span/input"));
        if (!element.isSelected()) {
            element.click();
        }

        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[2]/label/span/input"));
        if (element.isSelected()) {
            element.click();
        }

        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[1]/div/div/div[4]/div[3]/label/span/input"));
        if (element.isSelected()) {
            element.click();
        }

        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[2]/div/div[2]/div[1]/div[1]"));
        //element.getText


        // проверим что ежемесячный платеж составляет 53061 Р
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[2]/div/div[2]/div[1]/div[1]"));
        String s;
        s = element.getText();
        System.out.println(s);
        Assert.assertEquals(s, "53 061 ₽");

        // проверим что процентная ставка составляет 12.4%
        element = driver.findElement(By.xpath("//*[@id=\"root\"]/div[1]/div[2]/div[4]/div[2]/div/div[2]/div[2]/div[1]"));
        s = element.getText();
        System.out.println(s);
        Assert.assertEquals(s, "12.4%");
         */

        driver.close();

    }


    // иетод задания паузы
    public void createPause() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // выберем только цифры из поля убрав символы форматирования
    public String determinePositionSlider(String s) {
        String s1 = "";
        char sT;
        int j =  0, a;
        a = s.length();
        while (j < a) {
            sT = s.charAt(j);
            if (sT == '0' || sT == '1' || sT == '2' || sT == '3' || sT == '4' || sT == '5' || sT == '6' || sT == '7' ||
                    sT == '8' || sT == '9') {
                s1 = s1 + sT;
            }
            j++;
        }
        return s1;
    }


}