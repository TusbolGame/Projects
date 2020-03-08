from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import Select
import time
import ctypes
from tkinter import *
from tkinter import messagebox
import csv
from multiprocessing import Process, Queue, Pool



def userinfoWindow():
    global userinfowindow
    userinfowindow = Tk()

    userinfowindow.title('USER INFORMATION')
    userinfowindow.geometry("400x500+1000+200")
    userinfowindow.resizable(False, False)

    label_guide = Label(userinfowindow, text='Welcome To OCBC Bank', fg='green')
    label_guide.pack()
    label_guide.config(font=("Courier", 16))
    label_guide.place(x=70, y=50)

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

    label_pwd = Label(userinfowindow, text='PIN')
    label_pwd.pack()
    label_pwd.config(font=("Courier", 14))
    label_pwd.place(x=70, y=200)

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
        main(userid,pwd,amt)

def oneTimePwdWindow():
    global onetimepwdwindow
    onetimepwdwindow = Tk()
    onetimepwdwindow.title('ONE TIME PASSWORD')
    onetimepwdwindow.geometry("500x150+1000+200")
    onetimepwdwindow.resizable(False, False)

    label_ans = Label(onetimepwdwindow, text='One Time Password :')
    label_ans.pack()
    label_ans.config(font=("Courier", 14))
    label_ans.place(x=20, y=50)

    input_ans = Entry(onetimepwdwindow)
    input_ans.pack()
    input_ans.place(x=240, y=50, width=200)
    input_ans.config(font=("Courier", 14))

    btnStart = Button(onetimepwdwindow, text='SUBMIT', bg="skyblue",
                      command=lambda: validationPwd(input_ans.get()))
    btnStart.pack()
    btnStart.place(x=300, y=100, width=100)
    btnStart.config(font=("Courier", 12))

    onetimepwdwindow.mainloop()

def validationPwd(otp):
    global OTP
    if otp == '':
        messagebox.showerror('WARNING!', 'You should enter your One Time Password exactly !')
    else:
        onetimepwdwindow.destroy()
        OTP = otp


def tapWindow1():
    global tapwindow1
    tapwindow1 = Tk()
    tapwindow1.title('TAP Window')
    tapwindow1.geometry("400x500+1000+200")
    tapwindow1.resizable(False, False)
    label_guide = Label(tapwindow1, text='Please TAP', fg='green')
    label_guide.pack()
    label_guide.config(font=("Courier", 16))
    label_guide.place(x=100, y=50)
    tapwindow1.after(500, firstProcess)
    tapwindow1.mainloop()


def tapWindow2():
    global tapwindow2
    tapwindow2 = Tk()
    tapwindow2.title('TAP Window')
    tapwindow2.geometry("400x500+1000+200")
    tapwindow2.resizable(False, False)
    label_guide = Label(tapwindow2, text='Please TAP', fg='green')
    label_guide.pack()
    label_guide.config(font=("Courier", 16))
    label_guide.place(x=100, y=50)
    tapwindow2.after(500, secondProcess)
    tapwindow2.mainloop()

def firstProcess():
    WebDriverWait(driver, 300).until(
        EC.invisibility_of_element_located((By.XPATH, '//*[@id="AuthSection"]/div[2]/p[4]/a'))
    )
    destroyWindow1()
    afterTap1()
def secondProcess():
    WebDriverWait(driver, 300).until(
        EC.invisibility_of_element_located((By.XPATH, '//*[@id="AuthSection"]/div[2]/p[4]/a'))
    )
    destroyWindow2()
    afterTap2()
