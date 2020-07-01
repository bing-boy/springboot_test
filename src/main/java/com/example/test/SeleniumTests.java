package com.example.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.SpringbootStudyApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootStudyApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
//@WebIntegrationTest //已被废弃,在@SpringBootTest注解中用webEnvironment
public class SeleniumTests {

	private static Logger logger = LoggerFactory.getLogger(SeleniumTests.class);

	private static ChromeDriver browser;

	@Value("${local.server.port}")
	private int port;

	@BeforeClass
	public static void openBrowser() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\bing\\AppData\\Local\\Google\\Chrome\\Driver\\chromedriver.exe");
		browser = new ChromeDriver();
		browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@AfterClass
	public static void closeBrowser() {
		//browser.quit();
	}

	@Test
	public void addUserToList() {
		String baseUrl = "http://localhost:" + port + "/user/";

		browser.get(baseUrl);

		assertEquals("no user", browser.findElementByTagName("div").getText());

		browser.findElementByName("name").sendKeys("User Name");
		browser.findElementByTagName("form").submit();
	}

}
