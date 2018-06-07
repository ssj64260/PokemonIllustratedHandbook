package com.android.pokemonillustratedhandbook.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class JoinPokemonToCharacteristic {

    @Id(autoincrement = true)
    private Long id;

    private String pokemonId;//pokemonID
    private String characteristicId;//特性ID

    @Generated(hash = 1329567122)
    public JoinPokemonToCharacteristic(Long id, String pokemonId,
                                       String characteristicId) {
        this.id = id;
        this.pokemonId = pokemonId;
        this.characteristicId = characteristicId;
    }

    @Generated(hash = 380757234)
    public JoinPokemonToCharacteristic() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPokemonId() {
        return this.pokemonId;
    }

    public void setPokemonId(String pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getCharacteristicId() {
        return this.characteristicId;
    }

    public void setCharacteristicId(String characteristicId) {
        this.characteristicId = characteristicId;
    }


}
