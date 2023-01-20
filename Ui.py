from tkinter import *
import LoginScreen


def start():
    root = Tk()
    root.title('Smart Parking System')
    root.geometry("1920x1080")
    LoginScreen.beginUi(root)
    root.mainloop()
start()


