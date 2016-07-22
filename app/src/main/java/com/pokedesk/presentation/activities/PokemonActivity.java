package com.pokedesk.presentation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.core.model.dto.PokemonDTO;
import com.core.model.interfaces.PokemonCallbacks;
import com.pokedesk.R;
import com.pokedesk.controllers.PokemonActivityController;
import com.pokedesk.presentation.adapters.PokemonListAdapter;
import com.pokedesk.presentation.listerner.RecyclerItemOnClickListener;
import com.pokedesk.presentation.listerner.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class PokemonActivity extends AppCompatActivity {

    private PokemonActivityController pokemonActivityController;

    private RecyclerView rvPokemons;

    private PokemonListAdapter pokemonListAdapter;

    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private boolean isLoadingPokemons;

    private boolean hasMorePokemons = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewComponents();
        intiComponents();
    }

    private void initViewComponents() {
        rvPokemons = (RecyclerView) findViewById(R.id.rv_pokemons);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rvPokemons.setLayoutManager(mLayoutManager);

        rvPokemons.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();


                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        Log.v("LOAD", "Last Item Wow !");
                        if (!isLoadingPokemons && hasMorePokemons) {
                            isLoadingPokemons = true;
                            pokemonActivityController.getPokemons();
                        }
                    }

                }
            }
        });
    }

    private void intiComponents() {
        pokemonActivityController = new PokemonActivityController(this);
        pokemonActivityController.getPokemons();
    }

    public void showPokemons(final List<PokemonDTO> pokemonDTOList) {
        if (pokemonListAdapter == null) {
            pokemonListAdapter = new PokemonListAdapter(new ArrayList<PokemonDTO>(), getApplicationContext());
            rvPokemons.setAdapter(pokemonListAdapter);
        }
        isLoadingPokemons = false;

        pokemonListAdapter.getPokemonDTOList().addAll(pokemonDTOList);
        pokemonListAdapter.notifyDataSetChanged();

        rvPokemons.addOnItemTouchListener(new RecyclerItemOnClickListener(getApplicationContext(),
                new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Intent intent = new Intent(PokemonActivity.this,
                                PokemonDetailActivity.class);
                        intent.putExtra(getString(R.string.pokemon_key),
                                pokemonListAdapter.getPokemonDTOList().get(position));
                        startActivity(intent);

                    }
                }));
    }

    public void setLoadingPokemons(boolean loadingPokemons) {
        isLoadingPokemons = loadingPokemons;
    }

    public void setHasMorePokemons(boolean hasMorePokemons) {
        this.hasMorePokemons = hasMorePokemons;
    }
}
