
"""
Author : Li G
Created Date : June 17, 2019
"""

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

    label_guide = Label(userinfowindow, text='Welcome To HongLeong Bank', fg='green')
    label_guide.pack()
    label_guide.config(font=("Courier", 16))
    label_guide.place(x=30, y=50)

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

def main(uid,pwd,amt):
    chrome_options = webdriver.ChromeOptions()
    prefs = {"profile.default_content_setting_values.notifications": 2}
    chrome_options.add_experimental_option("prefs", prefs)
    chrome_options.add_argument("log-level=3")
    driver = webdriver.Chrome(options=chrome_options)
    driver.maximize_window()

    bank = bank_info[0].upper()
    amount = amt
    userid = uid
    password = pwd
    transactionid = bank_info[1]
    reference = bank_info[2]

    print('processing...')

    driver.get("https://s.hongleongconnect.my/rib/app/fo/login")
    WebDriverWait(driver, 120).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='idLoginId']")))

    driver.find_element_by_id("idLoginId").send_keys(userid)
    time.sleep(0.5)
    login_uid = driver.find_element_by_id("idBtnSubmit1")
    driver.execute_script("arguments[0].click()", login_uid)

    WebDriverWait(driver, 20).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='idSBCBConfirmPic']")))
    secure_chk = driver.find_element_by_id("idSBCBConfirmPic")
    actions = ActionChains(driver)
    actions.move_to_element(secure_chk).perform()
    driver.execute_script("arguments[0].click();", secure_chk)
    time.sleep(0.5)

    driver.find_element_by_id("idPswd").send_keys(password)
    login_pwd = driver.find_element_by_id("idBtnSubmit2")
    driver.execute_script("arguments[0].click()", login_pwd)

    WebDriverWait(driver, 20).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='idMainFrame']")))
    driver.switch_to.default_content()
    iframe = driver.find_element_by_id("idMainFrame")
    driver.switch_to.frame(iframe)

    time.sleep(5)

    payment_tag = driver.find_element_by_xpath("//*[@id='mega-menu']/li[2]/a")
    driver.execute_script("arguments[0].click()", payment_tag)
    time.sleep(0.5)

    if bank_info[0] == "Hong leong bank":
        hongleong_item = driver.find_element_by_xpath("//*[@id='mega-menu']/li[2]/div/ul/div[2]/li[1]/ul/li[2]/a")
        driver.execute_script("arguments[0].click()", hongleong_item)
        time.sleep(2)
        h_act_type_label = driver.find_element_by_id("idAcctTyp_label")
        driver.execute_script("arguments[0].click()", h_act_type_label)
        time.sleep(0.5)
        h_act_type_panel = driver.find_element_by_xpath("//*[@id='idAcctTyp_panel']/div/ul/li[2]")
        driver.execute_script("arguments[0].click()", h_act_type_panel)
        time.sleep(1)
        driver.find_element_by_id("idAcctNo").send_keys(transactionid)
        driver.find_element_by_id("idAmt").send_keys(amount)
        h_rr_label = driver.find_element_by_id("idRR_label")
        driver.execute_script("arguments[0].click()", h_rr_label)
        time.sleep(0.5)
        h_rr_panel = driver.find_element_by_xpath("//*[@id='idRR_panel']/div/ul/li[2]")
        driver.execute_script("arguments[0].click()", h_rr_panel)
        time.sleep(1)
        driver.find_element_by_id("idOthInfo").send_keys(reference)
        h_submit_btn = driver.find_element_by_id("idBtnSubmit")
        driver.execute_script("arguments[0].click()", h_submit_btn)
        time.sleep(5)

    else:
        bankname_chkcode = 0

        otherbank_item = driver.find_element_by_xpath("//*[@id='mega-menu']/li[2]/div/ul/div[2]/li[1]/ul/li[3]/a")
        driver.execute_script("arguments[0].click()", otherbank_item)
        time.sleep(2)
        bank_label = driver.find_element_by_id("idBnk_label")
        driver.execute_script("arguments[0].click()", bank_label)
        time.sleep(0.5)
        banks = driver.find_elements_by_xpath("//*[@id='idBnk_panel']/div/ul/li")
        for each_bnk in banks:
            if bank in each_bnk.text:
                bankname_chkcode = 1
                driver.execute_script("arguments[0].click()", each_bnk)
                time.sleep(1)
                break
        if not bankname_chkcode == 1:
            time.sleep(1)
            ctypes.windll.user32.MessageBoxW(0, "Sorry... Bank name is wrong.", "WARNING!", 0)
            driver.close()
            exit()
        act_type_label = driver.find_element_by_id("idAcctTyp_label")
        driver.execute_script("arguments[0].click()", act_type_label)
        time.sleep(0.5)
        act_type_panel = driver.find_element_by_xpath("//*[@id='idAcctTyp_panel']/div/ul/li[2]")
        driver.execute_script("arguments[0].click()", act_type_panel)
        time.sleep(1)
        driver.find_element_by_id("idAcctNo").send_keys(transactionid)
        trsf_mode_label = driver.find_element_by_id("idSOMTrsfMode_label")
        driver.execute_script("arguments[0].click()", trsf_mode_label)
        time.sleep(0.5)
        trsf_mode_panel = driver.find_element_by_xpath("//*[@id='idSOMTrsfMode_panel']/div/ul/li[2]")
        driver.execute_script("arguments[0].click()", trsf_mode_panel)
        time.sleep(1)
        driver.find_element_by_id("idAmt").send_keys(amount)
        rr_label = driver.find_element_by_id("idRR_label")
        driver.execute_script("arguments[0].click()", rr_label)
        time.sleep(0.5)
        rr_panel = driver.find_element_by_xpath("//*[@id='idRR_panel']/div/ul/li[2]")
        driver.execute_script("arguments[0].click()", rr_panel)
        time.sleep(1)
        driver.find_element_by_id("idOthInfo").send_keys(reference)

        secure_chk = driver.find_element_by_xpath("//*[@id='idSBCTnc']/div[2]/span")
        actions = ActionChains(driver)
        actions.move_to_element(secure_chk).perform()
        driver.execute_script("arguments[0].click();", secure_chk)
        time.sleep(1)

        submit_btn = driver.find_element_by_id("idBtnSubmit")
        driver.execute_script("arguments[0].click()", submit_btn)
        time.sleep(5)

    try:
        smscodebox = driver.find_element_by_id("idFormCfmAckDtl:idTACAdd")
        while True:
            smsCodeWindow()
            smscodebox.send_keys(SMS)
            confirm_sms = driver.find_element_by_id("idFormCfmAckDtl:idBtnConfirmTrsf")
            driver.execute_script("arguments[0].click()", confirm_sms)
            time.sleep(5)
            try:
                driver.find_element_by_class_name("ui-messages-error-summary")
                pass
            except:
                break
        ctypes.windll.user32.MessageBoxW(0, "Congratulation... Success !", "SUCCESS!", 0)
    except:
        ctypes.windll.user32.MessageBoxW(0, "Sorry... Please try again later.", "WARNING!", 0)
        driver.close()
        exit()

    time.sleep(5)
    driver.close()
    exit()


if __name__ == "__main__":
    bank_info = []
    with open("bankinfo.txt", "r") as file:
        reader = csv.reader(file, delimiter=':', quoting=csv.QUOTE_NONE)
        for line in reader:
            bank_info.append(line[1].strip())
    userinfoWindow()
