import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3' 

import numpy  as np
from keras.preprocessing import image
from pathlib import Path
from tensorflow.keras.models import load_model
import pickle

model = load_model("./home/model/alzheimers_model.hdf5")
model._make_predict_function()

def predictdisease(image_path):

    img = image.load_img(image_path,target_size=(150,150))
    img = image.img_to_array(img)
    img = img.reshape((1,150,150,3))

    output = model.predict(img)

    with open("./home/model/reverse_dict.pkl",'rb') as f:
        reverse_dict = pickle.load(f)

    result = reverse_dict[np.argmax(output[0])]

    return result