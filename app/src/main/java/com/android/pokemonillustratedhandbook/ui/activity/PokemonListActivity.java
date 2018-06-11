package com.android.pokemonillustratedhandbook.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.pokemonillustratedhandbook.R;
import com.android.pokemonillustratedhandbook.app.BaseActivity;
import com.android.pokemonillustratedhandbook.model.DaoSession;
import com.android.pokemonillustratedhandbook.model.GDCharacteristic;
import com.android.pokemonillustratedhandbook.model.GDCharacteristicDao;
import com.android.pokemonillustratedhandbook.model.GDPokemon;
import com.android.pokemonillustratedhandbook.model.GDPokemonDao;
import com.android.pokemonillustratedhandbook.model.GDPokemonName;
import com.android.pokemonillustratedhandbook.model.GDPokemonNameDao;
import com.android.pokemonillustratedhandbook.model.GDProperty;
import com.android.pokemonillustratedhandbook.model.GDPropertyDao;
import com.android.pokemonillustratedhandbook.model.JoinPokemonToCharacteristic;
import com.android.pokemonillustratedhandbook.model.JoinPokemonToCharacteristicDao;
import com.android.pokemonillustratedhandbook.model.JoinPokemonToProperty;
import com.android.pokemonillustratedhandbook.model.JoinPokemonToPropertyDao;
import com.android.pokemonillustratedhandbook.ui.adapter.GDPokemonListAdapter;
import com.android.pokemonillustratedhandbook.ui.adapter.OnListClickListener;
import com.android.pokemonillustratedhandbook.utils.AssetsUtil;
import com.android.pokemonillustratedhandbook.utils.FastClick;
import com.android.pokemonillustratedhandbook.utils.GreenDaoHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class PokemonListActivity extends BaseActivity {

    private RecyclerView rvPokemonList;

    private List<GDPokemon> mPokemonList;
    private GDPokemonListAdapter mPokemonAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_pokemon_list;
    }

    @Override
    public void onBackPressed() {
        if (!FastClick.isExitClick()) {
            showSnackbar("再次点击退出程序");
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void initData() {
        super.initData();

        mPokemonList = new ArrayList<>();
        mPokemonAdapter = new GDPokemonListAdapter(this, mPokemonList);
        mPokemonAdapter.setListClick(mListClick);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarTitle("Pokemon List");

        rvPokemonList = findViewById(R.id.rv_pokemon_list);
        rvPokemonList.setLayoutManager(new LinearLayoutManager(this));
        rvPokemonList.setAdapter(mPokemonAdapter);

        getPokemonList();
    }

    private void initPokemonDB(final DaoSession daoSession) {
        final Gson gson = new GsonBuilder().create();
        final String pokemonJson = AssetsUtil.getAssetsTxtByName(this, "all.json");
        final String nameJson = AssetsUtil.getAssetsTxtByName(this, "pokemon_name.json");
        final String propertyJson = AssetsUtil.getAssetsTxtByName(this, "property.json");
        final String characteristicJson = AssetsUtil.getAssetsTxtByName(this, "characteristic.json");

        final List<GDPokemon> pokemonList = gson.fromJson(pokemonJson, new TypeToken<List<GDPokemon>>() {
        }.getType());
        final List<GDPokemonName> nameList = gson.fromJson(nameJson, new TypeToken<List<GDPokemonName>>() {
        }.getType());
        final List<GDProperty> propertyList = gson.fromJson(propertyJson, new TypeToken<List<GDProperty>>() {
        }.getType());
        final List<GDCharacteristic> characteristicList = gson.fromJson(characteristicJson, new TypeToken<List<GDCharacteristic>>() {
        }.getType());
        final List<JoinPokemonToProperty> joinPTPList = new ArrayList<>();
        final List<JoinPokemonToCharacteristic> joinPTCList = new ArrayList<>();

        if (pokemonList == null || pokemonList.isEmpty()) {
            return;
        }

        final GDPokemonDao pokemonDao = daoSession.getGDPokemonDao();
        final GDPokemonNameDao nameDao = daoSession.getGDPokemonNameDao();
        final GDPropertyDao propertyDao = daoSession.getGDPropertyDao();
        final GDCharacteristicDao characteristicDao = daoSession.getGDCharacteristicDao();
        final JoinPokemonToPropertyDao joinPTPDao = daoSession.getJoinPokemonToPropertyDao();
        final JoinPokemonToCharacteristicDao joinPTCDao = daoSession.getJoinPokemonToCharacteristicDao();

        for (GDPokemon pokemon : pokemonList) {
            final String id = pokemon.getId();
            final List<GDProperty> pList = pokemon.getProperty();
            final List<GDCharacteristic> cList = pokemon.getCharacteristic();

            if (pList != null && !pList.isEmpty()) {
                for (GDProperty property : pList) {
                    final String pid = property.getId();
                    final JoinPokemonToProperty ptp = new JoinPokemonToProperty();
                    ptp.setPokemonId(id);
                    ptp.setPropertyId(pid);
                    joinPTPList.add(ptp);
                }
            }

            if (cList != null && !cList.isEmpty()) {
                for (GDCharacteristic characteristic : cList) {
                    final String cid = characteristic.getId();
                    final JoinPokemonToCharacteristic ptc = new JoinPokemonToCharacteristic();
                    ptc.setPokemonId(id);
                    ptc.setCharacteristicId(cid);
                    joinPTCList.add(ptc);
                }
            }
        }

        joinPTPDao.insertInTx(joinPTPList);
        joinPTCDao.insertInTx(joinPTCList);
        pokemonDao.insertInTx(pokemonList);
        nameDao.insertInTx(nameList);
        propertyDao.insertInTx(propertyList);
        characteristicDao.insertInTx(characteristicList);
    }

    private void getPokemonList() {
        final DaoSession daoSession = new GreenDaoHelper().getDaoSession();
        final GDPokemonDao pokemonDao = daoSession.getGDPokemonDao();

        final QueryBuilder<GDPokemon> qb = pokemonDao.queryBuilder();
        qb.orderAsc(GDPokemonDao.Properties.Id);
        Query<GDPokemon> query = qb.build();

        List<GDPokemon> pokemonList = query.list();
        if (pokemonList == null || pokemonList.isEmpty()) {
            initPokemonDB(daoSession);
            pokemonList = query.list();
        }

        if (pokemonList != null && !pokemonList.isEmpty()) {
            mPokemonList.clear();
            mPokemonList.addAll(pokemonList);
            mPokemonAdapter.notifyDataSetChanged();
        }
    }

    private OnListClickListener mListClick = new OnListClickListener() {
        @Override
        public void onItemClick(int position) {
            final GDPokemon pokemon = mPokemonList.get(position);
            final String id = pokemon.getId();
            PokemonDetailActivity.show(PokemonListActivity.this, id);
        }

        @Override
        public void onTagClick(int tag, int position) {

        }
    };
}
