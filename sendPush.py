import FCMManager as fcm

tokens = [
    "cfZwmasyT2qDUyQbltTj0r:APA91bFpy-SvgHB6-a-Vb4QDc3KdYeBuy_vdWzGuekn5UhPoGiJ_7PrK_ogPoJpZLWRwNhkXaeE3F8tD-pWeelwneYGtAaLur1-cMU60Ywk7HRIjtBm2exj7Lj6Bu_8G_2yhJPToAKez"]
fcm.sendPush("Hi", "This is my next msg", tokens)
