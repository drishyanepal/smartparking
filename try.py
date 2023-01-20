import pickle

finalList = []
print(finalList)
with open('file.pkl', 'rb') as file:
    finalList = pickle.load(file)
    print(finalList)

