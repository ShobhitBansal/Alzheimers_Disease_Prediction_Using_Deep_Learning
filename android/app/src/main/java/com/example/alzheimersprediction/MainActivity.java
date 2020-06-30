package com.example.alzheimersprediction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView predictionTextView;


    public void getPhoto(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);

        predictionTextView = findViewById(R.id.predTextView);
        predictionTextView.setVisibility(View.INVISIBLE);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage = data.getData();

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                Bitmap image = Bitmap.createScaledBitmap(bitmap, 150, 150, false);

                ImageView imageView = findViewById(R.id.scanImageView);
                imageView.setImageBitmap(bitmap);

                Classifier model = new Classifier(this);
                float[] predictions = model.classify(image);

                int argmax = -1;
                float max = 0;
                for (int i=0;i<predictions.length;i++){
                    if(predictions[i]>max){
                        argmax = i;
                        max=predictions[i];
                    }

                }

                Log.i("Predicted Label : ",Integer.toString(argmax));

                String output = "";
                switch (argmax){
                    case -1: output = "Unable To Predict";
                        break;
                    case 0: output = "Mild Demented";
                        break;
                    case 1: output = "Non Demented";
                        break;
                    case 2: output = "Very Mild Demented";
                        break;
                }

                if(argmax!=-1)
                    predictionTextView.setVisibility(View.VISIBLE);

                TextView outputView = findViewById(R.id.outputTextView);
                outputView.setText(output);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}