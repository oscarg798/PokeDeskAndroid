package com.pokedesk.presentation.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.core.model.builders.PokemonTypeDTOBuilder;
import com.core.model.dto.PokemonDTO;
import com.pokedesk.R;
import com.pokedesk.presentation.holders.PokemonListItemHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by oscargallon on 7/13/16.
 */

public class PokemonListAdapter extends RecyclerView.Adapter<PokemonListItemHolder> {

    private List<PokemonDTO> pokemonDTOList;

    private Context context;



    public PokemonListAdapter(List<PokemonDTO> pokemonDTOList, Context context) {
        this.pokemonDTOList = pokemonDTOList;
        this.context = context;
    }

    @Override
    public PokemonListItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pokemon_list_item, parent, false);
        return new PokemonListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PokemonListItemHolder holder, int position) {
        PokemonDTO pokemonDTO = pokemonDTOList.get(position);
        holder.getTvName().setText(pokemonDTO.getName());
        holder.getTvType().setText(pokemonDTO.getType().getName());
        if(pokemonDTO.getImage()!=null){
            Picasso.with(context).load(pokemonDTO.getImage()).into(holder.getIvImage());
        }
    }

    @Override
    public int getItemCount() {
        return this.pokemonDTOList.size();
    }

    public List<PokemonDTO> getPokemonDTOList() {
        return pokemonDTOList;
    }
}
