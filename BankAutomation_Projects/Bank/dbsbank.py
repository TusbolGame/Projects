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

    label_guide = Label(userinfowindow, text='Welcome To DBS Bank', fg='green')
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

    selected = IntVar()
    radio_tap = Radiobutton(userinfowindow, text='TAP', value=1, variable=selected)
    radio_tap.pack()
    radio_tap.place(x=100, y=320)
    radio_tap.config(font=("Courier", 14))
    radio_tap.select()

    radio_otp = Radiobutton(userinfowindow, text='OTP', value=2, variable=selected)
    radio_otp.pack()
    radio_otp.place(x=250, y=320)
    radio_otp.config(font=("Courier", 14))

    btnStart = Button(userinfowindow, text='START', bg="skyblue",
                      command=lambda: validation(input_userid.get(), input_pwd.get(), input_amt.get(), selected.get()))
    btnStart.pack()
    btnStart.place(x=150, y=400, width=100)
    btnStart.config(font=("Courier", 14))

    userinfowindow.mainloop()

def validation(userid,pwd,amt, val):
    if userid == '' or pwd == '' or amt == '':
        messagebox.showerror('WARNING!', 'You should enter your information exactly !')
    else:
        userinfowindow.destroy()
        main(userid,pwd,amt,val)

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

def main(uid,pwd,amt,val):
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
    dbs_rec_acnt = bank_info[1]
    other_rec_acnt = bank_info[2]
    myname = bank_info[3]
    rec_name = bank_info[4]
    purpose = bank_info[5]
    comment = bank_info[6]

    print('processing...')

    try:
        driver.get("https://internet-banking.dbs.com.sg/IB/Welcome")
        WebDriverWait(driver, 120).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='UID']")))
        time.sleep(2)

        uid_box = driver.find_element_by_id("UID")
        pin_box = driver.find_element_by_id("PIN")
        uid_box.send_keys(userid)
        pin_box.send_keys(password)
        time.sleep(1)
        login_btn = driver.find_element_by_xpath("//*[@class='login-form']/div[7]/button[1]")
        login_btn.click()

        WebDriverWait(driver, 120).until(EC.visibility_of_element_located((By.XPATH, "//*[@name='user_area']")))

        driver.switch_to.default_content()
        main_frame = driver.find_element_by_name("user_area")
        driver.switch_to.frame(main_frame)
        time.sleep(3)
        transfer_tab = driver.find_element_by_id("topnav1")
        hover = ActionChains(driver).move_to_element(transfer_tab)
        hover.perform()
        time.sleep(1)

        dbs_flag = False
        if bank.lower() == dbs_acnt.lower():
            acnt_tab = driver.find_element_by_xpath("//*[@id='topnav1']/div[2]/a[3]")
            dbs_flag = True
        else:
            acnt_tab = driver.find_element_by_xpath("//*[@id='topnav1']/div[2]/a[4]")
        acnt_tab.click()
        WebDriverWait(driver, 20).until(EC.visibility_of_element_located((By.XPATH, "//*[@id='iframe1']")))
        time.sleep(3)

        driver.execute_script("$('#iframe1').contents().find('a[id=\"AuthenticatBtnId\"]').click();")
        time.sleep(3)
        otp_man = driver.find_element_by_xpath("//*[@id='softTokenMobAuthent']/span[3]/a")

        if val == 2:
            otp_man.click()
            while True:
                oneTimePwdWindow()
                time.sleep(5)
                iframe = driver.find_element_by_id("iframe1")
                driver.switch_to.frame(iframe)
                driver.find_element_by_id("mpin1").send_keys(int(OTP[0]))
                driver.find_element_by_id("mpin2").send_keys(int(OTP[1]))
                driver.find_element_by_id("mpin3").send_keys(int(OTP[2]))
                driver.find_element_by_id("mpin4").send_keys(int(OTP[3]))
                driver.find_element_by_id("mpin5").send_keys(int(OTP[4]))
                driver.find_element_by_id("mpin6").send_keys(int(OTP[5]))
                time.sleep(1)
                driver.find_element_by_id("confirmButton").click()
                time.sleep(3)
                try:
                    auth_alert_ok = driver.find_element_by_xpath("//*[@id='KHTErrorModelPopUp']/div/div/div[3]/a")
                    driver.execute_script("arguments[0].click()", auth_alert_ok)
                    time.sleep(3)
                except:
                    break

        while True:

            try:
                progress_bar = driver.find_element_by_xpath("//*[@id='progress-bar']")
                break
            except:
                try:
                    retry_btn = driver.find_element_by_xpath("//*[@id='KHTerrorModelPopUpTimeOut']/div/div/div[3]/button")
                    driver.execute_script("arguments[0].click()", retry_btn)
                    time.sleep(3)
                except:
                    pass

        if dbs_flag:
            rec_acnt_tab = Select(driver.find_element_by_xpath("//*[@name='to_acct_']"))
            for each_rec_acnt in rec_acnt_list:
                if dbs_rec_acnt.lower() in each_rec_acnt.lower():
                    rec_acnt_tab.select_by_visible_text(each_rec_acnt)
                    time.sleep(1)
                    break
            amt_box = driver.find_element_by_xpath("//*[@name='txtAmount']")
            amt_box.send_keys(amount)
            time.sleep(1)
            next_btn = driver.find_element_by_id("Submit")
            next_btn.click()
            time.sleep(5)
            submit_btn = driver.find_element_by_id("Submit")
            submit_btn.click()
            print("Your transaction has been completed !")

        else:
            payingto_tab = driver.find_element_by_xpath("//*[@name='MainForm']/section[2]/div/div/div[1]/div[3]/div[2]/div/div/select")
            options = payingto_tab.find_elements_by_xpath(".//option")
            for option in options:
                print(option.text)
                if myname.lower() in option.text.lower():
                    paying_select = Select(payingto_tab)
                    paying_select.select_by_visible_text(option.text)
                    break
            try:
                rec_name_box = driver.find_element_by_id("beneficiaryNameNew")
                rec_name_box.send_keys(rec_name)
                time.sleep(1)
                bank_tab = Select(driver.find_element_by_xpath("//*[@name='lstToAccount']"))
                for each_bank in bank_list:
                    if bank.lower() in each_bank.lower():
                        bank_tab.select_by_visible_text(each_bank)
                        time.sleep(1)
                        break
                rec_acnt_other = driver.find_element_by_id("beneficiaryAccountNumberNew")
                rec_acnt_other.send_keys(other_rec_acnt)
                time.sleep(0.5)
                myname_other = driver.find_element_by_xpath("//*[@name='txtChoiceofInitials']")
                myname_other.send_keys(myname)
                time.sleep(0.5)
            except:
                pass

            amt_box_other = driver.find_element_by_xpath("//*[@name='amountTransfer']")
            amt_box_other.send_keys(amount)
            time.sleep(0.5)

            purpose_tab = Select(driver.find_element_by_xpath("//*[@name='paymentPurpose']"))
            purpose_tab.select_by_visible_text(purpose)
            time.sleep(1)

            comment_tab = driver.find_element_by_xpath("//*[@name='paymentRef']")
            comment_tab.send_keys(comment)
            time.sleep(0.5)
            next_btn = driver.find_element_by_id("fetchDetailsButton")
            next_btn.click()
            time.sleep(5)
            submit_btn = driver.find_element_by_id("confirm")
            submit_btn.click()
            print("Your transaction has been completed !")

    except Exception as e:
        print(str(e))
        print("-----> Something went wrong !")
    time.sleep(5)
    driver.close()
    exit()



