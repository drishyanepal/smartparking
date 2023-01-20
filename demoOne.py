import cv2
import pickle

width = 110
height = 46

try:
    with open('POSITIONS', 'rb') as f:
        posList = pickle.load(f)
except:
    posList = []


def mouseclick(events, x, y, flags, params):
    if events == cv2.EVENT_LBUTTONDOWN:
        posList.append((x, y))
    if events == cv2.EVENT_RBUTTONDOWN:
        for i, pos in enumerate(posList):
            x1, y1 = pos
            if x1 < x < x1 + width and y1 < y < y1 + height:
                posList.pop(i)
    with open('POSITIONS', 'wb') as f:
        pickle.dump(posList, f)


while True:
    img = cv2.imread('carParkImg.png')
    for pos in posList:
        cv2.rectangle(img, pos, (pos[0] + width, pos[1] + height), (255, 0, 0), 2)

    cv2.imshow("Smart", img)
    cv2.setMouseCallback("Smart", mouseclick)
    cv2.waitKey(1)
