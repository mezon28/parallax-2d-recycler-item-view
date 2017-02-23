package motaro222.miy.parallax2drecycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Simple Recycler Adapter providing image and text shown on {@link Parallax2dView}
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
    private static final int[] IMG_LIST = new int[]{R.drawable.m1, R.drawable.m2, R.drawable.m3, R.drawable.m4, R.drawable.m5, R.drawable.m6};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View recyclerItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_view, parent, false);
        return new ViewHolder(recyclerItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView1.setText("Position: " + position);
        holder.imageView.setImageResource(IMG_LIST[position % IMG_LIST.length]);
    }

    @Override
    public int getItemCount() {
        return IMG_LIST.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView1;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);

            textView1 = (TextView) itemView.findViewById(R.id.text_view_1);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
