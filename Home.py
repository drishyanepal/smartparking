from tkinter import *

import Video
from Video import *
from datetime import datetime
import time
import Main
import FCMManager as fcm

emptySlotsRecord = []
try:
    with open('file.pkl', 'rb') as file:
        emptySlotsRecord = pickle.load(file)
except:
    pass


def ShowSecondScreen(root):
    frameOne = Frame(root)

    monitorParkingButton = Button(frameOne, text='Monitor Parking', font="Bold 20", command=lambda: showFootage())

    vehicleEntryButton = Button(frameOne, text='Scan Number Plate (Entry)', font="Bold 20",
                                command=lambda: scanVehicleEntry())

    vehicleExitButton = Button(frameOne, text='Scan Number Plate (Exit)', font="Bold 20",
                               command=lambda: scanVehicleExit())

    checkLogsButton = Button(frameOne, text='Check Logs', font="Bold 20", command=lambda: checkLogs(root))

    monitorParkingButton.grid(row=0, column=1, pady=30, sticky=NSEW, columnspan=2)
    vehicleEntryButton.grid(row=1, column=1, pady=30)
    vehicleExitButton.grid(row=2, column=1, pady=30)
    checkLogsButton.grid(row=3, column=1, pady=30)

    frameOne.pack(side=TOP, fill=BOTH, expand=True, padx=600)


def checkLogs(root):
    database = Video.database
    logReference = database.child("Others").child("Logs").get()
    content = ""
    for log in logReference.each():
        print(log.key())
        properties = log.val()
        vNum = log.key()
        content += ("Date :  %s \n" % properties.get("date"))
        content += ("Vehicle Number :  %s \n" % vNum)
        content += ("Entry Time :  %s \n" % properties.get("entryTime"))
        content += ("Exit Time :  %s \n" % properties.get("exitTime"))
        content += ("\n")
    newWindow = Toplevel(root)
    newWindow.title("Logs")
    newWindow.geometry("600x600")

    Label(newWindow, text=content, font=15).pack()


def scanVehicleEntry():
    Main.startNPR()
    vehicleNumber = Main.numberPlateInString
    print(vehicleNumber)
    # checkVehicleNumberEntry(vehicleNumber)


def scanVehicleExit():
    Main.startNPR()
    vehicleNumber = Main.numberPlateInString
    checkVehicleNumberExit(vehicleNumber)


def checkVehicleNumberEntry(myVehicleNumber):
    tokens = []
    vehicleBooked = False
    vehicleSlotNumber = 0
    slotByVehicle = database.child("SlotsByVehicle").get()
    try:
        for row in slotByVehicle.each():
            if row.val() == myVehicleNumber:
                vehicleBooked = True
                vehicleSlotNumber = row.key()
                print(vehicleSlotNumber)
                break
            else:
                vehicleBooked = False
    except:
        pass
    if vehicleBooked:
        response = database.child("VehicleTokenMap").child(myVehicleNumber).get()
        token = "%s" % response.val()
        tokens.append(token)
        title = "Slot %s Booked" % vehicleSlotNumber
        body = "Dear user, Slot number %s is already booked for you. Please proceed there." % vehicleSlotNumber
        fcm.sendPush(title, body, tokens)

    else:
        try:
            emptySlot = findEmptySlot(myVehicleNumber)
            emptySlotInNumber = getSlotIdInNumber(emptySlot)
            response = database.child("VehicleTokenMap").child(myVehicleNumber).get()
            token = "%s" % response.val()
            tokens.append(token)
            title = "Slot %s Allocated" % emptySlotInNumber
            body = "Dear user, Slot %s is allocated for you. Please proceed there." % emptySlotInNumber
            fcm.sendPush(title, body, tokens)
        except:
            pass


