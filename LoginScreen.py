from tkinter import *
import Home


def beginUi(root):
    global username, password
    username = StringVar()
    password = StringVar()

    global frame
    frame = Frame(root)

    l0 = Label(frame, text="Enter Login Details:", font="Bold 20")
    l1 = Label(frame, text="Username:", font="TimesNewRoman 16")
    e1 = Entry(frame, textvariable=username, font="TimesNewRoman 16")
    l2 = Label(frame, text="Password:", font="TimesNewRoman 16")
    e2 = Entry(frame, textvariable=password, show="*", font="TimesNewRoman 16")
    b1 = Button(frame, text="Log in", command=lambda: login(root), font="TimesNewRoman 12", bg='#DCDCDC')

    l0.grid(row=0, column=2, pady=30, sticky=NSEW, columnspan=2)
    l1.grid(row=1, column=1, padx=20, pady=3)
    e1.grid(row=1, column=2, pady=3)
    l2.grid(row=2, column=1, pady=3)
    e2.grid(row=2, column=2, pady=3)
    b1.grid(row=3, column=2, pady=10, sticky=NSEW)

    frame.pack(pady=100)


def login(root):
    username_get = username.get()
    password_get = password.get()

    if username_get == "nce" and password_get == "admin":
        print("login Successful")
        frame.destroy()
        Home.ShowSecondScreen(root)

    else:
        Label(frame, text="Invalid username or password", fg='#f00', font='Times 14').grid(row=4, column=2)
