package com.core.model.builders;

import com.core.model.dto.PokemonDTO;
import com.core.model.dto.PokemonTypeDTO;

import java.util.List;

/**
 * Created by Stephys on 13/07/16.
 */
public class PokemonDTOBuilder {

    private String aName;

    private PokemonTypeDTO aPokemonTypeDTO;

    private List<String> anAttacks;

    private String anImage;


    public PokemonDTOBuilder() {

    }

    public PokemonDTOBuilder withAName(String aName){
        this.aName = aName;
        return this;
    }

    public PokemonDTOBuilder withAPokemonTypeDTO(PokemonTypeDTO aPokemonTypeDTO){
        this.aPokemonTypeDTO = aPokemonTypeDTO;
        return this;
    }

    public PokemonDTOBuilder withAnAttacks(List<String> Attacks){
        this.anAttacks = Attacks;
        return  this;
    }

    public PokemonDTOBuilder withAnImage(String anImage){
        this.anImage = anImage;
        return this;
    }

    public PokemonDTO createPokemonDTO(){
        return new PokemonDTO(this.aName, this.aPokemonTypeDTO, this.anAttacks, this.anImage);
    }
}
