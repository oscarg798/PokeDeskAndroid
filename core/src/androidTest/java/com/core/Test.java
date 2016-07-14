package com.core;

import android.test.suitebuilder.annotation.SmallTest;

import com.core.model.builders.PokemonDTOBuilder;
import com.core.model.dto.PokemonDTO;
import com.core.model.dto.PokemonTypeDTO;

import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Stephys on 13/07/16.
 */

public class Test {

    @SmallTest
    public void shouldCreateAPokemonDTO(){
        List<String> attacks =  new ArrayList<>();
        attacks.add("Thunder");
        PokemonTypeDTO pokemonTypeDTO =  new PokemonTypeDTO("Electric", null);
        PokemonDTO pokemonDTO = new PokemonDTO("Pika", pokemonTypeDTO, attacks);

        PokemonDTO pokemonDTO1 = new PokemonDTOBuilder()
                .withAName("Pika")
                .withAnAttacks(attacks)
                .withAPokemonTypeDTO(pokemonTypeDTO)
                .createPokemonDTO();

        assertEquals(pokemonDTO, pokemonDTO1);

    }

}
