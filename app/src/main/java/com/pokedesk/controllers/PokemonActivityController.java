package com.pokedesk.controllers;

import android.app.Activity;
import android.util.Log;

import com.core.model.Pokemon;
import com.core.model.dto.PokemonDTO;
import com.core.model.interfaces.PokemonCallbacks;
import com.pokedesk.controllers.abstracts.AbstractController;
import com.pokedesk.presentation.activities.PokemonActivity;

import java.util.List;

/**
 * Created by Stephys on 13/07/16.
 */
public class PokemonActivityController extends AbstractController {

    private int page = 0;

    private int limit = 10;

    public PokemonActivityController(Activity activity) {
        super(activity);
    }

    public void getPokemons() {
        page += 1;
        Pokemon.getInstance().getPokemons(page, limit, "http://159.203.236.220:1337",
                new PokemonCallbacks.GetPokemonsCallback() {
                    @Override
                    public void onPokemonsGot(List<PokemonDTO> pokemonDTOList) {
                        if (pokemonDTOList != null) {
                            ((PokemonActivity) getActivity()).showPokemons(pokemonDTOList);

                        } else {
                            cancelIsLoading();
                            noMorePokemons();
                        }
                    }

                    @Override
                    public void onErrorGettingPokemons(String err) {
                        Log.e("ERROR " + this.getClass().getName(), err);
                        cancelIsLoading();
                        noMorePokemons();
                    }
                });
    }

    private void cancelIsLoading() {
        ((PokemonActivity) getActivity()).setLoadingPokemons(false);
    }

    private void noMorePokemons() {
        ((PokemonActivity) getActivity()).setHasMorePokemons(false);
    }


}
