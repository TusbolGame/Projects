from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains
import time
import ctypes
from tkinter import *
from tkinter import messagebox
import csv


def userinfoWindow():
    global userinfowindow
    userinfowindow = Tk()

    userinfowindow.title('USER INFORMATION')
    userinfowindow.geometry("400x500+1000+200")
    userinfowindow.resizable(False, False)

    label_guide = Label(userinfowindow, text='Please enter your information.', fg='blue')
    label_guide.pack()
    label_guide.config(font=("Courier", 12))
    label_guide.place(x=50, y=100)

    label_userid = Label(userinfowindow, text='USERID')
    label_userid.pack()
    label_userid.config(font=("Courier", 14))
    label_userid.place(x=70, y=150)

    input_userid = Entry(userinfowindow)
    input_userid.pack()
    input_userid.place(x=170, y=150, width=200)
    input_userid.config(font=("Courier", 16))

    label_pwd = Label(userinfowindow, text='PASSWORD')
    label_pwd.pack()
    label_pwd.config(font=("Courier", 14))
    label_pwd.place(x=48, y=200)

    input_pwd = Entry(userinfowindow)
    input_pwd.pack()
    input_pwd.place(x=170, y=200, width=200)
    input_pwd.config(font=("Courier", 16))

    label_amt = Label(userinfowindow, text='AMOUNT')
    label_amt.pack()
    label_amt.config(font=("Courier", 14))
    label_amt.place(x=70, y=250)

    input_amt = Entry(userinfowindow)
    input_amt.pack()
    input_amt.place(x=170, y=250, width=200)
    input_amt.config(font=("Courier", 16))

    btnStart = Button(userinfowindow, text='START', bg="skyblue",
                      command=lambda: validation(input_userid.get(), input_pwd.get(), input_amt.get()))
    btnStart.pack()
    btnStart.place(x=150, y=400, width=100)
    btnStart.config(font=("Courier", 14))

    userinfowindow.mainloop()

def validation(userid,pwd,amt):
    if userid == '' or pwd == '' or amt == '':
        messagebox.showerror('WARNING!', 'You should enter your information exactly !')
    else:
        userinfowindow.destroy()
        run(userid,pwd,amt)

def smsCodeWindow():
    global smscodewindow
    smscodewindow = Tk()
    smscodewindow.title('SMSCODE CONFIRMATION')
    smscodewindow.geometry("330x100+1000+200")
    smscodewindow.resizable(False, False)

    label_sms = Label(smscodewindow, text='SMSCODE :')
    label_sms.pack()
    label_sms.config(font=("Courier", 14))
    label_sms.place(x=20, y=20)

    input_sms = Entry(smscodewindow)
    input_sms.pack()
    input_sms.place(x=140, y=20, width=150)
    input_sms.config(font=("Courier", 14))

    btnStart = Button(smscodewindow, text='CONFIRM', bg="skyblue",
                      command=lambda: validationCode(input_sms.get()))
    btnStart.pack()
    btnStart.place(x=125, y=60, width=100)
    btnStart.config(font=("Courier", 12))

    smscodewindow.mainloop()

def validationCode(smscode):
    global SMS
    if smscode == '':
        messagebox.showerror('WARNING!', 'You should enter your smscode exactly !')
    else:
        smscodewindow.destroy()
        SMS = smscode



