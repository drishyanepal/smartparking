import time
#
# current_time = time.strftime("%H:%M:%S")
# next_time = '22:30:49'
# print(time.strftime("%I:%M:%S"))
# diff = next_time - current_time
# print(diff)
from datetime import datetime

entry_time = '20:48:23'
current_time = time.strftime("%H:%M:%S")
time_format_str = '%H:%M:%S'
time1 = datetime.strptime(entry_time, time_format_str)
time2 = datetime.strptime(current_time, time_format_str)
diff = time2 - time1
diff_in_hours = diff.total_seconds() / 3600
a = round(diff_in_hours, 2)
print(a)


# date_1 = '11:13:08'
# date_2 = '12:14:18'
# date_format_str = '%H:%M:%S'
# start = datetime.strptime(date_1, date_format_str)
# end = datetime.strptime(date_2, date_format_str)
# # Get interval between two timstamps as timedelta object
# diff = end - start
# # Get interval between two timstamps in hours
# diff_in_hours = diff.total_seconds() / 3600
# print('Difference between two datetimes in hours:')
# print(diff_in_hours)
