package com.ahmedelsayed.sunmiprinterutill;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.ahmedelsayed.sunmiprinterutill.model.AidlUtil;

public class PrintMe{

    public AidlUtil aidlUtil;

    public PrintMe(Context context) {
        aidlUtil = AidlUtil.getInstance();
        aidlUtil.connectPrinterService(context);
    }

    public  void sendImageToPrinter(Bitmap bitmap) {
        aidlUtil.printBitmap(bitmap);
    }

    public  void sendTextToPrinter(String text, float size, boolean isBold, boolean isUnderLine, int lineBreak) {
        aidlUtil.printText(text, size, isBold, isUnderLine, lineBreak);
    }

    public void sendViewToPrinter(View view){
        sendImageToPrinter(scaleImage(convertViewToBitmap(view)));
    }

    public Bitmap convertDrawableToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);
        return mutableBitmap;
    }


    private Bitmap convertViewToBitmap(final View mView) {
        @SuppressLint("Range") final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.MATCH_PARENT, View.MeasureSpec.UNSPECIFIED);
        @SuppressLint("Range") final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED);
        mView.measure(widthMeasureSpec, heightMeasureSpec);
        Bitmap b = Bitmap.createBitmap(mView.getMeasuredWidth(), mView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        mView.layout(0, 0, mView.getMeasuredWidth(), mView.getMeasuredHeight());
        mView.draw(c);
        return b;
    }

    private Bitmap scaleImage(Bitmap bitmap1) {
        int width = bitmap1.getWidth();
        int height = bitmap1.getHeight();
        int newWidth = 384;
        float scaleWidth = (float)newWidth / (float)width;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, 1.0F);
        return Bitmap.createBitmap(bitmap1, 0, 0, width, height, matrix, true);
    }

}
