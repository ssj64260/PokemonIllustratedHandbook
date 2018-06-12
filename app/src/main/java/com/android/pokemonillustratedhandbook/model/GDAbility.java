package com.android.pokemonillustratedhandbook.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 特性
 */
@Entity(nameInDb = "GD_Ability")
public class GDAbility {

    @Id
    private String id;//特性ID
    private String name;//特性名称
    private String jp_name;//特性日文名
    private String en_name;//特性英文名
    private String description;//特性描述

    @ToMany
    @JoinEntity(
            entity = JoinPokemonToAbility.class,
            sourceProperty = "abilityId",
            targetProperty = "pokemonId"
    )
    @Transient
    private List<GDPokemon> pokemonList;

    @Generated(hash = 864043029)
    public GDAbility(String id, String name, String jp_name, String en_name,
            String description) {
        this.id = id;
        this.name = name;
        this.jp_name = jp_name;
        this.en_name = en_name;
        this.description = description;
    }

    @Generated(hash = 1366567443)
    public GDAbility() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJp_name() {
        return this.jp_name;
    }

    public void setJp_name(String jp_name) {
        this.jp_name = jp_name;
    }

    public String getEn_name() {
        return this.en_name;
    }

    public void setEn_name(String en_name) {
        this.en_name = en_name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
