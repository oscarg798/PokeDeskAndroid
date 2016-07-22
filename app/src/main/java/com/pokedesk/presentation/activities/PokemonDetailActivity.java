package com.pokedesk.presentation.activities;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.core.model.dto.PokemonDTO;
import com.pokedesk.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class PokemonDetailActivity extends AppCompatActivity {

    private PokemonDTO pokemonDTO;

    private ImageView ivImage;

    private TextView tvAbilities;

    private TextView tvWeight;

    private TextView tvHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_detail);

        if (getIntent() != null) {
            pokemonDTO = (PokemonDTO) getIntent()
                    .getSerializableExtra(getString(R.string.pokemon_key));
        }

        initViewComponents();
    }

    private void initViewComponents() {
        ivImage = (ImageView) findViewById(R.id.iv_image);

        tvAbilities = (TextView) findViewById(R.id.tv_abilities);

        tvWeight = (TextView) findViewById(R.id.tv_weight);

        tvHeight = (TextView) findViewById(R.id.tv_height);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(pokemonDTO.getName());
        }

        if (pokemonDTO.getHeight() != null) {
            tvHeight.setText(pokemonDTO.getHeight());
        }

        if (pokemonDTO.getWeight() != null) {
            tvWeight.setText(pokemonDTO.getWeight());
        }

        String type = pokemonDTO
                .getType().getName();
        type = Character.toString(type.charAt(0)).toUpperCase() + type.substring(1, type.length());

        tvAbilities.setText(type);

        Picasso.with(getApplicationContext()).load(pokemonDTO.getImage())
                .into(ivImage);


    }

}