def afterTap1():

    print("Login Successful...")

    WebDriverWait(driver, 300).until(
        EC.visibility_of_element_located((By.XPATH, '//*[@id="main-nav"]/div/div/ul/li[2]/a')))

    hover = ActionChains(driver).move_to_element(driver.find_element_by_xpath('//*[@id="main-nav"]/div/div/ul/li[2]/a'))
    hover.perform()
    time.sleep(1)
    to_an_account_btn = driver.find_element_by_xpath("//*[@id='main-nav']/div/div/ul/li[2]/div/div/div/div[1]/div/ul/li[1]/a")
    to_an_account_btn.click()

    WebDriverWait(driver, 300).until(
        EC.visibility_of_element_located(
            (By.XPATH, '//*[@id="ApplicationForm"]/div[3]/div/div[2]/div[2]/div/div/div[2]/label[3]')))
    someoneElseOCBC_btn = driver.find_element_by_xpath('//*[@id="other-ocbc"]')
    someoneElseOCBC_btn.click()

    WebDriverWait(driver, 300).until(
        EC.visibility_of_element_located(
            (By.XPATH, '//*[@id="btnSubmit"]/span/span/a')))
    amount_field = driver.find_element_by_xpath('//*[@id="beneList_0__Amount"]')
    amount_field.send_keys(amount)

    next_btn = driver.find_element_by_xpath('//*[@id="btnSubmit"]/span/span/a')
    next_btn.click()

    WebDriverWait(driver, 300).until(
        EC.visibility_of_element_located(
            (By.XPATH, '//*[@id="OTPModalTrigger"]')))
    submit_btn = driver.find_element_by_xpath('//*[@id="OTPModalTrigger"]')
    submit_btn.click()

    WebDriverWait(driver, 300).until(
        EC.visibility_of_element_located((By.ID, "countdown-timer")))

    counter_timer = driver.find_element_by_id("countdown-timer")


    remain_time = int(counter_timer.text)
    print("Waiting for TAP...")
    if remain_time > 0:
        tapWindow2()

def afterTap2():
    WebDriverWait(driver, 300).until(
        EC.invisibility_of_element_located((By.XPATH, '//*[@id="AuthSection"]/div[2]/p[4]/a'))
    )
    time.sleep(2)
    try:
        error_tag = driver.find_element_by_id('ErrorMsg')
        error_tag.click()
        failureWindow()
        driver.close()
        exit()

    except:
        WebDriverWait(driver, 300).until(
            EC.visibility_of_element_located(
                (By.XPATH, '//*[@id="global-notification"]/div/div/p')))
        result = driver.find_element_by_xpath('//*[@id="global-notification"]/div/div/p')
        if 'successful' in result.text:
            successWindow()
        else:
            failureWindow()
def successWindow():
    global successwindow
    successwindow = Tk()

    successwindow.title('Success Window')
    successwindow.geometry("400x500+1000+200")
    successwindow.resizable(False, False)

    label_guide = Label(successwindow, text='Successful.', fg='green')
    label_guide.pack()
    label_guide.config(font=("Courier", 16))
    label_guide.place(x=100, y=50)
    successwindow.mainloop()

def failureWindow():
    global failurewindow
    failurewindow = Tk()

    failurewindow.title('Failed Window')
    failurewindow.geometry("400x500+1000+200")
    failurewindow.resizable(False, False)

    label_guide = Label(failurewindow, text='Failed.', fg='red')
    label_guide.pack()
    label_guide.config(font=("Courier", 16))
    label_guide.place(x=100, y=50)
    failurewindow.mainloop()

def destroyWindow1():
    tapwindow1.destroy()
def destroyWindow2():
    tapwindow2.destroy()
def main(uid,pwd,amt):
    chrome_options = webdriver.ChromeOptions()
    prefs = {"profile.default_content_setting_values.notifications": 2}
    chrome_options.add_experimental_option("prefs", prefs)
    chrome_options.add_argument("log-level=3")
    global driver
    driver = webdriver.Chrome(options=chrome_options)
    driver.maximize_window()

    bank = "https://internet.ocbc.com/internet-banking/Login/Login"
    global amount
    amount = amt
    userid = uid
    password = pwd
    print('processing...')

    # try:
    driver.get(bank)
    WebDriverWait(driver, 300).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='LoginForm']/div[3]/div[3]/div/span/span/input")))
    time.sleep(2)

    uid_box = driver.find_element_by_id("access-code")
    pin_box = driver.find_element_by_id("access-pin")
    uid_box.send_keys(userid)
    pin_box.send_keys(password)
    time.sleep(1)
    login_btn = driver.find_element_by_xpath("//*[@id='LoginForm']/div[3]/div[3]/div/span/span/input")
    login_btn.click()
    print("Waiting TAP...")
    WebDriverWait(driver, 300).until(
        EC.visibility_of_element_located((By.ID, "countdown-timer")))

    counter_timer = driver.find_element_by_id("countdown-timer")
    remain_time = int(counter_timer.text, base=10)
    if remain_time > 0:
        tapWindow1()

    time.sleep(5)
    driver.close()
    exit()



if __name__ == "__main__":
    userinfoWindow()