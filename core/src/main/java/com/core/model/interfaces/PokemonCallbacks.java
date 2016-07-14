package com.core.model.interfaces;

import com.core.model.dto.PokemonDTO;

import java.util.List;

/**
 * Created by oscargallon on 7/13/16.
 */

public class PokemonCallbacks {

    public interface GetPokemonsCallback {
        void onPokemonsGot(List<PokemonDTO> pokemonDTOList);

        void onErrorGettingPokemons(String err);
    }
}