def checkVehicleNumberExit(myVehicleNumber):
    vehicleBooked = False
    slotByVehicle = database.child("SlotsByVehicle").get()
    try:
        for row in slotByVehicle.each():
            if row.val() == myVehicleNumber:
                vehicleBooked = True
                break
            else:
                vehicleBooked = False
    except:
        pass
    if vehicleBooked:
        # check if parking time exceed
        bookedDuration = database.child("BookDetails").child("TimeDetails").child(myVehicleNumber).child(
            "bookedDuration").get().val()
        get_time = database.child("BookDetails").child("TimeDetails").child(myVehicleNumber).child(
            "entryTime").get().val()
        current_time = time.strftime("%H:%M:%S")
        # if "pm" in get_time:
        #     x = get_time.split("pm")
        # else:
        #     x = get_time.split("am")
        # entry_time = x[0]
        entry_time = get_time
        time_format_str = '%H:%M:%S'
        time1 = datetime.strptime(entry_time, time_format_str)
        time2 = datetime.strptime(current_time, time_format_str)
        diff = time2 - time1
        diff_in_hours = diff.total_seconds() / 3600
        total_Duration = round(diff_in_hours, 2)
        x = total_Duration - int(bookedDuration)
        if x > 0:
            amount = int(x * 50)
        else:
            amount = 0
        database.child("BookDetails").child("TimeDetails").child(myVehicleNumber).child("cost").set(str(amount))
        database.child("BookDetails").child("TimeDetails").child(myVehicleNumber).child("exitTime").set(current_time)
        database.child("BookDetails").child("TimeDetails").child(myVehicleNumber).child("parkedDuration").set(
            str(total_Duration))
    # vehicle_not_booked
    else:
        entry_time = database.child("BookDetails").child("TimeDetails").child(myVehicleNumber).child(
            "entryTime").get().val()
        current_time = time.strftime("%H:%M:%S")
        time_format_str = '%H:%M:%S'
        time1 = datetime.strptime(entry_time, time_format_str)
        time2 = datetime.strptime(current_time, time_format_str)
        diff = time2 - time1
        diff_in_hours = diff.total_seconds() / 3600
        total_Duration = round(diff_in_hours, 2)
        amount = total_Duration * 50
        database.child("BookDetails").child("TimeDetails").child(myVehicleNumber).child("cost").set(amount)
        database.child("BookDetails").child("TimeDetails").child(myVehicleNumber).child("exitTime").set(current_time)
        database.child("BookDetails").child("TimeDetails").child(myVehicleNumber).child("parkedDuration").set(
            total_Duration)


def findEmptySlot(my_vehicle_number):
    print("Finding empty slot")
    emptySlot = "100"
    reference = database.child("Slots").get()
    for i in reference.each():
        if i.val() == 0:
            valueOne = i.key()
            if valueOne not in emptySlotsRecord and valueOne != "slotTwenty" and \
                    valueOne != "slotTwentyThree" and valueOne != "slotFiftyTwo":
                emptySlotsRecord.append(valueOne)
                emptySlot = valueOne
                with open('file.pkl', 'wb') as file:
                    pickle.dump(emptySlotsRecord, file)
                database.child("SlotsByVehicle").child(getSlotIdInNumber(emptySlot)).set(my_vehicle_number)
                break
    print(emptySlot)
    return emptySlot


def showFootage():
    while True:
        if cap.get(cv2.CAP_PROP_POS_FRAMES) == cap.get(cv2.CAP_PROP_FRAME_COUNT):
            cap.set(cv2.CAP_PROP_POS_FRAMES, 0)

        success, img = cap.read()
        imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        imgBlur = cv2.GaussianBlur(imgGray, (3, 3), 1)
        imgThreshold = cv2.adaptiveThreshold(imgBlur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                                             cv2.THRESH_BINARY_INV, 25, 16);
        imgMedian = cv2.medianBlur(imgThreshold, 5)
        kernel = np.ones((3, 3), np.int8)
        imgDilate = cv2.dilate(imgMedian, kernel, iterations=1)

        checkParkingSpace(imgDilate, img)

        cv2.imshow("Ima", img)
        # cv2.imshow("ImageGray", imgGray)
        # cv2.imshow("ImageBlur", imgBlur)
        # cv2.imshow("ImageThresh", imgMedian)
        cv2.waitKey(10)


def getSlotIdInNumber(slotId):
    if slotId == "slotSeven":
        return 7

    if slotId == "slotTwentySeven":
        return 27

    if slotId == "slotThirtyFour":
        return 34

    if slotId == "slotForty":
        return 40

    if slotId == "slotFortyThree":
        return 43

    if slotId == "slotFortyFour":
        return 44

    if slotId == "slotFiftyThree":
        return 53

    if slotId == "slotFiftyFive":
        return 55

    if slotId == "slotFiftySeven":
        return 57

    if slotId == "slotFiftyNine":
        return 59

    if slotId == "slotSixtySix":
        return 66

    if slotId == "slotSixtySeven":
        return 67

# checkVehicleNumber("BAA2010")
