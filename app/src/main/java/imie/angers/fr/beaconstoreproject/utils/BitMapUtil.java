package imie.angers.fr.beaconstoreproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by Anne on 22/02/2016.
 */
public class BitMapUtil {

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromByte(byte[] arrayBytes) {
        return BitmapFactory.decodeByteArray(arrayBytes, 0, arrayBytes.length);
    }

    public static Bitmap getBitmapFromString(String imgPath) {
        return BitmapFactory.decodeFile(imgPath);
    }

    public static String downloadImage(String url, String idPromo, String imageType) {

        Bitmap bmImage = null;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/beaconStoreImages");
        String filePath = "Image_"+ idPromo +"_" + imageType + ".jpg";
        File file = new File(myDir, filePath);

        try {

            InputStream in = new java.net.URL(url).openStream();
            bmImage = BitmapFactory.decodeStream(in);

            myDir.mkdirs();

            if (file.exists ()) file.delete ();

            FileOutputStream out = new FileOutputStream(file);
            bmImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return filePath;
    }
}
