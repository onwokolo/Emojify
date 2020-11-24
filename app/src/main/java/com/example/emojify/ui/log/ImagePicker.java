package com.example.emojify.ui.log;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;

import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.FileProvider;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.Frame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ImagePicker {

    private static final int DEFAULT_MIN_WIDTH_QUALITY = 400;        // min pixels
    private static final String TAG = "ImagePicker";
    private static final String TEMP_IMAGE_NAME = "tempImage";

    public static int minWidthQuality = DEFAULT_MIN_WIDTH_QUALITY;


    public static Intent getPickImageIntent(Context context) {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", getTempFile(context));
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        intentList = addIntentsToList(context, intentList, pickIntent);
        intentList = addIntentsToList(context, intentList, takePhotoIntent);

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1), "Choose an app");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        return chooserIntent;
    }

    private static List<Intent> addIntentsToList(Context context, List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
            Timber.d("Intent: " + intent.getAction() + " package: " + packageName);
        }
        return list;
    }


    public static Bitmap getImageFromResult(Context context, int resultCode,
                                            Intent imageReturnedIntent) {
        Timber.d("getImageFromResult, resultCode: %s", resultCode);
        Bitmap bm = null;
        File imageFile = getTempFile(context);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImage;
            boolean isCamera = (imageReturnedIntent == null ||
                    imageReturnedIntent.getData() == null  ||
                    imageReturnedIntent.getData().toString().contains(imageFile.toString()));
            if (isCamera) {     /** CAMERA **/
                selectedImage = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", getTempFile(context));
            } else {            /** ALBUM **/
                selectedImage = imageReturnedIntent.getData();
            }
            Timber.d("selectedImage: %s", selectedImage);

            bm = getImageResized(context, selectedImage);
            int rotation = getRotation(context, selectedImage, isCamera);
            bm = rotate(bm, rotation);
        }
        return bm;
    }


    private static File getTempFile(Context context) {
        File imageFile = new File(context.getExternalCacheDir(), TEMP_IMAGE_NAME);
        imageFile.getParentFile().mkdirs();
        return imageFile;
    }

    private static Bitmap decodeBitmap(Context context, Uri theUri, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;

        AssetFileDescriptor fileDescriptor = null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(theUri, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap actuallyUsableBitmap = BitmapFactory.decodeFileDescriptor(
                fileDescriptor.getFileDescriptor(), null, options);

        Timber.d(options.inSampleSize + " sample method bitmap ... " +
                actuallyUsableBitmap.getWidth() + " " + actuallyUsableBitmap.getHeight());

        return actuallyUsableBitmap;
    }

    /**
     * Resize to avoid using too much memory loading big images (e.g.: 2560*1920)
     **/
    private static Bitmap getImageResized(Context context, Uri selectedImage) {
        Bitmap bm = null;
        int[] sampleSizes = new int[]{5, 3, 2, 1};
        int i = 0;
        do {
            bm = decodeBitmap(context, selectedImage, sampleSizes[i]);
            Timber.d("resizer: new bitmap width = %s", bm.getWidth());
            i++;
        } while (bm.getWidth() < minWidthQuality && i < sampleSizes.length);
        return bm;
    }


    private static int getRotation(Context context, Uri imageUri, boolean isCamera) {
        int rotation;
        Timber.e(String.valueOf(isCamera));
        if (isCamera) {
            rotation = getRotationFromCamera(context, imageUri);
        } else {
            rotation = getRotationFromGallery(context, imageUri);
        }
        Timber.d("Image rotation: %s", rotation);
        return rotation;
    }

    private static int getRotationFromCamera(Context context, Uri imageFile) {
        int rotate = 0;
        try {

            context.getContentResolver().notifyChange(imageFile, null);
            ExifInterface exif = new ExifInterface(context.getContentResolver().openAssetFileDescriptor(imageFile, "r").getFileDescriptor());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rotate;
    }

    public static int getRotationFromGallery(Context context, Uri imageUri) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            String[] columns = {MediaStore.Images.Media.ORIENTATION};
            try (Cursor cursor = context.getContentResolver().query(imageUri, columns, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int orientationColumnIndex = cursor.getColumnIndex(columns[0]);
                    result = cursor.getInt(orientationColumnIndex);
                }
            } catch (Exception e) {
                //Do nothing
            }
        }
        return result;
    }

    private static Bitmap rotate(Bitmap bm, int rotation) {
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        }
        return bm;
    }

    public static Bitmap rotateResourceImage(Resources res, int resId) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //use the compression format of your need
        InputStream inputStream = new ByteArrayInputStream(stream.toByteArray());
        return rotateResourceImage(bitmap, inputStream);
    }

    private static Bitmap rotateResourceImage(Bitmap bitmap, InputStream inputStream) throws IOException {
        int rotate = 0;
        ExifInterface exif;
        exif = new ExifInterface(inputStream);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    private static Frame convertBitmapToFrame(Bitmap bitmap){
        AndroidFrameConverter converter = new AndroidFrameConverter();
        return converter.convert(bitmap);
    }

    public static int getFaceCount(Bitmap bitmap){
        int maxFaces = 1;
        PointF mid = new PointF();
        Bitmap bitmap565 = bitmap.copy(Bitmap.Config.RGB_565,true);
        FaceDetector.Face[] faces = new FaceDetector.Face[maxFaces];
        FaceDetector faceDetector = new FaceDetector(bitmap.getWidth(),bitmap.getHeight(), maxFaces);
        int faceCount = faceDetector.findFaces(bitmap565, faces);
        FaceDetector.Face face = faces[0];
        Log.d("FaceDetection", "Face Count: " + String.valueOf(faceCount));
        return faceCount;
    }

    /*public static int detectFaces(Bitmap bmp, int num_faces) {
        FaceDetector face_detector = new FaceDetector(bmp.getWidth(), bmp.getHeight(), num_faces);
        FaceDetector.Face[] faces = new FaceDetector.Face[num_faces];
        int face_count = face_detector.findFaces(bmp, faces);

        return face_count;
    }*/
}