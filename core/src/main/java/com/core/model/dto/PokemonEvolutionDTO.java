package com.core.model.dto;

import java.io.Serializable;

/**
 * Created by oscargallon on 7/21/16.
 */

public class PokemonEvolutionDTO implements Serializable{

        private final String name;

        private final String number;


        public PokemonEvolutionDTO(String name, String number) {
            this.name = name;
            this.number = number;
        }


        public String getName() {
            return name;
        }

        public String getNumber() {
            return number;
        }
}
