package com.example.loatandfind;




import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class image_loader extends AsyncTask<String[], Void, Bitmap[]> {


    @Override

    protected void onPreExecute() {

        super.onPreExecute();

    }



    @Override

    protected Bitmap[] doInBackground(String[]... params) {

        Bitmap[] bitMapList = new Bitmap[params[0].length];

        for (int i=0; i<params[0].length; i++) {

            URL url;

            try {

                url = new URL(params[0][i]);

                Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                bitMapList[i] = bitmap;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

        return bitMapList;

    }



    @Override

    protected void onPostExecute(Bitmap[] result) {

        super.onPostExecute(result);

    }


}
