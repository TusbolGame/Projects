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
import cv2
import numpy as np
import pytesseract
from PIL import Image
import string
from pytesseract import image_to_string


class Del:
  def __init__(self, keep=string.digits):
    self.comp = dict((ord(c),c) for c in keep)
  def __getitem__(self, k):
    return self.comp.get(k)


DD = Del()


def userinfoWindow():
    global userinfowindow
    userinfowindow = Tk()

    userinfowindow.title('USER INFORMATION')
    userinfowindow.geometry("400x500+1000+200")
    userinfowindow.resizable(False, False)

    label_guide = Label(userinfowindow, text='Welcome To BRI Bank', fg='green')
    label_guide.pack()
    label_guide.config(font=("Courier", 16))
    label_guide.place(x=70, y=50)

    label_guide = Label(userinfowindow, text='Please enter your information.', fg='blue')
    label_guide.pack()
    label_guide.config(font=("Courier", 12))
    label_guide.place(x=50, y=100)

    label_userid = Label(userinfowindow, text='Username')
    label_userid.pack()
    label_userid.config(font=("Courier", 14))
    label_userid.place(x=70, y=150)

    input_userid = Entry(userinfowindow)
    input_userid.pack()
    input_userid.place(x=170, y=150, width=200)
    input_userid.config(font=("Courier", 16))

    label_pwd = Label(userinfowindow, text='Password')
    label_pwd.pack()
    label_pwd.config(font=("Courier", 14))
    label_pwd.place(x=70, y=200)

    input_pwd = Entry(userinfowindow)
    input_pwd.pack()
    input_pwd.place(x=170, y=200, width=200)
    input_pwd.config(font=("Courier", 16))

    label_amt = Label(userinfowindow, text='Amount')
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

def Nomor_Rekening_Tujuan():
    nextstep('039001000030303')
    # global nomor_rekening_tujuan
    # nomor_rekening_tujuan = Tk()
    # nomor_rekening_tujuan.title('USER INFORMATION')
    # nomor_rekening_tujuan.geometry("400x500+1000+200")
    # nomor_rekening_tujuan.resizable(False, False)
    #
    # label_guide = Label(nomor_rekening_tujuan, text='Welcome To BRI Bank', fg='green')
    # label_guide.pack()
    # label_guide.config(font=("Courier", 16))
    # label_guide.place(x=70, y=50)
    #
    # label_guide = Label(nomor_rekening_tujuan, text='Please enter your information.', fg='blue')
    # label_guide.pack()
    # label_guide.config(font=("Courier", 12))
    # label_guide.place(x=50, y=100)
    #
    # label_userid = Label(nomor_rekening_tujuan, text='Nomor Rekening Tujuan')
    # label_userid.pack()
    # label_userid.config(font=("Courier", 14))
    # label_userid.place(x=70, y=150)
    #
    # input_userid = Entry(nomor_rekening_tujuan)
    # input_userid.pack()
    # input_userid.place(x=170, y=150, width=200)
    # input_userid.config(font=("Courier", 16))
    #
    # btnStart = Button(nomor_rekening_tujuan, text='START', bg="skyblue",
    #                   command=lambda: nextstep(input_userid.get()))
    # btnStart.pack()
    # btnStart.place(x=150, y=400, width=100)
    # btnStart.config(font=("Courier", 14))
    #
    # nomor_rekening_tujuan.mainloop()


def nextstep(userid):
    # nomor_rekening_tujuan.destroy()

    tujan_box = driver.find_element_by_id('T3')
    tujan_box.send_keys(userid)
    time.sleep(0.5)

    amount_box = driver.find_element_by_id('AMOUNT')
    amount_box.send_keys(amount)
    time.sleep(0.5)

    berita_box = driver.find_element_by_id('NOTES')
    email_box = driver.find_element_by_id('DEST_EML')
    time.sleep(0.5)

    berita_box.send_keys('Greatday')
    time.sleep(0.5)
    email_box.send_keys('Greatday@gmail.com')
    time.sleep(0.5)

    submit_btn = driver.find_element_by_name('submitButton')
    submit_btn.click()
    time.sleep(5)
    finalstage()
    # userinfoWindow1()


def userinfoWindow1():
    global userinfowindow1
    userinfowindow1 = Tk()

    userinfowindow1.title('USER INFORMATION')
    userinfowindow1.geometry("400x500+1000+200")
    userinfowindow1.resizable(False, False)

    label_guide = Label(userinfowindow1, text='Enter Information', fg='green')
    label_guide.pack()
    label_guide.config(font=("Courier", 16))
    label_guide.place(x=70, y=50)

    label_guide = Label(userinfowindow1, text='Please enter your information.', fg='blue')
    label_guide.pack()
    label_guide.config(font=("Courier", 12))
    label_guide.place(x=50, y=100)

    # label_pwd = Label(userinfowindow1, text='Password')
    # label_pwd.pack()
    # label_pwd.config(font=("Courier", 14))
    # label_pwd.place(x=70, y=200)
    #
    # input_pwd = Entry(userinfowindow1)
    # input_pwd.pack()
    # input_pwd.place(x=170, y=200, width=200)
    # input_pwd.config(font=("Courier", 16))

    label_mtoken = Label(userinfowindow1, text='mToken')
    label_mtoken.pack()
    label_mtoken.config(font=("Courier", 14))
    label_mtoken.place(x=70, y=250)

    input_mtoken = Entry(userinfowindow1)
    input_mtoken.pack()
    input_mtoken.place(x=170, y=250, width=200)
    input_mtoken.config(font=("Courier", 16))



    btnStart = Button(userinfowindow1, text='START', bg="skyblue",
                      command=lambda: complete(input_mtoken.get()))
    btnStart.pack()
    btnStart.place(x=150, y=400, width=100)
    btnStart.config(font=("Courier", 14))

    userinfowindow1.mainloop()

