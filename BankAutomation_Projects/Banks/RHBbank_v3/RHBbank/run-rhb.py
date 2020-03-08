
"""
Author : Li G
Created Date : June 22, 2019
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

    label_guide = Label(userinfowindow, text='Welcome To RHB Bank', fg='green')
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

def oneTimePwdWindow(code):
    global onetimepwdwindow
    onetimepwdwindow = Tk()
    onetimepwdwindow.title('ONE TIME PASSWORD')
    onetimepwdwindow.geometry("500x150+1000+200")
    onetimepwdwindow.resizable(False, False)

    label_guide = Label(onetimepwdwindow, text="Security Code : "+code, fg='blue')
    label_guide.pack()
    label_guide.config(font=("Courier", 12))
    label_guide.place(x=20, y=25)

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

def validationPwd(oneTP):
    global ONETIMEPWD
    if oneTP == '':
        messagebox.showerror('WARNING!', 'You should enter your One Time Password exactly !')
    else:
        onetimepwdwindow.destroy()
        ONETIMEPWD = oneTP

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
    bank_account = bank_info[1]
    reference = bank_info[2]
    nickname = bank_info[3]

    print('processing...')

    driver.get("https://logon.rhb.com.my/")
    WebDriverWait(driver, 120).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='ibk-frameset']")))

    driver.switch_to.default_content()

    signin_frame = driver.find_element_by_id("ibkFrame")
    driver.switch_to.frame(signin_frame)

    driver.find_element_by_id("btnClose").click()
    time.sleep(1)

    driver.find_element_by_id("txtAccessCode").send_keys(userid)
    time.sleep(0.5)
    driver.find_element_by_id("cmdLogin").click()
    time.sleep(5)
    driver.find_element_by_id("lblyes").click()
    time.sleep(1)
    driver.find_element_by_id("txtPswd").send_keys(password)
    time.sleep(0.5)
    driver.find_element_by_id("cmdLogin").click()
    # time.sleep(5)
    WebDriverWait(driver, 20).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='navlist']")))
    try:
        mdl_close_btn = driver.find_element_by_class_name("btnX")
        mdl_close_btn.click()
        time.sleep(1)
    except:
        pass

    fund_item = driver.find_element_by_xpath("//*[@id='navlist']/li[4]")
    fund_item.click()
    time.sleep(0.5)

    rhb_chk_code = 0

    if bank_info[0].lower() == rhb_acnt.lower():
        driver.find_element_by_xpath("//*[@id='navlist']/li[4]/ul/li[3]").click()
        # time.sleep(3)
        rhb_chk_code = 1

    else:
        driver.find_element_by_xpath("//*[@id='navlist']/li[4]/ul/li[4]").click()
        # time.sleep(3)
    WebDriverWait(driver, 20).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='_ctl14__ctl7_lblChallengeValue']")))
    while True:
        sct_code = driver.find_element_by_id("_ctl14__ctl7_lblChallengeValue").text
        time.sleep(1)
        oneTimePwdWindow(sct_code)
        driver.find_element_by_id("_ctl14__ctl7_txtOTP").send_keys(ONETIMEPWD)
        time.sleep(1)
        driver.find_element_by_id("_ctl14_cmdSubmit").click()
        time.sleep(3)
        try:
            error_msg = driver.find_element_by_id("_ctl14__ctl7_lblMessage").text
            if "Invalid" in error_msg:
                ctypes.windll.user32.MessageBoxW(0, "Unfortunately... One Time Password is not correct !", "WARNING!", 0)
        except:
            break
    driver.find_element_by_id("_ctl14_lnkbtnOpen").click()
    time.sleep(3)

    if rhb_chk_code == 1:
        driver.find_element_by_id("_ctl14_txtToAcctNo").send_keys(bank_account)
        time.sleep(0.5)
        driver.find_element_by_id("_ctl14_txtNickNm").send_keys(nickname)
        time.sleep(0.5)
        driver.find_element_by_id("_ctl14_txtAmt").send_keys(amount)
        time.sleep(0.5)
        driver.find_element_by_id("_ctl14_txtPymntDtl").send_keys(reference)
        time.sleep(1)
        driver.find_element_by_id("_ctl14_btnTransfer").click()
        WebDriverWait(driver, 20).until(
            EC.visibility_of_element_located((By.XPATH, "//*[@id='_ctl14__ctl28_lblChallengeValue']")))

        while True:
            sct_code = driver.find_element_by_id("_ctl14__ctl28_lblChallengeValue").text
            time.sleep(1)
            oneTimePwdWindow(sct_code)
            driver.find_element_by_id("_ctl14__ctl28_txtOTP").send_keys(ONETIMEPWD)
            time.sleep(1)
            driver.find_element_by_id("_ctl14_btnSubmit").click()
            time.sleep(5)
            try:
                error_msg = driver.find_element_by_id("_ctl14__ctl28_lblMessage").text
                if "Invalid" in error_msg:
                    ctypes.windll.user32.MessageBoxW(0, "Unfortunately... One Time Password is not correct !",
                                                     "WARNING!", 0)
            except:
                ctypes.windll.user32.MessageBoxW(0, "Congratulation... Transaction is Successful !",
                                                 "SUCCESS!", 0)
                break
    else:
        bank_chk_code = 0
        banks = ["Agrobank","Alliance Bank Malaysia Berhad","Al-Rajhi Bank","AMBank",
                 "Bank Islam Malaysia Berhad","Bank Kerjasama Rakyat Malaysia","Bank Muamalat Malaysia Berhad",
                 "Bank Simpanan Nasional","CIMB","Citibank Berhad","Hong Leong Bank Berhad","HSBC Bank",
                 "Kuwait Finance House","Maybank","MBSB Bank Berhad","OCBC Bank (M) Berhad","Public Bank",
                 "Standard Chartered Bank","United Overseas Bank Berhad"]
        bank_tag = Select(driver.find_element_by_id("_ctl14_ddlBank"))
        time.sleep(0.5)
        for each_bnk in banks:
            if bank.lower() in each_bnk.lower():
                bank_chk_code = 1
                bank_tag.select_by_visible_text(each_bnk)
                time.sleep(2)
                break
        if not bank_chk_code == 1:
            time.sleep(1)
            ctypes.windll.user32.MessageBoxW(0, "Sorry... Bank name is not correct !", "WARNING!", 0)
            time.sleep(3)
            driver.close()
            exit()
        payment_type = Select(driver.find_element_by_id("_ctl14_ddlPaymentType"))
        payment_type.select_by_visible_text("Fund Transfer")
        time.sleep(0.5)
        driver.find_element_by_id("_ctl14_txtToAcct").send_keys(bank_account)
        time.sleep(0.5)
        driver.find_element_by_id("_ctl14_txtToAcctNm").send_keys(nickname)
        time.sleep(0.5)
        driver.find_element_by_id("_ctl14_txtAmount").send_keys(amount)
        time.sleep(0.5)
        ref_tag = Select(driver.find_element_by_id("_ctl14_ddlRecipientRef"))
        ref_tag.select_by_visible_text("3rd Party Transfer")
        time.sleep(2)
        driver.find_element_by_id("_ctl14_txtPymntDscp").send_keys(reference)
        time.sleep(0.5)
        driver.find_element_by_id("_ctl14_btnPreview").click()
        WebDriverWait(driver, 20).until(
            EC.visibility_of_element_located((By.XPATH, "//*[@id='_ctl14__ctl32_lblChallengeValue']")))
        while True:
            sct_code = driver.find_element_by_id("_ctl14__ctl32_lblChallengeValue").text
            time.sleep(1)
            oneTimePwdWindow(sct_code)
            driver.find_element_by_id("_ctl14__ctl32_txtOTP").send_keys(ONETIMEPWD)
            time.sleep(1)
            driver.find_element_by_id("_ctl14_cmd_Submit").click()
            time.sleep(5)
            try:
                error_msg = driver.find_element_by_id("_ctl14__ctl32_lblMessage").text
                if "Invalid" in error_msg:
                    ctypes.windll.user32.MessageBoxW(0, "Unfortunately... One Time Password is not correct !",
                                                     "WARNING!", 0)
            except:
                ctypes.windll.user32.MessageBoxW(0, "Congratulation... Transaction is Successful !",
                                                 "SUCCESS!", 0)
                break

    time.sleep(3)
    driver.close()
    exit()


if __name__ == "__main__":
    rhb_acnt = "RHB Account"
    bank_info = []
    with open("bankinfo.txt", "r") as file:
        reader = csv.reader(file, delimiter=':', quoting=csv.QUOTE_NONE)
        for line in reader:
            bank_info.append(line[1].strip())
    userinfoWindow()
