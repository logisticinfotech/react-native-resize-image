package com.demoimagecompress.RNImageCompressor;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.uimanager.IllegalViewOperationException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import static com.facebook.react.common.ReactConstants.TAG;

public class RNImageCompressorModule extends ReactContextBaseJavaModule {

    String fullpath=Environment.getExternalStorageDirectory()
            + "/Android/data/"
            + getReactApplicationContext().getPackageName()
            + "/images";

    String mImageName;

    public RNImageCompressorModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CompressImageManager";
    }

    @ReactMethod
    public void fetchPhotos(String uri,Callback errorCallBack,Callback successCallBack){
        try{
        String finalPath=getRealPathFromURI(getReactApplicationContext(), Uri.parse(uri));
        Bitmap bmp = BitmapFactory.decodeFile(finalPath);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 10, bos);
        storeImage(bmp);
        File file = new File(fullpath+"/"+mImageName);
        String finalResultPath="file://"+file.getPath();
            successCallBack.invoke(finalResultPath);
        }
        catch (IllegalViewOperationException e){
            errorCallBack.invoke(e.getMessage());
        }
     }

    public String getRealPathFromURI(ReactContext context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private  File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getReactApplicationContext().getPackageName()
                + "/images");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }

        File nomedia=new File(mediaStorageDir+"/.nomedia");

        if(!nomedia.exists()){
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Create a media file name
        Random rand = new Random();
        int n = rand.nextInt(200000000);
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        mImageName="LI_"+ timeStamp + n +".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

}
