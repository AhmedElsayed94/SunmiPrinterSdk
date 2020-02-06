package com.ahmedelsayed.sunmiprinterapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ahmedelsayed.sunmiprinterutill.PrintMe;

public class MainActivity extends AppCompatActivity {

   private PrintMe printMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printMe =  new PrintMe(this);
    }

    public void printText(View v){
      printMe.sendTextToPrinter("Testing .. PrintMe",18,true,false,2);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void printImage(View v){
      printMe.sendImageToPrinter(
              printMe.convertDrawableToBitmap(getDrawable(R.mipmap.ic_launcher_round),100,100)
      );
    }

    public void printLayout(View v){
      View view = findViewById(R.id.print_me_layout);
      printMe.sendViewToPrinter(view);
    }



}
