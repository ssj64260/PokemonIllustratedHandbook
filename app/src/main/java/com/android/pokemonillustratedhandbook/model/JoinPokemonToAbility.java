package com.android.pokemonillustratedhandbook.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class JoinPokemonToAbility {

    @Id(autoincrement = true)
    private Long id;

    private String pokemonId;//pokemonID
    private String abilityId;//特性ID

    @Generated(hash = 1741951367)
    public JoinPokemonToAbility(Long id, String pokemonId, String abilityId) {
        this.id = id;
        this.pokemonId = pokemonId;
        this.abilityId = abilityId;
    }

    @Generated(hash = 1470971438)
    public JoinPokemonToAbility() {
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

    public String getAbilityId() {
        return this.abilityId;
    }

    public void setAbilityId(String abilityId) {
        this.abilityId = abilityId;
    }


}
