package com.android.pokemonillustratedhandbook.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.pokemonillustratedhandbook.R;
import com.android.pokemonillustratedhandbook.app.BaseActivity;
import com.android.pokemonillustratedhandbook.model.DaoSession;
import com.android.pokemonillustratedhandbook.model.GDAbility;
import com.android.pokemonillustratedhandbook.model.GDPokemon;
import com.android.pokemonillustratedhandbook.model.GDPokemonDao;
import com.android.pokemonillustratedhandbook.model.GDPokemonName;
import com.android.pokemonillustratedhandbook.model.GDProperty;
import com.android.pokemonillustratedhandbook.widget.imageloader.DrawableTransitionOptions;
import com.android.pokemonillustratedhandbook.utils.DisplayUtil;
import com.android.pokemonillustratedhandbook.utils.GreenDaoHelper;
import com.android.pokemonillustratedhandbook.widget.imageloader.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class PokemonDetailActivity extends BaseActivity {

    private static final String KEY_POKEMON_ID = "key_pokemon_id";

    private ScrollView svBackground;//总背景色
    private TextView tvCnName;//中文名
    private TextView tvJpEnName;//英文日文名
    private TextView tvId;//编号
    private ImageView ivImage;//图片
    private LinearLayout llBgAbility;//特性总背景色
    private LinearLayout llBgProperty;//属性总背景色
    private LinearLayout llBgEthnicValue;//种族值背景色
    private LinearLayout llBgAttributes;//各项属性背景色
    private TextView tvAbility1;//特性1
    private TextView tvAbility2;//特性2
    private TextView tvAbility3;//特殊特性
    private TextView tvProperty1;//属性1
    private TextView tvProperty2;//属性2
    private TextView tvEthnicValue;//种族值
    private TextView tvHp;//hp
    private TextView tvAttack;//攻击
    private TextView tvDefense;//防御
    private TextView tvSattack;//特攻
    private TextView tvSdefense;//特防
    private TextView tvSpeed;//速度
    private View lineHp;//hp柱状图
    private View lineAttack;//攻击柱状图
    private View lineDefense;//防御柱状图
    private View lineSattack;//特攻柱状图
    private View lineSdefense;//特防柱状图
    private View lineSpeed;//速度柱状图
    private Button btnAddDatabase;//加入数据库

    private String mPokemonId;
    private GDPokemon mPokemon;
    private List<GDProperty> mPropertyList;
    private List<GDAbility> mAbilityList;

    private final int[] mPropertyColor = {
            Color.parseColor("#FFBBBBAA"), Color.parseColor("#FFBB5544"), Color.parseColor("#FF6699FF"),
            Color.parseColor("#FFAA5599"), Color.parseColor("#FFDDBB55"), Color.parseColor("#FFBBAA66"),
            Color.parseColor("#FFAABB22"), Color.parseColor("#FF6666BB"), Color.parseColor("#FFAAAABB"),
            Color.parseColor("#FFFF4422"), Color.parseColor("#FF3399FF"), Color.parseColor("#FF77CC55"),
            Color.parseColor("#FFFFCC33"), Color.parseColor("#FFFF5599"), Color.parseColor("#FF77DDFF"),
            Color.parseColor("#FF7766EE"), Color.parseColor("#FF775544"), Color.parseColor("#FFFFAAFF")
    };
    private final int[] mPropertyTextColor = {
            R.drawable.shape_bg_general_light, R.drawable.shape_bg_fighting_light, R.drawable.shape_bg_flight_light,
            R.drawable.shape_bg_poison_light, R.drawable.shape_bg_ground_light, R.drawable.shape_bg_rock_light,
            R.drawable.shape_bg_insect_light, R.drawable.shape_bg_ghost_light, R.drawable.shape_bg_steel_light,
            R.drawable.shape_bg_fire_light, R.drawable.shape_bg_water_light, R.drawable.shape_bg_grass_light,
            R.drawable.shape_bg_electricity_light, R.drawable.shape_bg_superpower_light, R.drawable.shape_bg_ice_light,
            R.drawable.shape_bg_dragon_light, R.drawable.shape_bg_evil_light, R.drawable.shape_bg_fairy_light
    };
    private final int[] mPropertyBGColor = {
            R.drawable.shape_bg_general, R.drawable.shape_bg_fighting, R.drawable.shape_bg_flight,
            R.drawable.shape_bg_poison, R.drawable.shape_bg_ground, R.drawable.shape_bg_rock,
            R.drawable.shape_bg_insect, R.drawable.shape_bg_ghost, R.drawable.shape_bg_steel,
            R.drawable.shape_bg_fire, R.drawable.shape_bg_water, R.drawable.shape_bg_grass,
            R.drawable.shape_bg_electricity, R.drawable.shape_bg_superpower, R.drawable.shape_bg_ice,
            R.drawable.shape_bg_dragon, R.drawable.shape_bg_evil, R.drawable.shape_bg_fairy
    };

    public static void show(Activity activity, String pokemonId) {
        Intent intent = new Intent();
        intent.setClass(activity, PokemonDetailActivity.class);
        intent.putExtra(KEY_POKEMON_ID, pokemonId);
        activity.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pokemon_detail;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isChange", true);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void initData() {
        super.initData();

        mPokemonId = getIntent().getStringExtra(KEY_POKEMON_ID);
        final DaoSession daoSession = new GreenDaoHelper().getDaoSession();
        final GDPokemonDao pokemonDao = daoSession.getGDPokemonDao();

        final QueryBuilder<GDPokemon> qb = pokemonDao.queryBuilder();
        mPokemon = qb.where(GDPokemonDao.Properties.Id.eq(mPokemonId)).unique();

        mPropertyList = new ArrayList<>();
        mAbilityList = new ArrayList<>();
    }

    private void setStatusBar() {
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        setStatusBar();

        svBackground = findViewById(R.id.sv_background);
        tvCnName = findViewById(R.id.tv_cn_name);
        tvJpEnName = findViewById(R.id.tv_jp_en_name);
        tvId = findViewById(R.id.tv_pm_id);
        ivImage = findViewById(R.id.iv_pm_img);
        llBgAbility = findViewById(R.id.ll_bg_ability);
        llBgProperty = findViewById(R.id.ll_bg_property);
        llBgEthnicValue = findViewById(R.id.ll_bg_ethnic_value);
        llBgAttributes = findViewById(R.id.ll_bg_attributes);
        tvAbility1 = findViewById(R.id.tv_ability1);
        tvAbility2 = findViewById(R.id.tv_ability2);
        tvAbility3 = findViewById(R.id.tv_ability3);
        tvProperty1 = findViewById(R.id.tv_pm_property1);
        tvProperty2 = findViewById(R.id.tv_pm_property2);
        tvEthnicValue = findViewById(R.id.tv_ethnic_value);
        tvHp = findViewById(R.id.tv_hp);
        tvAttack = findViewById(R.id.tv_attack);
        tvDefense = findViewById(R.id.tv_defense);
        tvSattack = findViewById(R.id.tv_s_attack);
        tvSdefense = findViewById(R.id.tv_s_defense);
        tvSpeed = findViewById(R.id.tv_speed);
        lineHp = findViewById(R.id.view_hp_line);
        lineAttack = findViewById(R.id.view_attack_line);
        lineDefense = findViewById(R.id.view_defense_line);
        lineSattack = findViewById(R.id.view_s_attack_line);
        lineSdefense = findViewById(R.id.view_s_defense_line);
        lineSpeed = findViewById(R.id.view_speede_line);
        btnAddDatabase = findViewById(R.id.btn_add_database);

        if (mPokemon == null) {
            showSnackbar("没有该Pokemon的信息");
            return;
        }

        final String id = mPokemon.getId();
        final String name = mPokemon.getName();
        final GDPokemonName pokemonName = mPokemon.getPmName();
        final String mega = mPokemon.getMega();
        final String logo1 = "http://res.pokemon.name/sprites/core/xy/front/" + id + "." + mega + ".png";
        final String logo2 = "http://res.pokemon.name/common/pokemon/pgl/" + id + "." + mega + ".png";

        final List<GDProperty> propertyList = mPokemon.getProperty();
        mPropertyList.clear();

        final int primaryColor;
        final int primaryDarkColor;
        if (propertyList != null && !propertyList.isEmpty()) {
            mPropertyList.addAll(propertyList);

            final boolean haveOne = mPropertyList.size() > 0;
            final boolean haveTwo = mPropertyList.size() > 1;
            final int pId1 = haveOne ? Integer.parseInt(mPropertyList.get(0).getId()) - 1 : 0;
            final int pId2 = haveTwo ? Integer.parseInt(mPropertyList.get(1).getId()) - 1 : 0;
            final String pName1 = haveOne ? mPropertyList.get(0).getName() : "";
            final String pName2 = haveTwo ? mPropertyList.get(1).getName() : "";

            primaryColor = mPropertyColor[pId1];
            primaryDarkColor = mPropertyTextColor[pId1];

            tvProperty1.setText(pName1);
            tvProperty2.setText(pName2);
            tvProperty1.setVisibility(TextUtils.isEmpty(pName1) ? View.GONE : View.VISIBLE);
            tvProperty2.setVisibility(TextUtils.isEmpty(pName2) ? View.GONE : View.VISIBLE);

            tvProperty1.setOnClickListener(mClick);
            tvProperty2.setOnClickListener(mClick);
            tvProperty1.setBackgroundResource(mPropertyBGColor[pId1]);
            tvProperty2.setBackgroundResource(mPropertyBGColor[pId2]);
        } else {
            primaryColor = mPropertyColor[0];
            primaryDarkColor = mPropertyTextColor[0];
        }

        final List<GDAbility> abilityList = mPokemon.getAbility();
        mAbilityList.clear();
        if (abilityList != null && !abilityList.isEmpty()) {
            mAbilityList.addAll(abilityList);
            final String cName1 = mAbilityList.size() > 0 ? mAbilityList.get(0).getName() : "";
            final String cName2 = mAbilityList.size() > 1 ? mAbilityList.get(1).getName() : "";
            final String cName3 = mAbilityList.size() > 2 ? mAbilityList.get(2).getName() : "";

            if (!TextUtils.isEmpty(cName3)) {
                tvAbility1.setText(cName1);
                tvAbility2.setText(cName2);
                tvAbility3.setText(cName3);

                tvAbility2.setVisibility(View.VISIBLE);
            } else {
                tvAbility1.setText(cName1);
                tvAbility3.setText(cName2);
            }
            tvAbility1.setOnClickListener(mClick);
            tvAbility2.setOnClickListener(mClick);
            tvAbility3.setOnClickListener(mClick);
        }

        final String hp = mPokemon.getHp();
        final String attack = mPokemon.getAttack();
        final String defense = mPokemon.getDefense();
        final String sAttack = mPokemon.getS_attack();
        final String sDefense = mPokemon.getS_defense();
        final String speed = mPokemon.getSpeed();
        final String ethnicValue = mPokemon.getEthnic_value();

        mRootView.setBackgroundColor(primaryColor);
        llBgAbility.setBackgroundResource(primaryDarkColor);
        llBgProperty.setBackgroundResource(primaryDarkColor);
        llBgEthnicValue.setBackgroundResource(primaryDarkColor);
        llBgAttributes.setBackgroundResource(primaryDarkColor);
        tvCnName.setText(name);

        if (pokemonName != null) {
            final String pmName = pokemonName.getJp_name() + " " + pokemonName.getEn_name();
            tvJpEnName.setText(pmName);
        }
        tvId.setText("#" + id);

        GlideApp.with(this)
                .load(logo2)
                .transition(DrawableTransitionOptions.withCrossFade(1000, true))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.bg_ditto)
                .error(R.drawable.bg_ditto)
                .skipMemoryCache(true)
                .fitCenter()
                .into(ivImage);

        tvEthnicValue.setText(ethnicValue);
        tvHp.setText(hp);
        tvAttack.setText(attack);
        tvDefense.setText(defense);
        tvSattack.setText(sAttack);
        tvSdefense.setText(sDefense);
        tvSpeed.setText(speed);

        final LinearLayout.LayoutParams hpParams = (LinearLayout.LayoutParams) lineHp.getLayoutParams();
        hpParams.width = (int) (DisplayUtil.dip2px(Float.parseFloat(hp)) * 0.8);
        final LinearLayout.LayoutParams attackParams = (LinearLayout.LayoutParams) lineAttack.getLayoutParams();
        attackParams.width = (int) (DisplayUtil.dip2px(Float.parseFloat(attack)) * 0.8);
        final LinearLayout.LayoutParams defenseParams = (LinearLayout.LayoutParams) lineDefense.getLayoutParams();
        defenseParams.width = (int) (DisplayUtil.dip2px(Float.parseFloat(defense)) * 0.8);
        final LinearLayout.LayoutParams sAttackParams = (LinearLayout.LayoutParams) lineSattack.getLayoutParams();
        sAttackParams.width = (int) (DisplayUtil.dip2px(Float.parseFloat(sAttack)) * 0.8);
        final LinearLayout.LayoutParams sDefenseParams = (LinearLayout.LayoutParams) lineSdefense.getLayoutParams();
        sDefenseParams.width = (int) (DisplayUtil.dip2px(Float.parseFloat(sDefense)) * 0.8);
        final LinearLayout.LayoutParams speedParams = (LinearLayout.LayoutParams) lineSpeed.getLayoutParams();
        speedParams.width = (int) (DisplayUtil.dip2px(Float.parseFloat(speed)) * 0.8);

        lineHp.setLayoutParams(hpParams);
        lineAttack.setLayoutParams(attackParams);
        lineDefense.setLayoutParams(defenseParams);
        lineSattack.setLayoutParams(sAttackParams);
        lineSdefense.setLayoutParams(sDefenseParams);
        lineSpeed.setLayoutParams(speedParams);

        final ScaleAnimation scaleX = new ScaleAnimation(0f, 1f, 1f, 1f);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleX.setFillAfter(true);
        scaleX.setDuration(1500);

        lineHp.startAnimation(scaleX);
        lineAttack.startAnimation(scaleX);
        lineDefense.startAnimation(scaleX);
        lineSattack.startAnimation(scaleX);
        lineSdefense.startAnimation(scaleX);
        lineSpeed.startAnimation(scaleX);

        btnAddDatabase.setOnClickListener(mClick);

        if (savedInstanceState != null) {
            svBackground.post(new Runnable() {
                @Override
                public void run() {
                    svBackground.setScrollY(0);
                }
            });
        }
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_database:
                    final String name = mPokemon.getName();
                    final String detailWeb = "https://wiki.52poke.com/wiki/" + name;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(detailWeb));
                    startActivity(intent);
                    break;
                case R.id.tv_ability1:
                    showSnackbar(mAbilityList.get(0).getDescription());
                    break;
                case R.id.tv_ability2:
                    showSnackbar(mAbilityList.get(1).getDescription());
                    break;
                case R.id.tv_ability3:
                    showSnackbar(mAbilityList.get(mAbilityList.size() - 1).getDescription());
                    break;
                case R.id.tv_pm_property1:
                    if (tvProperty1.getText().toString().equals(mPropertyList.get(0).getName())) {
                        tvProperty1.setText(mPropertyList.get(0).getEn_name());
                    } else {
                        tvProperty1.setText(mPropertyList.get(0).getName());
                    }
                    break;
                case R.id.tv_pm_property2:
                    if (tvProperty2.getText().toString().equals(mPropertyList.get(1).getName())) {
                        tvProperty2.setText(mPropertyList.get(1).getEn_name());
                    } else {
                        tvProperty2.setText(mPropertyList.get(1).getName());
                    }
                    break;
            }
        }
    };
}
