package com.example.jasoseol;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloader {
    public void download(String url, ImageView imageView){
        if(cancelPotentialDownload(url, imageView)){
            BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
            DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
            imageView.setImageDrawable(downloadedDrawable);
            task.execute(url);
        }
    }

    public class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        String url;
        private final WeakReference<ImageView> imageViewWeakReference;

        public BitmapDownloaderTask(ImageView imageView) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(isCancelled()){
                bitmap = null;
            }
            if(imageViewWeakReference != null){
                ImageView imageView = imageViewWeakReference.get();
               BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
               if(this == bitmapDownloaderTask) {
                   imageView.setImageBitmap(bitmap);
               }
            }
            super.onPostExecute(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadBitmap(strings[0]);
        }

        public Bitmap downloadBitmap(final String imgURL){
            final Bitmap[] bitmap = new Bitmap[1];
            Thread thread = new Thread(){
                @Override
                public void run() {
                    try{
                        URL url = new URL(imgURL);
                        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap[0] = BitmapFactory.decodeStream(is);

                    }catch (MalformedURLException e){
                        e.printStackTrace();
                        Log.d("이미지 요청==", "실패");
                    }catch (IOException e){
                        e.printStackTrace();
                        Log.d("IOE==", "발생");
                    }
                }
            };
            thread.start();
            try{
                thread.join();
                return bitmap[0];
            }catch (InterruptedException e){
                e.printStackTrace();
                Log.d("쓰레드 인터럽트==", "발생");
            }
            return bitmap[0];
        }
    }


    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskWeakReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(Color.WHITE);
            this.bitmapDownloaderTaskWeakReference = new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskWeakReference.get();
        }
    }
    public static boolean cancelPotentialDownload(String url, ImageView imageView){
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if(bitmapDownloaderTask != null){
            String bitmapURL = bitmapDownloaderTask.url;
            if((bitmapURL == null) || (!bitmapURL.equals(url))){
                bitmapDownloaderTask.cancel(true);
            }else{
                return false;
            }
        }
        return true;
    }
    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView){
        if(imageView != null){
            Drawable drawable = imageView.getDrawable();
            if(drawable instanceof DownloadedDrawable){
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }
}