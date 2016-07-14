package com.core.model.dto;

import java.util.List;

/**
 * Created by Stephys on 13/07/16.
 */
public class PokemonDTO {


    private final String name;

    private final PokemonTypeDTO type;

    private final List<String> attacks;

    private final String image;

    public PokemonDTO(String name, PokemonTypeDTO type, List<String> attacks, String image) {
        this.name = name;
        this.type = type;
        this.attacks = attacks;
        this.image = image;
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
}
