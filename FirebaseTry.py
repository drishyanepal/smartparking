import pyrebase

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
# AUTH
# auth = firebase.auth()
# email = input("Enter Email: ")
# password = input("Enter Password: ")
# try:
#     auth.sign_in_with_email_and_password(email, password)
#     print("Login Successful")
# except:
#     print("Login Failed")
# read
