import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3' 

import numpy  as np
from keras.preprocessing import image
from pathlib import Path
from keras.models import load_model
import pickle

folder_name = input("\nEnter the name of the input folder: ")
p = Path(folder_name)

img_name = []
data = []
cnt = 0
for imgs in p.glob("*"):
    name = str(imgs).split("\\")[-1]
    img_name.append(name)
    img = image.load_img(imgs,target_size=(150,150))
    img = image.img_to_array(img)
    data.append(img)

print("\nNumber of images: ",len(data))
Xtest = np.array(data)

model = load_model("./model/alzheimers_model.hdf5")
output = model.predict(Xtest)

with open("./model/reverse_dict.pkl",'rb') as f:
    reverse_dict = pickle.load(f)

result = []

for i in range(len(output)):
    result.append(reverse_dict[np.argmax(output[i])])

for i in range(len(img_name)):
    print("\n",img_name[i]," : ",result[i])