def complete(mtoken):
    userinfowindow1.destroy()

    token_box = driver.find_element_by_id('mtoken-input')
    token_box.send_keys(mtoken)
    time.sleep(0.5)

    submit_btn = driver.find_element_by_name('submitButton')
    submit_btn.click()
    time.sleep(5)

def finalstage():

    driver.switch_to.default_content()
    time.sleep(0.5)
    driver.switch_to.frame(driver.find_element_by_id('content'))
    time.sleep(1)

    pwd_box = driver.find_element_by_name('pswdnotencrypt')
    time.sleep(0.5)
    pwd_box.send_keys(password)
    time.sleep(0.5)
    request_button = driver.find_element_by_id('request-token')
    time.sleep(0.5)
    request_button.click()

    time.sleep(1)

    userinfoWindow1()


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

def main(uid,pwd,amt):
    chrome_options = webdriver.ChromeOptions()
    prefs = {"profile.default_content_setting_values.notifications": 2}
    chrome_options.add_experimental_option("prefs", prefs)
    chrome_options.add_argument("log-level=3")
    global driver
    driver = webdriver.Chrome(options=chrome_options)
    driver.maximize_window()

    global bank
    bank = "https://ib.bri.co.id/ib-bri/Login.html"
    global amount, userid, password, transfer_pwd
    amount = amt
    userid = uid
    password = pwd
    transfer_pwd = pwd
    print('processing...')

    # try:
    driver.get(bank)
    WebDriverWait(driver, 300).until(EC.visibility_of_element_located((By.XPATH, '//*[@id="simple_img"]/img')))

    captcha_success = 0

    getPageScreenshot()

    pytesseract.pytesseract.tesseract_cmd = './Tesseract-OCR/tesseract'
    result = get_string('captcha.png')
    result = result.translate(DD)
    if len(result) == 4:
        captcha_success = 1

    while captcha_success == 0:
        driver.get(bank)
        result = getloop()
        result = result.translate(DD)
        if len(result) == 4:
            captcha_success = 1

    userid_box = driver.find_element_by_xpath('//*[@id="loginForm"]/input[3]')
    password_box = driver.find_element_by_xpath('//*[@id="loginForm"]/input[5]')
    valid_box = driver.find_element_by_xpath('//*[@id="loginForm"]/div[2]/input')
    login_button = driver.find_element_by_xpath('//*[@id="loginForm"]/button')

    userid_box.send_keys(userid)
    password_box.send_keys(password)
    valid_box.send_keys(result)
    login_button.click()
    time.sleep(5)
    transfer()

    time.sleep(5)
    driver.close()
    exit()

def transfer():
    transfer_tab = driver.find_element_by_id('transfer')
    try:
        transfer_tab.click()
    except:
        main(userid, password, amount)
    # WebDriverWait(driver, 300).until(EC.visibility_of_element_located((By.ID, 'iframemenu')))
    time.sleep(2)
    driver.switch_to.frame(driver.find_element_by_id('iframemenu'))
    time.sleep(2)
    toSesama_button = driver.find_element_by_xpath('/html/body/div[1]/div[1]/a')
    toSesama_button.click()
    time.sleep(5)
    driver.switch_to.default_content()
    time.sleep(1)
    driver.switch_to.frame(driver.find_element_by_id('content'))
    Nomor_Rekening_Tujuan()

def getloop():
    WebDriverWait(driver, 300).until(EC.visibility_of_element_located((By.XPATH, '//*[@id="simple_img"]/img')))
    getPageScreenshot()
    pytesseract.pytesseract.tesseract_cmd = './Tesseract-OCR/tesseract'
    result = get_string('captcha.png')
    return result

def getPageScreenshot():
    element = driver.find_element_by_xpath('//*[@id="simple_img"]/img');
    location = element.location;
    size = element.size;
    driver.save_screenshot("page.png");
    x = location['x'];
    y = location['y'];
    width = location['x'] + size['width'];
    height = location['y'] + size['height'];
    im = Image.open('page.png')
    im = im.crop((int(x), int(y), int(width), int(height)))
    im.save('captcha.png')

def get_string(img_path):
    # Read image with opencv
    img = cv2.imread(img_path)
    scale_percent = 200  # percent of original size
    width = int(img.shape[1] * scale_percent / 100)
    height = int(img.shape[0] * scale_percent / 100)
    dim = (width, height)
    resized = cv2.resize(img, dim, interpolation=cv2.INTER_AREA)
    cv2.imwrite('Resized.png', resized)

    # Convert to gray
    img = cv2.cvtColor(resized, cv2.COLOR_BGR2GRAY)

    # Apply dilation and erosion to remove some noise
    kernel = np.ones((1, 1), np.uint8)
    img = cv2.dilate(img, kernel, iterations=1)
    img = cv2.erode(img, kernel, iterations=1)

    # Write image after removed noise
    cv2.imwrite("removed_noise.png", img)

    #  Apply threshold to get image with only black and white
    img = cv2.adaptiveThreshold(img, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY, 61, 2)

    # Write the image after apply opencv to do some ...
    cv2.imwrite("thres.png", img)
    pytesseract.pytesseract.tesseract_cmd = 'Tesseract-OCR/tesseract'

    # Recognize text with tesseract for python
    # result = pytesseract.image_to_string(Image.open("thres.png"), config='-psm 6', lang='eng')
    result = pytesseract.image_to_string(Image.open("thres.png"))

    return result

if __name__ == "__main__":
    userinfoWindow()