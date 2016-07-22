package com.core.model.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Stephys on 13/07/16.
 */
public class PokemonDTO implements Serializable{


    private final String name;

    private final PokemonTypeDTO type;

    private final List<String> attacks;

    private final String image;

    private final String number;

    private final String height;

    private final String weight;

    private final List<String> weaknesses;

    private final List<PokemonEvolutionDTO> pokemonEvolutionDTO;

    public PokemonDTO(String name, PokemonTypeDTO type, List<String> attacks, String image,
                      String number, String height, String weight, List<String> weaknesses,
                      List<PokemonEvolutionDTO> pokemonEvolutionDTO) {
        this.name = name;
        this.type = type;
        this.attacks = attacks;
        this.image = image;
        this.number = number;
        this.height = height;
        this.weight = weight;
        this.weaknesses = weaknesses;
        this.pokemonEvolutionDTO = pokemonEvolutionDTO;
    }

    public String getName() {
        return name;
    }

    public PokemonTypeDTO getType() {
        return type;
    }

    public List<String> getAttacks() {
        return attacks;
    }

    public String getImage() {
        return image;
    }

    public String getNumber() {
        return number;
    }

    public List<PokemonEvolutionDTO> getPokemonEvolutionDTO() {
        return pokemonEvolutionDTO;
    }

    public List<String> getWeaknesses() {
        return weaknesses;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }
}
