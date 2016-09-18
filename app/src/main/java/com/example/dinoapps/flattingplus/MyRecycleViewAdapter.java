package com.example.dinoapps.flattingplus;

        import android.support.v7.widget.RecyclerView;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.TextView;

        import java.util.ArrayList;

        import static com.google.android.gms.wearable.DataMap.TAG;

public class MyRecycleViewAdapter extends RecyclerView
        .Adapter<MyRecycleViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecycleViewAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView title;
        TextView content;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);


            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecycleViewAdapter(ArrayList<DataObject> myDataset) {
        Log.v(TAG, "Dataset reset!!");
        mDataset = myDataset;
    }

    public ArrayList<DataObject> getDataset()
    {
        return mDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemlayout, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.title.setText(mDataset.get(position).getTitle());//title
        holder.content.setText(mDataset.get(position).getContent());//content
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index, String comingFrom) {
        Log.i(LOG_TAG, "Deleting item " + mDataset.get(index).getTitle() + " " + mDataset.get(index).getContent());

        mDataset.remove(index);
        notifyItemRemoved(index);

        if(comingFrom.equals("Notes")) {
            MainActivity.dbHelper.deleteNote(mDataset.get(index).getTime());
        }
        else if(comingFrom.equals("Shopping"))
        {
            MainActivity.dbHelper.deleteShoppingItem(mDataset.get(index).getTime());
        }
        else if(comingFrom.equals("Money"))
        {
            MainActivity.dbHelper.deleteMoneyItem(mDataset.get(index).getTime());
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
