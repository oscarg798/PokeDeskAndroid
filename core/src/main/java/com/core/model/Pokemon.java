package com.core.model;

import android.content.Context;
import android.util.Log;

import com.core.model.builders.PokemonDTOBuilder;
import com.core.model.builders.PokemonTypeDTOBuilder;
import com.core.model.dto.PokemonDTO;
import com.core.model.dto.PokemonTypeDTO;
import com.core.model.interfaces.PokemonCallbacks;
import com.core.model.utils.CoupleParams;
import com.core.services.HTTPServices;
import com.core.services.interfaces.IHTTPServices;
import com.core.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oscargallon on 7/13/16.
 */

public class Pokemon {

    private final static Pokemon instance = new Pokemon();

    private final String SKIP_KEY = "skip";

    private final String PAGINATION_KEY = "pagination";


    private Pokemon() {
    }

    public static Pokemon getInstance() {
        return instance;
    }

    private List<PokemonDTO> getPokemonDTOListFromJsonArray(JSONArray jsonArray) {
        List<PokemonDTO> pokemonDTOList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                pokemonDTOList.add(getPokemonDTOFromJsonObject(jsonArray.getJSONObject(i)));

            } catch (JSONException e) {
                e.printStackTrace();
                pokemonDTOList = null;
                break;
            }

        }

        return pokemonDTOList;
    }


    private PokemonDTO getPokemonDTOFromJsonObject(JSONObject jsonObject) {
        PokemonDTO pokemon = null;

        PokemonDTOBuilder pokemonBuilder = new PokemonDTOBuilder();

        try {
            pokemonBuilder
                    .withAName(jsonObject.getString("name"));

            pokemonBuilder
                    .withAPokemonTypeDTO(new PokemonTypeDTOBuilder()
                            .withAName(jsonObject.getString("type"))
                            .createPokemonTypeDTO());

            String att = jsonObject.getString("attacks");

            att = att.substring(1, att.length() - 1);

            pokemonBuilder.withAnAttacks(Arrays.asList(att.split(",")));

            if (Utils.jsonHasProperty(jsonObject.names(), "image")) {
                pokemonBuilder.withAnImage(jsonObject.getString("image"));
            }

            if (Utils.jsonHasProperty(jsonObject.names(), "height")) {
                pokemonBuilder.withAHeight(jsonObject.getString("height"));
            }

            if (Utils.jsonHasProperty(jsonObject.names(), "weight")) {
                pokemonBuilder.withAWeight(jsonObject.getString("weight"));
            }

            pokemon = pokemonBuilder.createPokemonDTO();

        } catch (JSONException e) {
            e.printStackTrace();
            pokemon = null;
            pokemonBuilder = null;
        }

        return pokemon;
    }

    private void pokemonsGetted(JSONArray pokemons,
                                PokemonCallbacks.GetPokemonsCallback getPokemonsCallback) {

        getPokemonsCallback.onPokemonsGot(getPokemonDTOListFromJsonArray(pokemons));

    }

    private void errorGettingPokemons(String error,
                                      PokemonCallbacks.GetPokemonsCallback getPokemonsCallback) {

        getPokemonsCallback.onErrorGettingPokemons(error);

    }

    private void validateGetPokemonsAnswer(String response,
                                           PokemonCallbacks.GetPokemonsCallback getPokemonsCallback) {

        try {
            JSONObject jsonObject = new JSONObject(response);

            if (!Utils.jsonHasProperty(jsonObject.names(), Utils.STATUS_KEY)) {
                getPokemonsCallback.onErrorGettingPokemons("Something went wrong");
                return;

            }

            if (jsonObject.getString(Utils.STATUS_KEY).equals(Utils.FAILED_KEY)) {

                String error = Utils.jsonHasProperty(jsonObject.names(), Utils.MESSAGE_KEY) ?
                        jsonObject.getString(Utils.MESSAGE_KEY) : "Something went wrong";

                getPokemonsCallback.onErrorGettingPokemons(error);
                return;
            }

            JSONArray jsonArray = jsonObject.getJSONArray("pokemon");

            pokemonsGetted(jsonArray, getPokemonsCallback);


        } catch (JSONException e) {
            e.printStackTrace();
            getPokemonsCallback.onErrorGettingPokemons(e.getMessage());
        }

    }

    private IHTTPServices getPokemonsResponse = new IHTTPServices() {
        @Override
        public void successFullResponse(String response)
        {
            Log.i("ok", response);
        }

        @Override
        public void errorResponse(String message) {
            Log.i("ok", message);
        }
    };


    public void getPokemonsNearby(String id,  String token, double lat, double lng, String url) {
        List<CoupleParams> coupleParamsList = new ArrayList<>();
        coupleParamsList.add(new CoupleParams
                .CoupleParamBuilder("id")
                .nestedParam(id)
                .createCoupleParam());

        coupleParamsList.add(new CoupleParams
                .CoupleParamBuilder("token")
                .nestedParam(token)
                .createCoupleParam());
        coupleParamsList.add(new CoupleParams
                .CoupleParamBuilder("lng")
                .nestedParam(Double.toString(lng))
                .createCoupleParam());
        coupleParamsList.add(new CoupleParams
                .CoupleParamBuilder("lat")
                .nestedParam(Double.toString(lat))
                .createCoupleParam());

        HTTPServices httpServices = new HTTPServices(getPokemonsResponse,
                coupleParamsList, "POST", false);

        httpServices.execute(url);
    }

    public void getPokemonsNearby(String id, String username, String password, double lat, double lng) {

        List<CoupleParams> coupleParamsList = new ArrayList<>();
        coupleParamsList.add(new CoupleParams
                .CoupleParamBuilder("id")
                .nestedParam(id)
                .createCoupleParam());
        coupleParamsList.add(new CoupleParams
                .CoupleParamBuilder("username")
                .nestedParam(username)
                .createCoupleParam());
        coupleParamsList.add(new CoupleParams
                .CoupleParamBuilder("password")
                .nestedParam(password)
                .createCoupleParam());
        coupleParamsList.add(new CoupleParams
                .CoupleParamBuilder("lng")
                .nestedParam(Double.toString(lng))
                .createCoupleParam());
        coupleParamsList.add(new CoupleParams
                .CoupleParamBuilder("lat")
                .nestedParam(Double.toString(lat))
                .createCoupleParam());

        HTTPServices httpServices = new HTTPServices(getPokemonsResponse,
                coupleParamsList, "POST", false);

        httpServices.execute("http://159.203.236.220:1337/pokemon/getNearbyPokemons");
    }


    public void getPokemons(int pagination, int limit, String url,
                            final PokemonCallbacks.GetPokemonsCallback getPokemonsCallback) {
        List<CoupleParams> coupleParamsList = new ArrayList<>();

        coupleParamsList.add(new CoupleParams.CoupleParamBuilder(PAGINATION_KEY)
                .nestedParam(Integer.toString(pagination)).createCoupleParam());

        coupleParamsList.add(new CoupleParams.CoupleParamBuilder(SKIP_KEY)
                .nestedParam(Integer.toString(limit)).createCoupleParam());


        HTTPServices httpServices = new HTTPServices(new IHTTPServices() {
            @Override
            public void successFullResponse(String response) {
                validateGetPokemonsAnswer(response, getPokemonsCallback);
            }

            @Override
            public void errorResponse(String message) {
                errorGettingPokemons(message, getPokemonsCallback);
            }
        }, coupleParamsList, Utils.POST_SERVICE_TYPE, false);

        httpServices.execute(url);

    }

}
