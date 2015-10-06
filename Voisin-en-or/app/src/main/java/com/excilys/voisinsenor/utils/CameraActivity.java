package com.excilys.voisinsenor.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.excilys.voisinsenor.R;
import com.excilys.voisinsenor.network.service.IUploadPhotoService;
import com.excilys.voisinsenor.ui.profile.ProfilActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import timber.log.Timber;

import static android.graphics.BitmapFactory.decodeFile;

/**
 * Created by mada on 23/09/15.
 */
public class CameraActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    public String path_photo = null;
    private String user_id = null;
    private Internet internet = null;
    private PopupActivity popupActivity = null;
    private RestAdapter restAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        internet = new Internet(this);
        popupActivity = new PopupActivity(this);
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.server_adress)).build();
        Uri fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);// create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                /*** Réorientation de l'image ***/

                path_photo = getResources().getString(R.string.sdcard_selfie);
                File imageSelfie = new File(path_photo);
                try {
                    File filePhotoNoRotated = new File(path_photo);
                    Bitmap bitmap = getCorrectBitmap(null, path_photo);
                    FileOutputStream fOut = new FileOutputStream(filePhotoNoRotated);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Thread thread = new Thread(new EnvoieSelfie(this));
                thread.start();

                Intent intent = new Intent(CameraActivity.this, ProfilActivity.class);
                //intent.putExtra("user_id",user_id);
                startActivity(intent);
                finish();


            } else if (resultCode == RESULT_CANCELED) {
                Intent intent = new Intent(CameraActivity.this, ProfilActivity.class);
                //intent.putExtra("user_id",user_id);
                startActivity(intent);
                finish();

            } else {
                // Image capture failed, advise user
                popupActivity.afficherPopup("Une erreur est survenue lors de la prise de photo", null);
                Intent intent = new Intent(CameraActivity.this, ProfilActivity.class);
                //  intent.putExtra("user_id",user_id);
                startActivity(intent);
                finish();
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                // Toast.makeText(this, "Video saved to:\n" +  data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }


    /**
     * Create a file Uri for saving an image or video
     */
    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile(int type) {

        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        // File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
        //         Environment.DIRECTORY_PICTURES), "PartyBay");
        File mediaStorageDir = new File(getResources().getString(R.string.sdcard_path));


        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(getResources().getString(R.string.sdcard_selfie));

        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    public Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        Log.d("Camera ", "decodeSampledBitmapFromFile class Camera");
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }

        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return decodeFile(path, options);
    }

    public void copy(File src, File dst) throws IOException {
        System.out.println("je copie " + src + " jusqu'a " + dst);
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    class EnvoieSelfie implements Runnable {
        Context context;

        public EnvoieSelfie(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            if (internet.internet()) {
                //TODO SEND PHOTO API
                File file = new File(getResources().getString(R.string.sdcard_selfie));
                TypedFile typedFile = new TypedFile("multipart/form-data", file);
                SharedPreferences sharedPref = getSharedPreferences(getResources().getString(R.string.myPref), Context.MODE_PRIVATE);
                String myemail = sharedPref.getString(getResources().getString(R.string.myemail), null);
                IUploadPhotoService photoToUpload = restAdapter.create(IUploadPhotoService.class);
                photoToUpload.upload(typedFile, myemail, new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        Timber.e("SUCCES");
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Timber.e("ERROR");
                    }
                });
            }else{
                popupActivity.afficherPopup(getResources().getString(R.string.error_internet), null);
            }
        }

    }
        public Bitmap getCorrectBitmap(Bitmap bitmap, String photo_path) {

            Bitmap bmp = decodeSampledBitmapFromFile(photo_path, 500, 300);
            Matrix matrix = new Matrix();
            float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1};
            Matrix matrixMirrorY = new Matrix();
            matrixMirrorY.setValues(mirrorY);
            matrix.postConcat(matrixMirrorY);
            ExifInterface ei;
            try {
                ei = new ExifInterface(photo_path);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90: {
                        matrix.postRotate(90);
                        Bitmap bMapRotate = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                        return bMapRotate;
                    }

                    case ExifInterface.ORIENTATION_ROTATE_180: {
                        matrix.postRotate(180);
                        Bitmap bMapRotate = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                        return bMapRotate;
                    }

                    case ExifInterface.ORIENTATION_ROTATE_270: {
                        matrix.postRotate(270);
                        Bitmap bMapRotate = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                        return bMapRotate;
                    }
                    default: {
                        Bitmap bMapRotate = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
                        return bMapRotate;
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return bitmap;
        }

}