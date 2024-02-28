package com.ahmedelsayed.sunmiprinterutill.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.ahmedelsayed.sunmiprinterutill.R;

import java.util.ArrayList;
import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;


public class AidlUtil {
    private static final String SERVICE_PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE_ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";
    private static final AidlUtil mAidlUtil = new AidlUtil();
    private IWoyouService woyouService;
    private Context context;



    private final ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };

    private final int[] darkness = new int[]{0x0600, 0x0500, 0x0400, 0x0300, 0x0200, 0x0100, 0,
            0xffff, 0xfeff, 0xfdff, 0xfcff, 0xfbff, 0xfaff};

    private AidlUtil() {
    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }


    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent();
        intent.setPackage(SERVICE_PACKAGE);
        intent.setAction(SERVICE_ACTION);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }


    public void disconnectPrinterService(Context context) {
        if (woyouService != null) {
            context.getApplicationContext().unbindService(connService);
            woyouService = null;
        }
    }

    public boolean isConnect() {
        return woyouService != null;

    }


    public ICallback generateCB(final PrinterCallback printerCallback) {
        return new ICallback.Stub() {


            @Override
            public void onRunResult(boolean isSuccess) {

            }

            @Override
            public void onReturnString(String result) {
                printerCallback.onReturnString(result);
            }

            @Override
            public void onRaiseException(int code, String msg) {

            }
        };
    }

    public void setDarkness(int index) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        int k = darkness[index];
        try {
            woyouService.sendRAWData(ESCUtil.setPrinterDarkness(k), null);
            woyouService.printerSelfChecking(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public List<String> getPrinterInfo(PrinterCallback printerCallback) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return null;
        }

        List<String> info = new ArrayList<>();
        try {
            woyouService.getPrintedLength(generateCB(printerCallback));
            info.add(woyouService.getPrinterSerialNo());
            info.add(woyouService.getPrinterModal());
            info.add(woyouService.getPrinterVersion());
            info.add(printerCallback.getResult());
            info.add("");
            //info.add(woyouService.getServiceVersion());
            PackageManager packageManager = context.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(SERVICE_PACKAGE, 0);
                if (packageInfo != null) {
                    info.add(packageInfo.versionName);
                    info.add(String.valueOf(packageInfo.versionCode));
                } else {
                    info.add("");
                    info.add("");
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return info;
    }


    public void initPrinter() {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.printerInit(null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void printQr(String data, int modulesize, int errorlevel) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.setAlignment(1, null);
            woyouService.printQRCode(data, modulesize, errorlevel, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void printBarCode(String data, int symbology, int height, int width, int textposition) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            woyouService.printBarCode(data, symbology, height, width, textposition, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void printText(String content, float size, boolean isBold, boolean isUnderLine, int lineBreak) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (isBold) {
                woyouService.sendRAWData(ESCUtil.boldOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.boldOff(), null);
            }

            if (isUnderLine) {
                woyouService.sendRAWData(ESCUtil.underlineWithOneDotWidthOn(), null);
            } else {
                woyouService.sendRAWData(ESCUtil.underlineOff(), null);
            }

            woyouService.printTextWithFont(content, null, size, null);
            woyouService.lineWrap(lineBreak, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    public void printBitmap(Bitmap bitmap) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.setAlignment(1, null);
            woyouService.printBitmap(bitmap, null);
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


//    public void printTable(LinkedList<TableItem> list) {
//        if (woyouService == null) {
//            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        try {
//            for (TableItem tableItem : list) {
//                Log.i("kaltin", "printTable: " + tableItem.getText()[0] + tableItem.getText()[1] + tableItem.getText()[2]);
//                woyouService.printColumnsString(tableItem.getText(), tableItem.getWidth(), tableItem.getAlign(), null);
//            }
//            woyouService.lineWrap(3, null);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }


    public void print3Line() {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.lineWrap(3, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void sendRawData(byte[] data) {
        if (woyouService == null) {
            Toast.makeText(context, R.string.toast_2, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            woyouService.sendRAWData(data, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
