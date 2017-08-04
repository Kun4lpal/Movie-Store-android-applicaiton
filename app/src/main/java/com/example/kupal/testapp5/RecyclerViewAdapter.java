package com.example.kupal.testapp5;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kupal on 2/12/2017.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    //<-------------------------private data members------------------------------------->

    private OnItemClickListener mItemClickListener;
    private List<Map<String, ?>> mData;
    private Context context;
    private int lastposition = -1;
    public boolean check = false;
    private PopupMenu popupMenu;

    public void setOnItemClickListener(final OnItemClickListener mItemClickLisn){
        mItemClickListener = mItemClickLisn;
    }

    //<-------------------------Constructor------------------------------------->

    public RecyclerViewAdapter(Context myContext, List<Map<String, ?>> moviesData){
        mData = moviesData;
        context = myContext;
    }

    //<-------------------------Here the view is generated on creation------------------------------------->

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newView;
        switch (viewType) {
            case 1:
                newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view, parent, false);
                break;
            case 2:
                newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view_1, parent, false);
                break;
            case 3:
                newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view_2, parent, false);
                break;
            default:
                newView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view, parent, false);
                break;
        }
        return new ViewHolder(newView);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    //<-----------------This method binds the current movie data to corresponding views-------------------------->

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position<30 && position>-1) {
            Map<String, ?> movie = mData.get(position);
            holder.bindMovieData(movie);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    //<------------------------------change the cardview layout based on item position------------------------------>

    @Override
    public int getItemViewType(int position) {
        while (check) {
            if (position < 4) {
                return 2;
            } else if (position > mData.size() - 6) {
                return 1;
            } else
                return 3;
        }
        return 2;
    }

    //<-------------------------ViewHolder class for items in RecyclerView----------------------------------->

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView movieImage;
        public TextView movieName;
        public TextView movieDesc;
        public CheckBox isSelected;
        public TextView rating;
        public ImageView overflow;

        public ViewHolder(View itemView) {
            super(itemView);
            movieImage=(ImageView)itemView.findViewById(R.id.movieImage);
            movieImage.setOnClickListener(this);
            itemView.setOnClickListener(this);

            movieName=(TextView)itemView.findViewById(R.id.movieN);
            movieDesc= (TextView) itemView.findViewById(R.id.movieD);
            isSelected = (CheckBox) itemView.findViewById(R.id.checkB);
            rating = (TextView) itemView.findViewById(R.id.rat);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);

            if(overflow!=null){
                overflow.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(mItemClickListener!=null)
                            mItemClickListener.onOverFlowButtonClick(v,getPosition());
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemLongClick(v, getAdapterPosition());
                    }
                    return true;
                }
            });
        }

        //<-------------------------Task1 popUp menu on item click------------------------------------->

        @Override
        public void onClick(View v) {
            if(v.getId() == movieImage.getId()){
                popupMenu = new PopupMenu(v.getContext(), v);
                createMenu(popupMenu.getMenu());
                popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        popupMenu = null;
                    }
                });
                popupMenu.show();
            }else{
                if (mItemClickListener != null) {
                    mItemClickListener.onItemLongClick(v, getAdapterPosition());
                }
            }
        }


        private void createMenu(Menu menu) {
            menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(movieImage.getContext(), "Deleted!" , Toast.LENGTH_SHORT).show();
                            removeAt(getPosition());
                            notifyItemRemoved(getPosition());
                            notifyDataSetChanged();
                            return true;
                        }
                    });

            menu.add("Copy")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(movieImage.getContext(), "Copied!" , Toast.LENGTH_SHORT).show();
                            mData.add(getPosition() + 1, (HashMap<String, ?>) mData.get(getPosition()));
                            notifyDataSetChanged();
                            return true;
                        }
                    });
        }

        //<-------------------------Helper function for delete------------------------------------->

        private void removeAt(int position) {
            mData.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mData.size());
        }


        public void bindMovieData(Map<String,?> movie){
            movieName.setText((String) movie.get("name"));
            movieImage.setImageResource((Integer) movie.get("image"));
            movieDesc.setText((String) movie.get("description"));
            isSelected.setChecked((Boolean) movie.get("selection"));
            rating.setText( movie.get("rating")+"/10");

        }
    }

    //<-------------------------Interface for on click------------------------------------->

    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
        public void onOverFlowButtonClick(View view, int position);
    }
}
//<------------------------------------------End--------------------------------------------------->