package com.core.model.builders;

import com.core.model.dto.PokemonDTO;
import com.core.model.dto.PokemonEvolutionDTO;
import com.core.model.dto.PokemonTypeDTO;

import java.util.List;

/**
 * Created by Stephys on 13/07/16.
 */
public class PokemonDTOBuilder {

    private String aName;

    private PokemonTypeDTO aPokemonTypeDTO;

    private List<String> anAttacks;

    private  String aHeight;

    private  String aWeight;

    private String anImage;

    private  String aNumber;

    private  List<String> aWeaknessesList;

    private  List<PokemonEvolutionDTO> aPokemonEvolutionDTOList;


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

    public PokemonDTOBuilder withAHeight(String aHeight){
        this.aHeight = aHeight;
        return this;
    }

    public PokemonDTOBuilder withAWeight(String aWeight){
        this.aWeight = aWeight;
        return this;
    }

    public PokemonDTOBuilder withANumber(String aNumber){
        this.aNumber = aNumber;
        return this;
    }

    public PokemonDTOBuilder withAWeaknessesList(List<String> aWeaknessesList){
        this.aWeaknessesList = aWeaknessesList;
        return  this;
    }

    public PokemonDTOBuilder withAPokemonEvolutionDTOList(List<PokemonEvolutionDTO> aPokemonEvolutionDTOList){
        this.aPokemonEvolutionDTOList = aPokemonEvolutionDTOList;
        return  this;
    }



    public PokemonDTO createPokemonDTO(){
        return new PokemonDTO(this.aName, this.aPokemonTypeDTO, this.anAttacks,
                this.anImage, this.aNumber, this.aHeight, this.aWeight,
                this.aWeaknessesList, this.aPokemonEvolutionDTOList);
    }
}
