package com.core.model.builders;

import com.core.model.dto.PokemonTypeDTO;

/**
 * Created by Stephys on 13/07/16.
 */
public class PokemonTypeDTOBuilder {

    private String aName;

    private String aDescription;

    public PokemonTypeDTOBuilder withAName(String aName) {
        this.aName = aName;
        return this;
    }

    public PokemonTypeDTOBuilder withADescription(String aDescription) {
        this.aDescription = aDescription;
        return this;
    }

    public PokemonTypeDTO createPokemonTypeDTO() {
        return new PokemonTypeDTO(this.aName, this.aDescription);
    }

}
