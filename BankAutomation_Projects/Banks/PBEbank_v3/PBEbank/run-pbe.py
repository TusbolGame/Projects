
"""
Author : Li G
Created Date : June 20, 2019
"""

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

def userinfoWindow():
    global userinfowindow
    userinfowindow = Tk()

    userinfowindow.title('USER INFORMATION')
    userinfowindow.geometry("400x500+1000+200")
    userinfowindow.resizable(False, False)

    label_guide = Label(userinfowindow, text='Welcome To PBE Bank', fg='green')
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
        main(userid,pwd,amt)

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

def answerWindow(quiz):
    global answerwindow
    answerwindow = Tk()
    answerwindow.title('USER ANSWER')
    answerwindow.geometry("600x150+1000+200")
    answerwindow.resizable(False, False)

    label_guide = Label(answerwindow, text=quiz, fg='blue')
    label_guide.pack()
    label_guide.config(font=("Courier", 10))
    label_guide.place(x=20, y=25)

    label_ans = Label(answerwindow, text='ANSWER :')
    label_ans.pack()
    label_ans.config(font=("Courier", 14))
    label_ans.place(x=20, y=50)

    input_ans = Entry(answerwindow)
    input_ans.pack()
    input_ans.place(x=140, y=50, width=300)
    input_ans.config(font=("Courier", 14))

    btnStart = Button(answerwindow, text='SUBMIT', bg="skyblue",
                      command=lambda: validationAnswer(input_ans.get()))
    btnStart.pack()
    btnStart.place(x=300, y=100, width=100)
    btnStart.config(font=("Courier", 12))

    answerwindow.mainloop()

def validationAnswer(ans):
    global ANSWER
    if ans == '':
        messagebox.showerror('WARNING!', 'You should enter your answer exactly !')
    else:
        answerwindow.destroy()
        ANSWER = ans