if __name__ == "__main__":

    dbs_acnt = "DBS Account"
    rec_acnt_list = ['POSBkids Account 358-11961-1 Eunice ',
                     'POSBkids Account (Passbook) 107-60856-7 Nigel ',
                     'POSBkids Account 249-69019-8 Shirley ', 'POSB Payroll Account 425-77298-8 Zhen wei ',
                     'POSBkids Account (Passbook) 247-68251-1 Terrorist ',
                     'ePOSBkids Account 428-00319-5 Jek ']
    bank_list = ['AUSTRALIA & NEW ZEALAND BANKING GROUP', 'BANK OF CHINA LIMITED', 'BNP PARIBAS',
                 'CIMB BANK BERHAD', 'CITIBANK NA', 'Citibank Singapore Limited', 'DEUTSCHE BANK AG',
                 'HL BANK', 'HSBC (Corporate)', 'HSBC (Personal)', 'ICICI BANK LIMITED',
                 'INDUSTRIAL AND COMMERCIAL BANK OF CHINA LIMITED',
                 'MIZUHO BANK LIMITED', 'Malayan Banking Berhad, Singapore Branch', 'Maybank Singapore Limited',
                 'OVERSEA-CHINESE BANKING CORPN LTD', 'RHB BANK BERHAD', 'SUMITOMO MITSUI BANKING CORPORATION',
                 'Standard Chartered Bank (Singapore) Limited', 'THE BANK OF TOKYO-MITSUBISHI UFJ, LTD',
                 'UNITED OVERSEAS BANK LTD']
    bank_info = []
    with open("bankinfo.txt", "r") as file:
        reader = csv.reader(file, delimiter=':', quoting=csv.QUOTE_NONE)
        for line in reader:
            bank_info.append(line[1].strip())
    userinfoWindow()