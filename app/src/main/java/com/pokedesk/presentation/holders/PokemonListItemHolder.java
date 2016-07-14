package com.pokedesk.presentation.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pokedesk.R;

/**
 * Created by Stephys on 13/07/16.
 */
public class PokemonListItemHolder extends RecyclerView.ViewHolder {

    private final TextView tvName;
    private final TextView tvType;
    private final ImageView ivImage;

    public PokemonListItemHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvType = (TextView) itemView.findViewById(R.id.tv_type);
        ivImage = (ImageView) itemView.findViewById(R.id.iv_image);
    }

    public TextView getTvName() {
        return tvName;
    }

    public ImageView getIvImage() {
        return ivImage;
    }

    public TextView getTvType() {
        return tvType;
    }
}