def main(uid,pwd,amt):
    chrome_options = webdriver.ChromeOptions()
    prefs = {"profile.default_content_setting_values.notifications": 2}
    chrome_options.add_experimental_option("prefs", prefs)
    chrome_options.add_argument("log-level=3")
    driver = webdriver.Chrome(options=chrome_options)
    driver.maximize_window()

    bank = bank_info[0]
    amount = amt
    userid = uid
    password = pwd
    transactionid = bank_info[1]
    reference = bank_info[2]

    print('processing...')

    driver.get("https://www2.pbebank.com/myIBK/apppbb/servlet/BxxxServlet?RDOName=BxxxAuth&MethodName=login")
    WebDriverWait(driver, 120).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='new-ebank-container']")))

    driver.switch_to.default_content()
    iframe = driver.find_element_by_id("new-ebank-container")
    driver.switch_to.frame(iframe)

    login_iframe = driver.find_element_by_id("iframe1")
    driver.switch_to.frame(login_iframe)

    driver.find_element_by_xpath("//*[@id='LoginId']/table/tbody/tr[3]/td/div/input[1]").send_keys(userid)
    time.sleep(0.5)

    driver.find_element_by_id("NextBtn").click()
    time.sleep(3)

    secure_chk = driver.find_element_by_id("passcred")
    actions = ActionChains(driver)
    actions.move_to_element(secure_chk).perform()
    driver.execute_script("arguments[0].click();", secure_chk)
    time.sleep(2)

    submitbtn_div = driver.find_element_by_id("buttonSec")
    driver.execute_script("arguments[0].setAttribute('style','')", submitbtn_div)
    pwd_div = driver.find_element_by_id("pasword")
    driver.execute_script("arguments[0].setAttribute('style','')", pwd_div)

    driver.find_element_by_id("password").clear()
    driver.find_element_by_id("password").send_keys(password)
    time.sleep(1)
    driver.find_element_by_id("SubmitBtn").click()

    time.sleep(5)

    driver.switch_to.default_content()
    iframe_home = driver.find_element_by_tag_name("iframe")
    driver.switch_to.frame(iframe_home)

    try:
        answer_tag = driver.find_element_by_xpath("//*[@name='USER_ANSWER']")
        question = driver.find_element_by_xpath("//*[@class='form-body']/div[2]/div/div/p").text
        answerWindow(question)
        answer_tag.send_keys(ANSWER)
        time.sleep(1)
        driver.find_element_by_xpath("//*[@name='Next']").click()
        time.sleep(5)
    except:
        pass
    WebDriverWait(driver, 20).until(EC.visibility_of_element_located((By.XPATH, "//*[@class='tiles']/a[2]/div/div")))
    fund_transfer = driver.find_element_by_xpath("//*[@class='tiles']/a[2]/div/div")
    driver.execute_script("arguments[0].click()", fund_transfer)
    time.sleep(5)

    if bank_info[0].lower() == public_bank.lower():
        WebDriverWait(driver, 20).until(EC.visibility_of_element_located((By.XPATH, "//*[@class='page-sidebar-menu']/li[2]/a")))
        pbe_item = driver.find_element_by_xpath("//*[@class='page-sidebar-menu']/li[2]/a")
        driver.execute_script("arguments[0].click()", pbe_item)
        time.sleep(2)
        driver.find_element_by_xpath("//*[@class='page-sidebar-menu']/li[3]/a").click()
        time.sleep(2)
        driver.find_element_by_xpath("//*[@name='TO_ACC_NO']").send_keys(transactionid)
        time.sleep(0.5)
        driver.find_element_by_xpath("//*[@name='RECIPIENT_REF']").send_keys(reference)
        time.sleep(0.5)
        driver.find_element_by_xpath("//*[@name='TRANSACTION_AMT']").send_keys(amount)
        time.sleep(1)
        driver.find_element_by_xpath("//*[@name='next']").click()
        time.sleep(2)
        driver.find_element_by_xpath("//*[@class='form-actions fluid']/div/a/div").click()
        time.sleep(2)
        driver.find_element_by_class_name("close").click()
        time.sleep(1)
    else:
        bankname_chkcode = 0
        banks = ["AGRO Bank","Affin Bank","Al Rajhi Bank","Alliance Bank","AmBank",
                 "Bank Islam Malaysia Berhad","Bank Kerjasama Rakyat Malaysia","Bank Muamalat Malaysia Berhad",
                 "Bank Simpanan Nasional","CIMB Bank Berhad","Citibank Berhad","HSBC Bank Malaysia Berhad",
                 "Hong Leong Bank","Kuwait Finance House (Malaysia) Berhad","Maybank","OCBC Bank (Malaysia) Berhad",
                 "RHB Bank","Standard Chartered Bank","United Overseas Bank (Malaysia) Berhad"]
        WebDriverWait(driver, 20).until(EC.visibility_of_element_located((By.XPATH, "//*[@class='page-sidebar-menu']/li[3]/a")))
        oth_item = driver.find_element_by_xpath("//*[@class='page-sidebar-menu']/li[3]/a")
        driver.execute_script("arguments[0].click()", oth_item)
        time.sleep(2)
        driver.find_element_by_xpath("//*[@class='page-sidebar-menu']/li[1]/a").click()
        time.sleep(0.5)
        driver.find_element_by_xpath("//*[@class='page-sidebar-menu']/li[1]/ul/li[2]/a").click()
        time.sleep(2)
        bank_tag = Select(driver.find_element_by_xpath("//*[@name='TO_BANK_NBR']"))
        time.sleep(0.5)
        for each_bnk in banks:
            if bank.lower() in each_bnk.lower():
                bankname_chkcode = 1
                bank_tag.select_by_visible_text(each_bnk)
                time.sleep(1)
                break
        if not bankname_chkcode == 1:
            time.sleep(1)
            ctypes.windll.user32.MessageBoxW(0, "Sorry... Bank name is wrong.", "WARNING!", 0)
            driver.find_element_by_xpath("//*[@id='header_task_bar']/a").click()
            time.sleep(3)
            driver.close()
            exit()
        driver.find_element_by_xpath("//*[@name='BENE_ACC_NO']").send_keys(transactionid)
        time.sleep(0.5)
        payment_type = Select(driver.find_element_by_xpath("//*[@name='PAYMENT_TYPE']"))
        payment_type.select_by_visible_text("Savings/Current Account")
        time.sleep(1)
        driver.find_element_by_xpath("//*[@name='RECIPIENT_REFERENCE']").send_keys(reference)
        time.sleep(0.5)
        driver.find_element_by_xpath("//*[@name='TRANSACTION_AMT']").send_keys(amount)
        time.sleep(1)
        driver.find_element_by_xpath("//*[@class='form-actions fluid']/div/button[3]").click()
        time.sleep(2)
        driver.find_element_by_xpath("//*[@class='form-actions fluid']/div/a/div").click()
        time.sleep(2)
        driver.find_element_by_class_name("close").click()
        time.sleep(1)



    try:
        smscodebox = driver.find_element_by_id("pac")
        smsCodeWindow()
        smscodebox.send_keys(SMS)
        confirm_sms = driver.find_element_by_xpath("//*[@name='next']")
        driver.execute_script("arguments[0].click()", confirm_sms)
        time.sleep(3)
        try:
            answer_tag = driver.find_element_by_xpath("//*[@name='USER_ANSWER']")
            question = driver.find_element_by_xpath("//*[@class='form-body']/div[2]/div/div/p").text
            answerWindow(question)
            answer_tag.send_keys(ANSWER)
            time.sleep(1)
            driver.find_element_by_xpath("//*[@name='Next']").click()
            time.sleep(3)
        except:
            pass
        
        ctypes.windll.user32.MessageBoxW(0, "Congratulation... Success !", "SUCCESS!", 0)

    except:
        ctypes.windll.user32.MessageBoxW(0, "Sorry... Please try again later.", "WARNING!", 0)


    time.sleep(3)
    driver.find_element_by_xpath("//*[@id='header_task_bar']/a").click()
    time.sleep(3)
    driver.close()
    exit()


if __name__ == "__main__":
    public_bank = "Public bank"
    bank_info = []
    with open("bankinfo.txt", "r") as file:
        reader = csv.reader(file, delimiter=':', quoting=csv.QUOTE_NONE)
        for line in reader:
            bank_info.append(line[1].strip())
    userinfoWindow()
