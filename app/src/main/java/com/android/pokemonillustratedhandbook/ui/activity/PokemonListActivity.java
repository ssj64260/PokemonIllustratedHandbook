package com.android.pokemonillustratedhandbook.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.pokemonillustratedhandbook.R;
import com.android.pokemonillustratedhandbook.app.BaseActivity;
import com.android.pokemonillustratedhandbook.model.DaoSession;
import com.android.pokemonillustratedhandbook.model.GDAbility;
import com.android.pokemonillustratedhandbook.model.GDAbilityDao;
import com.android.pokemonillustratedhandbook.model.GDPokemon;
import com.android.pokemonillustratedhandbook.model.GDPokemonDao;
import com.android.pokemonillustratedhandbook.model.GDPokemonName;
import com.android.pokemonillustratedhandbook.model.GDPokemonNameDao;
import com.android.pokemonillustratedhandbook.model.GDProperty;
import com.android.pokemonillustratedhandbook.model.GDPropertyDao;
import com.android.pokemonillustratedhandbook.model.JoinPokemonToAbility;
import com.android.pokemonillustratedhandbook.model.JoinPokemonToAbilityDao;
import com.android.pokemonillustratedhandbook.model.JoinPokemonToProperty;
import com.android.pokemonillustratedhandbook.model.JoinPokemonToPropertyDao;
import com.android.pokemonillustratedhandbook.ui.adapter.GDPokemonListAdapter;
import com.android.pokemonillustratedhandbook.ui.adapter.OnListClickListener;
import com.android.pokemonillustratedhandbook.utils.AssetsUtil;
import com.android.pokemonillustratedhandbook.utils.FastClick;
import com.android.pokemonillustratedhandbook.utils.GreenDaoHelper;
import com.android.pokemonillustratedhandbook.utils.ThreadPoolUtil;
import com.android.pokemonillustratedhandbook.widget.DividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class PokemonListActivity extends BaseActivity {

    private EditText etKeyword;
    private ImageView ivSearch;
    private RecyclerView rvPokemonList;
    private FloatingActionButton btnToTop;

    private List<GDPokemon> mPokemonList;
    private List<GDPokemon> mCurrentList;
    private GDPokemonListAdapter mPokemonAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_pokemon_list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        etKeyword.removeTextChangedListener(mTextWatcher);
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
        mCurrentList = new ArrayList<>();
        mPokemonAdapter = new GDPokemonListAdapter(this, mCurrentList);
        mPokemonAdapter.setListClick(mListClick);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setToolbarTitle("Pokemon List");

        etKeyword = findViewById(R.id.et_keyword);
        ivSearch = findViewById(R.id.iv_search);
        rvPokemonList = findViewById(R.id.rv_pokemon_list);
        btnToTop = findViewById(R.id.btn_to_top);

        ivSearch.setOnClickListener(mClick);
        btnToTop.setOnClickListener(mClick);
        etKeyword.addTextChangedListener(mTextWatcher);

        rvPokemonList.setLayoutManager(new LinearLayoutManager(this));
        rvPokemonList.addItemDecoration(new DividerItemDecoration(this, 1, R.color.color_FAFAFA));
        rvPokemonList.setAdapter(mPokemonAdapter);

        getPokemonList();
    }

    private void initPokemonDB(final DaoSession daoSession) {
        final Gson gson = new GsonBuilder().create();
        final String pokemonJson = AssetsUtil.getAssetsTxtByName(this, "pokemon.json");
        final String nameJson = AssetsUtil.getAssetsTxtByName(this, "pokemon_name.json");
        final String propertyJson = AssetsUtil.getAssetsTxtByName(this, "property.json");
        final String abilityJson = AssetsUtil.getAssetsTxtByName(this, "ability.json");

        final List<GDPokemon> pokemonList = gson.fromJson(pokemonJson, new TypeToken<List<GDPokemon>>() {
        }.getType());
        final List<GDPokemonName> nameList = gson.fromJson(nameJson, new TypeToken<List<GDPokemonName>>() {
        }.getType());
        final List<GDProperty> propertyList = gson.fromJson(propertyJson, new TypeToken<List<GDProperty>>() {
        }.getType());
        final List<GDAbility> abilityList = gson.fromJson(abilityJson, new TypeToken<List<GDAbility>>() {
        }.getType());
        final List<JoinPokemonToProperty> joinPTPList = new ArrayList<>();
        final List<JoinPokemonToAbility> joinPTCList = new ArrayList<>();

        if (pokemonList == null || pokemonList.isEmpty()) {
            return;
        }

        final GDPokemonDao pokemonDao = daoSession.getGDPokemonDao();
        final GDPokemonNameDao nameDao = daoSession.getGDPokemonNameDao();
        final GDPropertyDao propertyDao = daoSession.getGDPropertyDao();
        final GDAbilityDao abilityDao = daoSession.getGDAbilityDao();
        final JoinPokemonToPropertyDao joinPTPDao = daoSession.getJoinPokemonToPropertyDao();
        final JoinPokemonToAbilityDao joinPTCDao = daoSession.getJoinPokemonToAbilityDao();

        for (GDPokemon pokemon : pokemonList) {
            final String id = pokemon.getId();
            final List<GDProperty> pList = pokemon.getProperty();
            final List<GDAbility> cList = pokemon.getAbility();

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
                for (GDAbility ability : cList) {
                    final String cid = ability.getId();
                    final JoinPokemonToAbility ptc = new JoinPokemonToAbility();
                    ptc.setPokemonId(id);
                    ptc.setAbilityId(cid);
                    joinPTCList.add(ptc);
                }
            }
        }

        joinPTPDao.insertInTx(joinPTPList);
        joinPTCDao.insertInTx(joinPTCList);
        pokemonDao.insertInTx(pokemonList);
        nameDao.insertInTx(nameList);
        propertyDao.insertInTx(propertyList);
        abilityDao.insertInTx(abilityList);
    }

    private void getPokemonList() {
        showProgress("加载中...");

        ThreadPoolUtil.getInstache().singleExecute(new Runnable() {
            @Override
            public void run() {
                final DaoSession daoSession = new GreenDaoHelper().getDaoSession();
                final GDPokemonDao pokemonDao = daoSession.getGDPokemonDao();

                final QueryBuilder<GDPokemon> qb = pokemonDao.queryBuilder();
                qb.orderAsc(GDPokemonDao.Properties.Id);
                final Query<GDPokemon> query = qb.build();

                List<GDPokemon> pokemonList = query.list();
                if (pokemonList == null || pokemonList.isEmpty()) {
                    initPokemonDB(daoSession);
                    pokemonList = query.list();
                }

                if (pokemonList != null && !pokemonList.isEmpty()) {
                    mPokemonList.clear();
                    mPokemonList.addAll(pokemonList);
                }

                mCurrentList.clear();
                mCurrentList.addAll(mPokemonList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPokemonAdapter.notifyDataSetChanged();
                        hideProgress();
                    }
                });
            }
        });
    }

    private void doSearch() {
        final String keyword = etKeyword.getText().toString();
        if (!TextUtils.isEmpty(keyword)) {
            hideKeyboard();

            showProgress("搜索中...");

            ThreadPoolUtil.getInstache().singleExecute(new Runnable() {
                @Override
                public void run() {
                    final DaoSession daoSession = new GreenDaoHelper().getDaoSession();
                    final GDPokemonDao pokemonDao = daoSession.getGDPokemonDao();

                    final QueryBuilder<GDPokemon> qb = pokemonDao.queryBuilder();
                    qb.whereOr(GDPokemonDao.Properties.Id.like("%" + keyword + "%"),
                            GDPokemonDao.Properties.Name.like("%" + keyword + "%"));
                    final Query<GDPokemon> query = qb.build();

                    final List<GDPokemon> pokemonList = query.list();
                    mCurrentList.clear();
                    if (pokemonList != null && !pokemonList.isEmpty()) {
                        mCurrentList.addAll(pokemonList);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPokemonAdapter.notifyDataSetChanged();
                            hideProgress();
                        }
                    });
                }
            });
        }
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_search:
                    doSearch();
                    break;
                case R.id.btn_to_top:
                    rvPokemonList.scrollToPosition(0);
                    mAppBarLayout.setExpanded(true, true);
                    btnToTop.setVisibility(View.INVISIBLE);
                    break;
            }
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            final String text = s.toString();
            if (TextUtils.isEmpty(text)) {
                mCurrentList.clear();
                mCurrentList.addAll(mPokemonList);
                mPokemonAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private OnListClickListener mListClick = new OnListClickListener() {
        @Override
        public void onItemClick(int position) {
            final GDPokemon pokemon = mCurrentList.get(position);
            final String id = pokemon.getId();
            PokemonDetailActivity.show(PokemonListActivity.this, id);
        }

        @Override
        public void onTagClick(int tag, int position) {

        }
    };
}
