package com.example.exif_lib;

import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.exifinterface.media.ExifInterface;

import java.io.IOException;

public class ExifMetadataManager {

    public interface ExifMetadataListener {
        void onConsentGiven();
        void onConsentDenied();
    }

    private Context context;

    public ExifMetadataManager(Context context) {
        this.context = context;
    }

    public void checkAndHandleExifMetadata(String imagePath, ExifMetadataListener listener) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);

            String datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
            String gpsLatitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String gpsLongitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String cameraModel = exif.getAttribute(ExifInterface.TAG_MODEL);
            String cameraMake = exif.getAttribute(ExifInterface.TAG_MAKE);
            String deviceIdentifier = exif.getAttribute(ExifInterface.TAG_IMAGE_UNIQUE_ID);

            // Log the EXIF metadata
            logExifData(datetime, gpsLatitude, gpsLongitude, cameraModel, cameraMake, deviceIdentifier);

            // Toast the warning to the user
            Toast.makeText(context, "EXIF metadata found in the image!", Toast.LENGTH_LONG).show();

            // Ask for user consent to remove EXIF metadata
            new AlertDialog.Builder(context)
                    .setTitle("Remove EXIF Metadata")
                    .setMessage("This image contains EXIF metadata. Do you want to remove it?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        removeExifMetadata(imagePath);
                        listener.onConsentGiven();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        listener.onConsentDenied();
                    })
                    .show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkExifMetadata(String imagePath) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);

            String datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
            String gpsLatitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String gpsLongitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String cameraModel = exif.getAttribute(ExifInterface.TAG_MODEL);
            String cameraMake = exif.getAttribute(ExifInterface.TAG_MAKE);
            String deviceIdentifier = exif.getAttribute(ExifInterface.TAG_IMAGE_UNIQUE_ID);

            // Log the EXIF metadata
            logExifData(datetime, gpsLatitude, gpsLongitude, cameraModel, cameraMake, deviceIdentifier);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logExifData(String datetime, String gpsLatitude, String gpsLongitude,
                             String cameraModel, String cameraMake, String deviceIdentifier) {
        System.out.println("DateTime: " + datetime);
        System.out.println("GPS Latitude: " + gpsLatitude);
        System.out.println("GPS Longitude: " + gpsLongitude);
        System.out.println("Camera Model: " + cameraModel);
        System.out.println("Camera Make: " + cameraMake);
        System.out.println("Device Identifier: " + deviceIdentifier);
    }

    private void removeExifMetadata(String imagePath) {
        try {
            ExifInterface exif = new ExifInterface(imagePath);

            exif.setAttribute(ExifInterface.TAG_DATETIME, null);
            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, null);
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, null);
            exif.setAttribute(ExifInterface.TAG_MODEL, null);
            exif.setAttribute(ExifInterface.TAG_MAKE, null);
            exif.setAttribute(ExifInterface.TAG_IMAGE_UNIQUE_ID, null);

            exif.saveAttributes();

            Toast.makeText(context, "EXIF metadata removed!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

