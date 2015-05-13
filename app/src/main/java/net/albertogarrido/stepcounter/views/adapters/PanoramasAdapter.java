package net.albertogarrido.stepcounter.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import net.albertogarrido.stepcounter.R;
import net.albertogarrido.stepcounter.panoramio.model.Panoramio;

import java.util.List;

public class PanoramasAdapter extends RecyclerView.Adapter<PanoramasAdapter.ViewHolder> {

    private List<Panoramio> panoramas;
    private Context context;

    public PanoramasAdapter(List<Panoramio> panoramas) {
        this.panoramas = panoramas;
    }

    @Override
    public PanoramasAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater.from(this.context).inflate(R.layout.panorama_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder((LinearLayout) view);

        viewHolder.panoramaImage = (ImageView) view.findViewById(R.id.panorama_image);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (panoramas.get(position).getPhotoFileUrl() != null && !panoramas.get(position).getPhotoFileUrl().equals("")) {
            Picasso.with(this.context).load(panoramas.get(position).getPhotoFileUrl()).into(holder.panoramaImage);
        }
    }

    @Override
    public int getItemCount() {
        return panoramas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView panoramaImage;

        public ViewHolder(LinearLayout linearLayout) {
            super(linearLayout);
        }
    }
}