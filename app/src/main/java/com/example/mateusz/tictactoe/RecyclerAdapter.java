package com.example.mateusz.tictactoe;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {

    private List<Save> savesList;
    private Context mContext;



    public RecyclerAdapter(Context context, List<Save> savesList)
    {

        this.savesList = savesList;
        mContext = context;

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_komponent, viewGroup,false);
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);
        return imageViewHolder;
    }


    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int i)
    {

        final DBHelper dbHelper=DBHelper.getInstance(mContext);
        final Save save = savesList.get(i);

        holder.textId.setText(Integer.toString(i)+". ");
        holder.textData.setText(save.getData());
        holder.delete.setImageResource(R.drawable.ic_delete_forever_black_24dp);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,DetailsActivity.class);

                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                save.getScreenshoot().compress(Bitmap.CompressFormat.PNG, 100, bStream);
                byte[] byteArray = bStream.toByteArray();

                intent.putExtra("Image", byteArray);
                intent.putExtra("Data", save.getData());
                intent.putExtra("Address", save.getAddress());
                intent.putExtra("Winner", save.getWinner());
                intent.putExtra("GameTime", save.getGameTime());
                intent.putExtra("Computer", save.getComputer());

                mContext.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savesList.remove(i);
                dbHelper.deleteOne(save);
                notifyDataSetChanged();

            }
        });


    }

    @Override
    public int getItemCount() {
        return savesList.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder
    {

        TextView textId;
        TextView textData;
        ImageView delete;




        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            textId = itemView.findViewById(R.id.textId);
            textData= itemView.findViewById(R.id.textDataDetails);
            delete = (ImageView) itemView.findViewById(R.id.imageDelete);
        }



    }
}

