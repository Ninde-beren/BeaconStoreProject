package imie.angers.fr.beaconstoreproject.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 *
 * Permet de downloader + enregistrer l'image sur la carte sd du téléphone
 * Created by Anne on 24/02/2016.
 */
public class DownloadImage extends AsyncTask<Void, Void, Boolean> {

    private String url;
    private String idPromo;
    private String filePath;

    public DownloadImage(String url, String idPromo) {
        this.url = url;
        this.idPromo = idPromo;
    }

    protected Boolean doInBackground(Void... urls) {

        Bitmap bmImage = null;

        Boolean result;

        String root = Environment.getExternalStorageDirectory().toString();

        File myDir = new File(root + "/beaconStoreImages");

        filePath = "Image-"+ idPromo +".jpg";

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

            result = true;

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();

            result = false;
        }

        return result;
    }

    protected void onPostExecute(Boolean result) {

    }

    public String getFilePath() {
        return filePath;
    }
}


