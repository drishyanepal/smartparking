import cv2
import pickle
import cvzone
import numpy as np
import pyrebase

width = 110
height = 46
cap = cv2.VideoCapture('carPark.mp4')
slotCount = 0
slotOne = 1  # 0 available 1 not available 2 booked
slotTwo = 0
slotThree = 0
slotFour = 0
bookedSlot = []
with open('POSITIONS', 'rb') as f:
    posList = pickle.load(f)
# --------------------------------------------------------------------------------------------------
firebaseConfig = {
    "apiKey": "AIzaSyDLajRhuBAYYWMj0nXeCDb1FB95-DUXaNs",
    "authDomain": "smartparking-357310.firebaseapp.com",
    "databaseURL": "https://smartparking-357310-default-rtdb.firebaseio.com",
    "projectId": "smartparking-357310",
    "storageBucket": "smartparking-357310.appspot.com",
    "messagingSenderId": "778365085754",
    "appId": "1:778365085754:web:e745c407eda67f1ceeed03",
    "measurementId": "G-DEP2VD8Y7F"
}

firebase = pyrebase.initialize_app(firebaseConfig)
database = firebase.database()

slots = database.child("BookDetails").child("SlotsBooked").get()

try:
    for slot in slots.each():
        slotItem = slot.val()
        bookedSlot.append(slotItem)
        # print(bookedSlot)
except:
    print("null")


def updateBookedSlots():
    global bookedSlot
    bookedSlot = []
    items = database.child("BookDetails").child("SlotsBooked").get()
    try:
        for item in items.each():
            status = item.val()
            bookedSlot.append(status)
            print(bookedSlot)
    except:
        print("null")


# -----------------------------------------------------------------------------------------------------------

def checkParkingSpace(imgPro, img):
    global slotCount
    slotCount += 1
    id = 1
    vacant = 10

    if slotCount % 20 == 0:
        updateBookedSlots()

    for pos in posList:
        slotNumber = posList.index(pos)
        # print(slotCount)
        x, y = pos
        imgCrop = imgPro[y:y + height, x:x + width]
        # cv2.imshow(str(x * y), imgCrop)
        count = cv2.countNonZero(imgCrop)
        cvzone.putTextRect(img, "NCE Parking", (70, 70), scale=2, thickness=2, offset=0)
        cvzone.putTextRect(img, str(id), (x, y + height - 10), scale=1, thickness=2, offset=0)

        if count < 800:
            color = (0, 255, 0)
            for slotItemNumber in bookedSlot:
                if slotNumber == slotItemNumber - 1:
                    color = (0, 255, 255)
            thickness = 5
            if slotCount % 100 == 0:
                count += 1
                if slotNumber + 1 in bookedSlot:
                    pass
                else:
                    if slotNumber == 19:  # slot0
                        database.child("Slots").child("slotTwenty").set(0)
                        vacant += 1
                        database.child("Slots").child("NceVacant").set(str(vacant))
                        print(vacant)
                    elif slotNumber == 22:  # slot1
                        database.child("Slots").child("slotTwentyThree").set(0)
                        vacant += 1
                        database.child("Slots").child("NceVacant").set(str(vacant))
                        print(vacant)
                    elif slotNumber == 26:  # slot1
                        database.child("Slots").child("slotTwentySeven").set(0)
                        vacant += 1
                        database.child("Slots").child("NceVacant").set(str(vacant))
                        print(vacant)
                    elif slotNumber == 33:  # slot1
                        database.child("Slots").child("slotThirtyFour").set(0)
                        vacant += 1
                        database.child("Slots").child("NceVacant").set(str(vacant))
                        print(vacant)
                    elif slotNumber == 39:  # slot1
                        database.child("Slots").child("slotForty").set(0)
                        vacant += 1
                        database.child("Slots").child("NceVacant").set(str(vacant))
                        print(vacant)
                    elif slotNumber == 51:  # slot2
                        database.child("Slots").child("slotFiftyTwo").set(0)
                        vacant += 1
                        database.child("Slots").child("NceVacant").set(str(vacant))
                        print(vacant)
        else:
            color = (0, 0, 255)
            thickness = 2
            if slotCount % 100 == 0:
                if slotNumber == 19:  # slot0
                    database.child("Slots").child("slotTwenty").set(1)
                    vacant -= 1
                    database.child("Slots").child("NceVacant").set(str(vacant))
                    print(vacant)
                elif slotNumber == 22:  # slot1
                    database.child("Slots").child("slotTwentyThree").set(1)
                    vacant -= 1
                    database.child("Slots").child("NceVacant").set(str(vacant))
                    print(vacant)
                elif slotNumber == 51:  # slot2
                    database.child("Slots").child("slotFiftyTwo").set(1)
                    vacant -= 1
                    database.child("Slots").child("NceVacant").set(str(vacant))
                    print(vacant)

        cv2.rectangle(img, pos, (pos[0] + width, pos[1] + height), color, thickness)
        id += 1


if __name__ == "__main__":
    while True:

        if cap.get(cv2.CAP_PROP_POS_FRAMES) == cap.get(cv2.CAP_PROP_FRAME_COUNT):
            cap.set(cv2.CAP_PROP_POS_FRAMES, 0)

        success, img = cap.read()
        imgGray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
        imgBlur = cv2.GaussianBlur(imgGray, (3, 3), 1)
        imgThreshold = cv2.adaptiveThreshold(imgBlur, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C,
                                             cv2.THRESH_BINARY_INV, 25, 16)
        imgMedian = cv2.medianBlur(imgThreshold, 5)
        kernel = np.ones((3, 3), np.int8)
        imgDilate = cv2.dilate(imgMedian, kernel, iterations=1)
        checkParkingSpace(imgDilate, img)

        cv2.imshow("Ima", img)
        # cv2.imshow("ImageGray", imgGray)
        # cv2.imshow("ImageBlur", imgBlur)
        # cv2.imshow("ImageThresh", imgMedian)
        cv2.waitKey(0)
