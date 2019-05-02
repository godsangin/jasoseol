package com.example.jasoseol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class BoardListAdapter extends BaseAdapter {
    List<CompanyBoard> boards;
    Context mContext;
    LayoutInflater mInflater;

    public BoardListAdapter(List<CompanyBoard> boards, Context context){
        this.boards = boards;
        mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return boards.size();
    }

    @Override
    public Object getItem(int position) {
        return boards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_item,parent,false);
        final CompanyBoard board = boards.get(position);
//        ImageView myImage = view.findViewById(R.id.myImage);
//        TextView title = view.findViewById(R.id.company_title);
//        TextView cPosition = view.findViewById(R.id.company_position);
//        TextView until = view.findViewById(R.id.until);
//        setImageView(myImage, board.getImgURL());
//        title.setText(board.getTitle());
//        cPosition.setText(board.getPosition());
//        until.setText(board.getUntil());
//
//        final ImageView favoriteBt = view.findViewById(R.id.favorite_bt);
//        favoriteBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(board.isFavorite()){
//                    favoriteBt.setColorFilter(Color.parseColor("#BEBEBE"), PorterDuff.Mode.SRC_IN);
//                    board.setFavorite(false);
//                }
//                else {
//                    favoriteBt.setColorFilter(Color.parseColor("#ffd700"), PorterDuff.Mode.SRC_IN);
//                    board.setFavorite(true);
//                }
//            }
//        });
//
//        return view;
        final ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.board_item, parent, false);
            holder = new ViewHolder();
            holder.title = convertView.findViewById(R.id.company_title);
            holder.myImage = convertView.findViewById(R.id.myImage);
            holder.cPosition = convertView.findViewById(R.id.company_position);
            holder.until = convertView.findViewById(R.id.until);
            holder.favorite = convertView.findViewById(R.id.favorite_bt);
            Log.d("holder.title==", holder.title + "");
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(board.getTitle());
//        setImageView(holder.myImage, board.getImgURL());
        ImageDownloader imageDownloader = new ImageDownloader();
        imageDownloader.download(board.getImgURL(), holder.myImage);
        holder.cPosition.setText(board.getPosition());
        holder.cPosition.setSelected(true);
        holder.until.setText(board.getUntil());
        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(board.isFavorite()){
                    holder.favorite.setColorFilter(Color.parseColor("#BEBEBE"), PorterDuff.Mode.SRC_IN);
                    board.setFavorite(false);
                }
                else {
                    holder.favorite.setColorFilter(Color.parseColor("#ffd700"), PorterDuff.Mode.SRC_IN);
                    board.setFavorite(true);
                }
            }
        });

        return convertView;

    }

    public void setImageView(ImageView imageView, final String imgURL){
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
            imageView.setImageBitmap(bitmap[0]);
        }catch (InterruptedException e){
            e.printStackTrace();
            Log.d("쓰레드 인터럽트==", "발생");
        }
    }

    private class ViewHolder{
        ImageView myImage;
        TextView title;
        TextView cPosition;
        TextView until;
        ImageView favorite;
    }
}