def run(uid,pwd,amt):
    chrome_options = webdriver.ChromeOptions()
    prefs = {"profile.default_content_setting_values.notifications": 2}
    chrome_options.add_experimental_option("prefs", prefs)
    chrome_options.add_argument("log-level=3")
    driver = webdriver.Chrome(options=chrome_options)
    driver.maximize_window()

    banknames = ['CIMB', 'MAYBANK', 'BANK KERJASAMA RAKYAT MALAYSIA BERHAD', 'BANK SIMPANAN NASIONAL BERHAD',
                 'AFFIN BANK BERHAD', 'ALLIANCE BANK MALAYSIA BERHAD', 'AMBANK/AMFINANCE',
                 'Al-Rajhi Banking & Investment Corporation (Malaysia) Berhad', 'BANGKOK BANK BERHAD',
                 'BANK ISLAM MALAYSIA BERHAD', 'BANK MUAMALAT MALAYSIA BERHAD', 'BANK OF AMERICA',
                 'BANK OF CHINA (MALAYSIA) BERHAD', 'BANK PERTANIAN MALAYSIA BERHAD (AGROBANK)',
                 'BNP PARIBAS MALAYSIA BERHAD', 'CHINA CONSTRUCTION BANK (MALAYSIA) BERHAD', 'CITIBANK BERHAD',
                 'DEUTSCHE BANK (MALAYSIA) BERHAD', 'HONG LEONG BANK', 'HSBC BANK MALAYSIA BERHAD',
                 'INDUSTRIAL AND COMMERCIAL BANK OF CHINA (MALAYSIA) BERHAD', 'J.P. MORGAN CHASE BANK BHD',
                 'KUWAIT FINANCE HOUSE (MALAYSIA) BERHAD', 'MBSB BANK BERHAD', 'MUFG BANK (MALAYSIA) BERHAD',
                 'Mizuho Corporate Bank Malaysia Berhad', 'OCBC BANK (MALAYSIA) BERHAD',
                 'PUBLIC BANK BERHAD/PUBLIC ISLAMIC BANK BERHAD', 'RHB BANK', 'STANDARD CHARTERED BANK MALAYSIA BERHAD',
                 'SUMITOMO MITSUI BANKING CORPORATION MALAYSIA BERHAD', 'UNITED OVERSEAS BANK BERHAD']

    bank = bank_info[0].upper()
    amount = amt
    userid = uid
    password = pwd
    transactionid = bank_info[1]
    reference = bank_info[2]

    print('processing...')

    driver.get("https://www.cimbclicks.com.my/clicks/#/")
    WebDriverWait(driver, 120).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='googlerecaptcha']")))

    js = "var myNode = document.getElementById('googlerecaptcha');while (myNode.firstChild) {myNode.removeChild(myNode.firstChild);}"
    driver.execute_script(js)

    driver.find_element_by_id("user-id").send_keys(userid)
    time.sleep(0.5)
    driver.find_element_by_xpath("//button[@class='btn-primary btn-block btn-next googleCapthaCls']").click()
    time.sleep(2)

    secure_chk = driver.find_element_by_class_name("secure-word-label")
    actions = ActionChains(driver)
    actions.move_to_element(secure_chk).perform()
    driver.execute_script("arguments[0].click();", secure_chk)
    time.sleep(1)
    driver.find_element_by_id("password").send_keys(password)
    driver.find_element_by_xpath("//button[@class='btn-primary btn-block btn-login']").click()

    while True:
        try:
            paytransfer = driver.find_element_by_xpath(
                "//ul[@class='menu-item-inner']/li[@class='pay-transfer has-sub-menu']/a[@class='collapsed']")
            paytransfer.click()
            break
        except:
            time.sleep(2)

    time.sleep(1)
    driver.find_element_by_xpath(
        "//ul[@class='pay-sub-menu sub-menu menu-item-inner padding-10-10 active']/div/div[1]/ul/li[1]/a").click()
    time.sleep(5)

    driver.find_element_by_xpath("//input[@class='input-text select2 android-fixes']").click()
    time.sleep(3)
    driver.find_element_by_xpath("//input[@class='input-text select2 android-fixes']").send_keys(transactionid)
    time.sleep(2)
    driver.find_element_by_xpath("//span[@class='send-money-account']").click()
    time.sleep(2)
    if bank_info[0] == 'Cimb bank berhad':
        try:
            driver.find_element_by_xpath("//a[@title='Transfer money within CIMB Bank Malaysia.']").click()
            time.sleep(2)
        except:
            pass
    else:
        for realbankname in banknames:
            if bank in realbankname:
                bank = realbankname
        print(bank)

        driver.find_element_by_xpath("//a[@title='Transfer money to other local banks.']").click()
        time.sleep(2)
        banktags = driver.find_elements_by_xpath("//div[@class='input-cmp']")
        banktags[2].click()
        time.sleep(2)
        options = driver.find_elements_by_xpath("//li[@class='select2-results__option']")
        for i in options:
            if bank in i.text:
                i.click()
                time.sleep(2)
                transfers = driver.find_elements_by_xpath("//label[@class='cimb-radio margin-zero top']")
                transfers[len(transfers) - 1].click()
                time.sleep(1)
                banktags = driver.find_elements_by_xpath("//div[@class='input-cmp']")
                banktags[3].click()
                time.sleep(1)
                types = driver.find_elements_by_xpath(
                    "//ul[@class='select2-results__options scroll-content']/li")
                types[0].click()
                time.sleep(1)
                break
    driver.find_element_by_xpath("//input[@name='amount']").send_keys(amount)
    time.sleep(1)
    driver.find_element_by_xpath("//input[@name='rep-refer']").send_keys(reference)
    time.sleep(1)
    driver.find_element_by_xpath("//button[@class='btn btn-primary scroll']").click()
    time.sleep(5)
    returnvalue = driver.find_element_by_xpath("//div[@class='content col-xs-12 text-left clearfix']")
    if 'Invalid' in returnvalue.text:
        time.sleep(1)
        driver.find_element_by_xpath(
            "//a[@class='btn btn-primary btn-full btn-half-left btn-yes cursor']").click()
        time.sleep(3)
        ctypes.windll.user32.MessageBoxW(0, "Unfortunately... Failed ! Please try again.", "FAILURE!", 0)
    driver.find_element_by_xpath("//a[@class='link-text link-text-cus']").click()
    time.sleep(5)

    while True:

        smsCodeWindow()

        driver.find_element_by_xpath("//input[@class='input-tac-number js-tac-sms']").click()
        time.sleep(2)
        driver.find_element_by_xpath("//input[@class='input-tac-number js-tac-sms']").send_keys(SMS)
        time.sleep(2)
        driver.find_element_by_xpath("//button[@class='btn btn-primary']").click()
        time.sleep(5)
        returnvalue = driver.find_element_by_xpath(
            "//div[@class='content col-xs-12 text-left clearfix']")
        if 'Invalid' in returnvalue.text:
            time.sleep(1)
            driver.find_element_by_xpath(
                "//a[@class='btn btn-primary btn-full btn-half-left btn-yes cursor']").click()
            time.sleep(4)
            ctypes.windll.user32.MessageBoxW(0, "Unfortunately... Failed ! Please try again.", "FAILURE!", 0)
            time.sleep(2)
        else:
            ctypes.windll.user32.MessageBoxW(0, "Congratulation... Success !", "SUCCESS!", 0)
            break

    time.sleep(5)

    driver.close()
    return "success"
    exit()

if __name__ == '__main__':
    bank_info = []
    with open("bankinfo.txt","r") as file:
        reader = csv.reader(file, delimiter=':', quoting=csv.QUOTE_NONE)
        for line in reader:
            bank_info.append(line[1].strip())
    userinfoWindow()

