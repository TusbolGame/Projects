from selenium import webdriver
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
import sys
from selenium.webdriver.common.by import By
from selenium.webdriver.common.action_chains import ActionChains
import xlsxwriter
from PyQt5 import QtCore, QtGui, QtWidgets
from PyQt5.QtWidgets import QApplication, QWidget, QInputDialog, QLineEdit, QFileDialog, QMessageBox
import pandas as pd
import threading


class Ui_Form(object):

    urls = ''
    success = 0
    status = 0

    def setupUi(self, Form):
        Form.setObjectName("Form")
        Form.resize(977, 718)
        font = QtGui.QFont()
        font.setPointSize(10)
        font.setBold(True)
        font.setWeight(75)
        Form.setFont(font)
        self.gridLayout = QtWidgets.QGridLayout(Form)
        self.gridLayout.setObjectName("gridLayout")
        self.verticalLayout = QtWidgets.QVBoxLayout()
        self.verticalLayout.setObjectName("verticalLayout")
        self.horizontalLayout = QtWidgets.QHBoxLayout()
        self.horizontalLayout.setObjectName("horizontalLayout")
        self.filePath = QtWidgets.QLineEdit(Form)
        self.filePath.setObjectName("filePath")
        self.horizontalLayout.addWidget(self.filePath)
        self.fileBrowse = QtWidgets.QPushButton(Form)
        self.fileBrowse.setObjectName("fileBrowse")
        self.horizontalLayout.addWidget(self.fileBrowse)
        self.verticalLayout.addLayout(self.horizontalLayout)
        self.verticalLayout_8 = QtWidgets.QVBoxLayout()
        self.verticalLayout_8.setObjectName("verticalLayout_8")
        self.urlList = QtWidgets.QListWidget(Form)
        font = QtGui.QFont()
        font.setPointSize(10)
        font.setBold(False)
        font.setWeight(50)
        self.urlList.setFont(font)
        self.urlList.setObjectName("urlList")
        self.verticalLayout_8.addWidget(self.urlList)
        self.verticalLayout.addLayout(self.verticalLayout_8)
        self.verticalLayout_2 = QtWidgets.QVBoxLayout()
        self.verticalLayout_2.setObjectName("verticalLayout_2")
        self.progressBar = QtWidgets.QProgressBar(Form)
        self.progressBar.setProperty("value", 0)
        self.progressBar.setObjectName("progressBar")
        self.verticalLayout_2.addWidget(self.progressBar)
        self.horizontalLayout_2 = QtWidgets.QHBoxLayout()
        self.horizontalLayout_2.setObjectName("horizontalLayout_2")
        spacerItem = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
        self.horizontalLayout_2.addItem(spacerItem)
        self.startButton = QtWidgets.QPushButton(Form)
        self.startButton.setObjectName("startButton")
        self.horizontalLayout_2.addWidget(self.startButton)
        spacerItem1 = QtWidgets.QSpacerItem(40, 20, QtWidgets.QSizePolicy.Expanding, QtWidgets.QSizePolicy.Minimum)
        self.horizontalLayout_2.addItem(spacerItem1)
        self.verticalLayout_2.addLayout(self.horizontalLayout_2)
        self.verticalLayout.addLayout(self.verticalLayout_2)
        self.gridLayout.addLayout(self.verticalLayout, 1, 0, 1, 1)

        self.retranslateUi(Form)
        self.fileBrowse.clicked.connect(self.fileBrowse_Clicked)
        self.startButton.clicked.connect(self.startButton_Clicked)
        QtCore.QMetaObject.connectSlotsByName(Form)

        self.urls = list()

    def fileBrowse_Clicked(self):
        options = QFileDialog.Options()
        options |= QFileDialog.DontUseNativeDialog
        fileName, _ = QFileDialog.getOpenFileName(Form, "QFileDialog.getOpenFileName()", "",
                                                  "Excel Files (*.xlsx);;All Files (*)", options=options)
        global users_data
        self.filePath.setText(fileName)
        if fileName:
            try:
                links = pd.read_excel(fileName).values.tolist()

                for i in range(0, len(links)):
                    try:
                        link = links[i][2]
                        if 'http' in link:
                            self.urls.append(link)
                    except:
                        continue

            except Exception as inst:
                print(inst)
    def startButton_Clicked(self):

        if len(self.urls) == 0:
            msg = QtWidgets.QMessageBox()
            msg.setIcon(QtWidgets.QMessageBox.Information)

            msg.setText("There is no Scraping urls")
            msg.setWindowTitle("Attention")
            msg.setStandardButtons(QtWidgets.QMessageBox.Ok)
            msg.exec_()
            return

        perusernumber = 10
        loop_count = int(len(self.urls) / perusernumber)
        # for i in range(0, len(self.urls)):
        #     main = threading.Thread(target=self.run_scraping, args=[self.urls[i]])
        #     main.start()
        #     main.join()

        for loop in range(0, loop_count):
            thread_list = []
            for i in range(0, perusernumber):
                thread = threading.Thread(target=self.run_scraping, args=[loop * perusernumber + i])
                thread_list.append(thread)
            for thread in thread_list:
                thread.start()
            for thread in thread_list:
                thread.join()
            thread_list.clear()
        if len(users_data) > perusernumber * loop_count:
            thread_list = []
            for i in range(0, len(users_data) - perusernumber * loop_count):
                thread = threading.Thread(target=self.run_scraping, args=[ loop_count * perusernumber + i])
                thread_list.append(thread)
            for thread in thread_list:
                thread.start()
            for thread in thread_list:
                thread.join()
            thread_list.clear()

    def retranslateUi(self, Form):
        _translate = QtCore.QCoreApplication.translate
        Form.setWindowTitle(_translate("Form", "Scraper GUI"))
        self.fileBrowse.setText(_translate("Form", "Browse"))
        self.startButton.setText(_translate("Form", "Start"))

    def run_scraping(self, index):
        url = self.urls[index]
        chrome_options = webdriver.ChromeOptions()
        prefs = {"profile.default_content_setting_values.notifications": 2}
        chrome_options.add_experimental_option("prefs", prefs)
        chrome_options.add_argument("log-level=3")
        chrome_options.add_argument("start-maximized")
        driver = webdriver.Chrome(options=chrome_options)
        title = url.split("/")[len(url.split("/")) - 1]
        # load inning url
        # url = "https://www.espncricinfo.com/series/8048/commentary/335982/royal-challengers-bangalore-vs-kolkata-knight-riders-1st-match-indian-premier-league-2007-08"
        print('processing...')
        driver.get(url)

        links = driver.find_element_by_xpath(
            '//*[@id="main-container"]/div/div[3]/div[1]/article/header/div/div[1]/div[1]/ul').find_elements_by_class_name(
            'react-router-link')

        workbook = xlsxwriter.Workbook(title + '.xlsx')

        for inning in range(0, len(links)):
            links = driver.find_element_by_xpath(
                '//*[@id="main-container"]/div/div[3]/div[1]/article/header/div/div[1]/div[1]/ul').find_elements_by_class_name(
                'react-router-link')
            link_url = links[inning].get_attribute('href')
            worksheet = workbook.add_worksheet()
            worksheet.name = driver.find_element_by_xpath(
                '//*[@id="main-container"]/div/div[3]/div[1]/article/header/div/div[1]/div[1]/button').text
            worksheet.write(0, 0, 'Over Number')
            worksheet.write(0, 1, 'Over Score')
            worksheet.write(0, 2, 'bowler')
            worksheet.write(0, 3, 'batsman')

            driver.get(link_url)

            # icon-arrow-up-solid-before
            start_time = time.time()

            while True:
                driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
                time.sleep(0.1)
                driver.execute_script("window.scrollTo(document.body.scrollHeight, 300);")
                time.sleep(0.2)
                driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
                time.sleep(0.1)
                elapsed_time = time.time() - start_time
                if elapsed_time > 8:
                    break
            over_items = driver.find_elements_by_class_name('commentary-item')

            for i in range(0, len(over_items)):
                item = over_items[i]
                try:
                    over_number = item.find_element_by_class_name('time-stamp')
                    over_score = item.find_element_by_class_name('over-score')
                    description = item.find_element_by_class_name('description').text
                    description = description[0:description.index(',')]
                    name_list = description.split(' ')
                    worksheet.write(i + 1, 0, over_number.text)
                    worksheet.write(i + 1, 1, over_score.text)
                    worksheet.write(i + 1, 2, name_list[0])
                    worksheet.write(i + 1, 3, name_list[2])
                except:
                    continue
        self.success = self.success + 1
        self.progressBar.setValue(int(self.success * 100 / len(self.urls)))
        workbook.close()
        driver.close()


if __name__ == "__main__":
    import sys
    app = QtWidgets.QApplication(sys.argv)
    Form = QtWidgets.QWidget()
    ui = Ui_Form()
    ui.setupUi(Form)
    Form.show()
    sys.exit(app.exec_())
