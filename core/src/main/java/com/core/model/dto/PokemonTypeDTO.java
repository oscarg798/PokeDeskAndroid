package com.core.model.dto;

/**
 * Created by Stephys on 13/07/16.
 */
public class PokemonTypeDTO {


    private final String name;

    private final String description;

    public PokemonTypeDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
