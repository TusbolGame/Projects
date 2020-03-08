from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
from selenium.webdriver.common.by import By
from datetime import datetime
from selenium.webdriver.common.keys import Keys
import os
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains
from selenium.common.exceptions import NoSuchElementException,StaleElementReferenceException,WebDriverException
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from pandas import DataFrame, read_csv
import re
import pandas as pd
import urllib.request
import sys
import csv
import mysql.connector 
class YahooSportScraper(object):
    def __init__(self):
        # opening  web driver

        chromeOptions = webdriver.ChromeOptions()
        chromeOptions.add_argument('--disable-browser-side-navigation')
        prefs={'profile.managed_default_content_settings.images':2,'disk-cache-size': 4096 }
        chromeOptions.add_experimental_option('prefs', prefs)
        # self.driver = webdriver.Chrome(options=chromeOptions)
        self.mydb = mysql.connector.connect(
                host="localhost",
                user="root",
                passwd="",
                database="financetable"
        )
        self.mycursor = self.mydb.cursor()
        self.driver = webdriver.Firefox()
        # set valueable 
        # self.standingInfo =[]

    verification = "" 
    def get_team_standing(self):
        print(self.username)
        sql = "select * from users where username =" +  "'"+self.username+  "'"
        self.mycursor.execute(sql) 
        rows = self.mycursor.fetchall() 
        
        while True:
            self.mycursor.execute(sql) 
            rows = self.mycursor.fetchall() 
            verification = rows[0][3]
            if verification == "verified":
                print("waiting for user")
                self.driver.close()
                exit()          
            if len(rows) == 0:
                self.driver.close()
                print("none Database")
                time.sleep(0.5)
                exit()
            for row in rows :
                try :
                    try:
                        print(row[3])                           
                        self.driver.get("https://www.maybank2u.com.my/home/m2u/common/login.do?sessionTimeout=true")
                        WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//input[@id='username']")))
                        username = self.driver.find_element_by_xpath("//input[@id='username']") 
                        username.send_keys(row[1])
                        loginbutton = self.driver.find_element_by_xpath("//div[2]/div/div/div/div/div[2]/button")  
                        loginbutton.click()
                        WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//div[@class='modal-content']")))
                        buttonYes = self.driver.find_element_by_xpath("//div[2]/div/div[2]/button")
                        buttonYes.click()
                        time.sleep(0.2)
                        password = self.driver.find_element_by_xpath("//input[@id='badge']")
                        password.send_keys(row[2])
                        time.sleep(0.2)
                        WebDriverWait(self.driver, 30).until(EC.element_to_be_clickable((By.XPATH, "//div[2]/div[2]/div/div[2]/button")))
                        loginPress = self.driver.find_element_by_xpath("//div[2]/div[2]/div/div[2]/button")
                        loginPress.click()
                        time.sleep(1)   
                    # self.driver.get("https://www.maybank2u.com.my/home/m2u/common/dashboard/casa?sessionTimeout=true")
                    
                        WebDriverWait(self.driver, 30).until(EC.element_to_be_clickable((By.XPATH, "//*[@id='mainNav']/div[1]/div[2]/ul/li[3]/a")))        
                        self.mycursor.execute("UPDATE users SET loginstatus = 'unverified' WHERE id="+str(row[0]))
                        WebDriverWait(self.driver, 30).until(EC.element_to_be_clickable((By.XPATH, "//*[@id='mainNav']/div[1]/div[2]/ul/li[3]/a")))
                        transferMoneyButton = self.driver.find_element_by_xpath("//*[@id='mainNav']/div[1]/div[2]/ul/li[3]/a")
                        time.sleep(1)
                        transferMoneyButton.click()
                        time.sleep(1)
                        WebDriverWait(self.driver, 30).until(EC.element_to_be_clickable((By.XPATH, "//div[2]/div/div[2]/div/div[2]/div/div")))
                    except:
                        self.mycursor.execute("UPDATE users SET loginstatus = 'verified', htmlTransaction = ' ,,,,,please verrifiy your Manybank Account ' WHERE id="+str(row[0]))
                        self.driver.close()
                        exit()                        
                    transferButton = self.driver.find_element_by_xpath("//div[2]/div/div[2]/div/div[2]/div/div")
                    time.sleep(1)
                    transferButton.click()
                    time.sleep(1)
                    WebDriverWait(self.driver, 30).until(EC.element_to_be_clickable((By.XPATH, "//div[1]/div/div/div[2]/div[2]/div[3]/div/div")))        
                    transferTo = self.driver.find_element_by_xpath("//div[1]/div/div/div[2]/div[2]/div[3]/div/div")
                    time.sleep(1)
                    transferTo.click()
                    time.sleep(1)
                    WebDriverWait(self.driver, 30).until(EC.element_to_be_clickable((By.XPATH, "//div/div/div[2]/div[2]/div[3]/div/div/ul/li[2]/a")))        
                    transferSelect = self.driver.find_element_by_xpath("//div/div/div[2]/div[2]/div[3]/div/div/ul/li[2]/a")
                    time.sleep(1)
                    transferSelect.click()
                    time.sleep(1)
                    WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//*[@id='root']/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div[1]/div/div/div[2]/div[3]/div[2]/input")))
                    self.mycursor.execute("select * from cimbbankhead where bankMode = " + "'"+str(row[4])  + "'") 
                    bankMode = self.mycursor.fetchall()  
                    bankMode = bankMode[0]                   
                    if row[4]=='CIMBBankBerhad':
                        try:
                            bankName = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div[1]/div/div/div[2]/div[3]/div[2]/input")
                            bankName.send_keys(str(bankMode[1]))
                            time.sleep(3)
                            clickElement = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div[1]/div/div/div[2]/div[3]/div[2]/div/ul/li[1]/a")
                            time.sleep(0.1)
                            clickElement.click()
                            time.sleep(0.1)
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[1]/div/div[2]/input")))        
                            accountNumber = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[1]/div/div[2]/input")
                            time.sleep(0.1)
                            accountNumber.send_keys(str(bankMode[2]))
                            time.sleep(0.1)
                            recipientName =self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[2]/div/div[2]/input")
                            time.sleep(0.1)
                            recipientName.send_keys(str(bankMode[3]))
                            time.sleep(0.1)
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[5]/div/div[2]/input")))
                            transferAmount = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[5]/div/div[2]/input")
                            time.sleep(0.1)
                            transferAmount.send_keys(str(row[5]))
                            time.sleep(0.1)
                            receitptName = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[2]/div/div/div[2]/input")
                            time.sleep(0.1)
                            receitptName.send_keys(str(bankMode[4]))
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[4]/div[2]/button")))
                            transferButton = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div/div/div/div/div[2]/div/div[6]/div[2]/button")
                            time.sleep(1)
                            transferButton.click()
                            time.sleep(2)
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[3]/div/div/div/div/div[2]/div/div/div[3]/button")))        
                            transferfinal = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[3]/div/div/div/div/div[2]/div/div/div[3]/button")
                            time.sleep(3)
                            transferfinal.click()
                        except:
                            self.mycursor.execute("UPDATE users SET loginstatus = 'verified', htmlTransaction = ' ,,,,,please verrifiy your Manybank Account ' WHERE id="+str(row[0]))
                            self.driver.close()
                            exit()                                  
                    elif row[4]=='Maybank':
                        try :                       
                            bankName = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div[1]/div/div/div[2]/div[3]/div[2]/input")
                            bankName.send_keys(str(bankMode[1]))
                            time.sleep(3)
                            clickElement = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div[1]/div/div/div[2]/div[3]/div[2]/div/ul/li[1]/a")
                            time.sleep(0.1)
                            clickElement.click()
                            time.sleep(0.1)
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[1]/div/div[2]/input")))        
                            accountNumber = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[1]/div/div[2]/input")
                            time.sleep(0.1)
                            accountNumber.send_keys(str(bankMode[2]))
                            time.sleep(0.1)
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[2]/div/div[2]/input")))
                            transferAmount = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[2]/div/div[2]/input")
                            time.sleep(0.1)
                            transferAmount.send_keys(str(row[5]))
                            time.sleep(0.1)
                            receitptName = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[4]/div/div/div[2]/input")
                            time.sleep(0.1)
                            receitptName.send_keys(str(bankMode[3]))
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "/html/body/div[1]/div/div/div[1]/div/div/div/div/div[2]/div/div[6]/div[2]/button")))
                            transferButton = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div/div/div/div/div[2]/div/div[6]/div[2]/button")
                            time.sleep(1)
                            transferButton.click()
                            time.sleep(2)
                        except:
                            self.mycursor.execute("UPDATE users SET loginstatus = 'verified', htmlTransaction = ' ,,,,,please verrifiy your Manybank Account ' WHERE id="+str(row[0]))
                            self.driver.close()
                            exit()                                  
                    else:
                        try:
                            bankName = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div[1]/div/div/div[2]/div[3]/div[2]/input")
                            bankName.send_keys(str(bankMode[1]))
                            time.sleep(3)
                            clickElement = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div[1]/div/div/div[2]/div[3]/div[2]/div/ul/li[1]/a")
                            time.sleep(0.1)
                            clickElement.click()
                            time.sleep(0.1)
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[1]/div/div[2]/input")))        
                            accountNumber = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[1]/div/div[2]/input")
                            time.sleep(0.1)
                            accountNumber.send_keys(str(bankMode[2]))
                            time.sleep(0.1)
                            recipientName =self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[2]/div/div[2]/input")
                            time.sleep(0.1)
                            recipientName.send_keys(str(bankMode[3]))
                            time.sleep(0.1)
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[5]/div/div[2]/input")))
                            transferAmount = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[1]/div[5]/div/div[2]/input")
                            time.sleep(0.1)
                            transferAmount.send_keys(str(row[5]))
                            time.sleep(0.1)
                            receitptName = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[2]/div/div/div[2]/input")
                            time.sleep(0.1)
                            receitptName.send_keys(str(bankMode[4]))
                            WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[4]/div[2]/button")))
                            transferButton = self.driver.find_element_by_xpath("//*[@id='root']/div/div/div[1]/div/div/div/div/div[2]/div/div[4]/div[2]/button")
                            time.sleep(1)
                            transferButton.click()
                            time.sleep(2)
                        except:
                            self.mycursor.execute("UPDATE users SET loginstatus = 'verified', htmlTransaction = ' ,,,,please verrifiy your Manybank Account ' WHERE id="+str(row[0]))
                            self.driver.close()
                            exit()  

                    WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[3]/div/div/div/div/div[2]/div/div/div[3]/button")))        
                    transferfinal = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[3]/div/div/div/div/div[2]/div/div/div[3]/button")
                    time.sleep(3)
                    transferfinal.click()                        
                        
                    WebDriverWait(self.driver, 30).until(EC.presence_of_element_located((By.XPATH, "/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[3]/div/div/div/div/div[2]/div/div/div[2]/div[2]/input")))        
                    verifyInput = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[3]/div/div/div/div/div[2]/div/div/div[2]/div[2]/input")
                        
                    verificationCode = ""
                    while verificationCode == "": 
                        sql = "select * from users where username = " + "'"+str(row[1])  + "'"
                        self.mycursor.execute(sql) 
                        otherrows = self.mycursor.fetchall() 
                             
                        for otherrow  in otherrows:   
                            if otherrow[6] != "":                                    
                                verificationCode = otherrow[6]
                                break
                            
                        time.sleep(5)
                    verifyInput.send_keys(verificationCode)
                    time.sleep(0.5)
                    self.mycursor.execute("UPDATE users SET verification = '' WHERE id="+str(row[0]))
                    elementConfirmButton = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[3]/div/div/div/div/div[2]/div/div/div[2]/button")
                    elementConfirmButton.click()
                            
                    try:
                        security = ""
                        while security == "": 
                            sql = "select * from users where username = " + "'"+str(row[1])  + "'"
                            self.mycursor.execute(sql) 
                            otherrows = self.mycursor.fetchall() 
                                
                            for otherrow  in otherrows:   
                                if otherrow[7] != "":                                    
                                    security = otherrow[7]
                                    break 
                            time.sleep(3)
                        WebDriverWait(self.driver, 10).until(EC.presence_of_element_located((By.XPATH, "/html/body/div[1]/div/div/div[3]/div/div/div/div[2]/div[2]/div[2]/input")))                                 
                        securityAnswerElement = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[3]/div/div/div/div[2]/div[2]/div[2]/input")
                        securityAnswerElement.send_keys(security)
                        time.sleep(0.5)
                        securityButton = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[3]/div/div/div/div[3]/div/div/button")
                        securityButton.click()
                    except:
                        None
                    try:

                        receiptBank =self.driver.find_element_by_xpath("//div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[1]/div/span[2]/div").text
                        transferType = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[2]/div/span[2]/div").text
                        transferMode = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[3]/div/span[2]/div").text
                        effectiveDate = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[4]/div/span[2]/div").text
                        receiptReference = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[5]/div/span[2]/div").text
                        refereceId = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[6]/span[2]").text
                        status = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[1]/div/div").text
                        transaction = receiptBank +","+ transferType +","+ transferMode +","+effectiveDate +","+receiptReference+","+refereceId+status
                        self.mycursor.execute("UPDATE users SET htmlTransaction = "+"'"+transaction+"'"+" WHERE id="+str(row[0]))
                    except:

                        receiptBank =self.driver.find_element_by_xpath("//div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[1]/div/span[2]/div").text
                        transferType = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[2]/div/span[2]/div").text
                        transferMode = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[3]/div/span[2]/div").text
                        effectiveDate = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[4]/div/span[2]/div").text
                        receiptReference = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[5]/div/span[2]/div").text
                        refereceId = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[2]/div/div[6]/span[2]").text
                        status = self.driver.find_element_by_xpath("/html/body/div[1]/div/div/div[1]/div[1]/div[3]/div/div/div[2]/div/div[2]/div[2]/div/div[1]/div/div").text
                        transaction = receiptBank +","+ transferType +","+ transferMode +","+effectiveDate +","+receiptReference+","+refereceId+status
                        self.mycursor.execute("UPDATE users SET htmlTransaction = "+"'"+transaction+"'"+" WHERE id="+str(row[0]))
                    varifiction ="verified"
                    self.mycursor.execute("UPDATE users SET loginstatus = 'verified' WHERE id="+str(row[0]))
                    self.driver.close()
                    exit()
                    break
                except Exception as e:
                    print(e) 
                    # self.mycursor.execute("UPDATE users SET loginstatus = 'unverified' WHERE id="+str(row[0]))
                    self.driver.close()
                    self.driver = webdriver.Firefox()
                    continue
            exit()
   
    def run(self):
        self.username = 'ooiguanyu'
        self.get_team_standing()
        
        # self.get_email_info()
def runScrapeSports():
    run = YahooSportScraper()
    try:
        run.run()
    except Exception as e:
        print(e)
        return "failed"
    else:
        print("we have done")
        return "success"
if __name__ == "__main__":
    runScrapeSports()  
