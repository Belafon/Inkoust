package com.belafon.zapoctovy_program.Client;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.belafon.zapoctovy_program.MainActivity;
import com.belafon.zapoctovy_program.R;

import org.w3c.dom.Text;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public static int idEditText = 1;
    public int[] slideTextView = {
            R.string.slider1,
            R.string.slider2,
            R.string.slider3,
            R.string.slider4,
            R.string.slider5,
            R.string.slider6};

    @Override
    public int getCount() {
        return slideTextView.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    public Object instantiateItem(ViewGroup conteiner, int position){

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_layout, conteiner, false);
        TextView textView = view.findViewById(R.id.textIntreduction);
        textView.setText(slideTextView[position]);
        LinearLayout text = view.findViewById(R.id.tutorial_text);
        LinearLayout pictures = view.findViewById(R.id.tutorial_pictures);
        switch (position){
            case 0:
                text.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                intreduction0(text, pictures, view);
                break;
            case 1 :
                text.setLayoutParams(new LinearLayout.LayoutParams(MainActivity.screenWidth, ViewGroup.LayoutParams.MATCH_PARENT));
                intreduction1(text, pictures, view);
                break;
            case 2:
                text.setLayoutParams(new LinearLayout.LayoutParams((int)(MainActivity.screenWidth / 1.5), ViewGroup.LayoutParams.MATCH_PARENT));
                intreduction2(text, pictures, view);
                break;
            case 3:
                text.setLayoutParams(new LinearLayout.LayoutParams((int)(MainActivity.screenWidth / 1.5), ViewGroup.LayoutParams.MATCH_PARENT));
                intreduction3(text, pictures, view);
                break;
            case 4:
                text.setLayoutParams(new LinearLayout.LayoutParams((int)(MainActivity.screenWidth / 1.25), ViewGroup.LayoutParams.MATCH_PARENT));
                intreduction4(text, pictures, view);
                break;
            case 5:
                text.setLayoutParams(new LinearLayout.LayoutParams((int)(MainActivity.screenWidth), ViewGroup.LayoutParams.MATCH_PARENT));
                intreduction5(text, pictures, view);
                break;
        }

        conteiner.addView(view);
        return view;
    }

    private void intreduction5(LinearLayout text, LinearLayout pictures, View view) {
        pictures.setVisibility(View.GONE);
        TextView intreduction = new TextView(view.getContext());
        intreduction.setText(R.string.attack);
        text.setPadding(150, 50, 150, 0);
        intreduction.setTextSize(18);
        intreduction.setPadding(0, 0, 0, 10);
        text.addView(intreduction);

        Button butContinue = new Button(MainActivity.actual_activity);
        butContinue.setLayoutParams(new LinearLayout.LayoutParams(
                250,
                100));
        butContinue.setHint("Continue");
        butContinue.setHintTextColor(Color.BLACK);
        text.addView(butContinue);
        butContinue.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                                MainActivity.actual_activity.findViewById(R.id.viewPager).setVisibility(View.GONE);
                                                MainActivity.actual_activity.findViewById(R.id.startMenu).setVisibility(View.VISIBLE);
                                           }
                                       }
        );
    }
    private void intreduction4(LinearLayout text, LinearLayout pictures, View view) {
        TextView intreduction = new TextView(view.getContext());
        intreduction.setText(R.string.map_tutorial);
        intreduction.setPadding(150, 50, 20, 0);
        intreduction.setTextSize(18);
        text.addView(intreduction);

        pictures.setPadding(0, 80, 0, 0);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (MainActivity.screenWidth - (int)(MainActivity.screenWidth / 1.25))/2));
        layoutParams.setMargins(0, 5, 0, 0);
        ImageView village = new ImageView(view.getContext());
        village.setLayoutParams(layoutParams);
        village.setImageDrawable(view.getContext().getDrawable(R.drawable.village));
        pictures.addView(village);

        ImageView meadow = new ImageView(view.getContext());
        meadow.setLayoutParams(layoutParams);
        meadow.setImageDrawable(view.getContext().getDrawable(R.drawable.meadow));
        pictures.addView(meadow);

        ImageView forest = new ImageView(view.getContext());
        forest.setLayoutParams(layoutParams);
        forest.setImageDrawable(view.getContext().getDrawable(R.drawable.forest));
        pictures.addView(forest);

        ImageView mountains = new ImageView(view.getContext());
        mountains.setLayoutParams(layoutParams);
        mountains.setImageDrawable(view.getContext().getDrawable(R.drawable.mountains));
        pictures.addView(mountains);
    }

    private void intreduction3(LinearLayout text, LinearLayout pictures, View view) {
        TextView intreduction = new TextView(view.getContext());
        intreduction.setText(R.string.village);
        intreduction.setPadding(150, 50, 0, 0);
        intreduction.setTextSize(18);
        text.addView(intreduction);

        pictures.setPadding(0, 50, 0, 0);
        RelativeLayout rel_village_civilian = new RelativeLayout(view.getContext());
        rel_village_civilian.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MainActivity.screenWidth - (int)(MainActivity.screenWidth / 1.25)));
        layoutParams.setMargins(0, 0, 0, 0);
        ImageView village = new ImageView(view.getContext());
        village.setLayoutParams(layoutParams);
        village.setImageDrawable(view.getContext().getDrawable(R.drawable.village));

        ImageView civilian = new ImageView(view.getContext());
        civilian.setLayoutParams(layoutParams);
        civilian.setImageDrawable(view.getContext().getDrawable(R.drawable.civilian));

        rel_village_civilian.addView(village);
        rel_village_civilian.addView(civilian);


        RelativeLayout rel_village_soldier = new RelativeLayout(view.getContext());
        rel_village_civilian.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MainActivity.screenWidth - (int)(MainActivity.screenWidth / 1.25)));
        layoutParams4.setMargins(0, -20, 0, 0);
        ImageView village2 = new ImageView(view.getContext());
        village2.setLayoutParams(layoutParams4);
        village2.setImageDrawable(view.getContext().getDrawable(R.drawable.village));
        rel_village_soldier.addView(village2);

        ImageView soldier = new ImageView(view.getContext());
        soldier.setLayoutParams(layoutParams4);
        soldier.setImageDrawable(view.getContext().getDrawable(R.drawable.soldier));
        rel_village_soldier.addView(soldier);

        pictures.addView(rel_village_civilian);
        pictures.addView(rel_village_soldier);
    }

    private void intreduction2(LinearLayout text, LinearLayout pictures, View view) {
        TextView intreduction = new TextView(view.getContext());
        intreduction.setText(R.string.village_units);
        intreduction.setPadding(150, 50, 20, 0);
        intreduction.setTextSize(18);
        text.addView(intreduction);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MainActivity.screenWidth - (int)(MainActivity.screenWidth / 1.25)));

        layoutParams.setMargins(0, 30, 0, 0);
        ImageView village = new ImageView(view.getContext());
        village.setLayoutParams(layoutParams);
        village.setImageDrawable(view.getContext().getDrawable(R.drawable.village));
        pictures.addView(village);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,MainActivity.screenWidth - (int)(MainActivity.screenWidth / 1.25)));
        layoutParams2.setMargins(0, 0, 0, 0);
        ImageView civilian = new ImageView(view.getContext());
        civilian.setLayoutParams(layoutParams2);
        civilian.setImageDrawable(view.getContext().getDrawable(R.drawable.civilian));
        pictures.addView(civilian);

        LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MainActivity.screenWidth - (int)(MainActivity.screenWidth / 1.25)));
        layoutParams3.setMargins(0, 0, 0, 0);
        ImageView soldier = new ImageView(view.getContext());
        soldier.setLayoutParams(layoutParams3);
        soldier.setImageDrawable(view.getContext().getDrawable(R.drawable.soldier));
        pictures.addView(soldier);
    }

    private void intreduction1(LinearLayout text, LinearLayout pictures, View view) {
        pictures.setVisibility(View.GONE);
        TextView intreduction = new TextView(view.getContext());
        intreduction.setText(R.string.end_game);
        intreduction.setPadding(150, 50, 150, 15);
        intreduction.setTextSize(18);
        text.addView(intreduction);
    }

    private void intreduction0(LinearLayout text, LinearLayout pictures, View view) {
        pictures.setVisibility(View.GONE);
        TextView intreduction = new TextView(view.getContext());
        intreduction.setText(R.string.intreduction);
        text.setPadding(150, 50, 150, 0);
        intreduction.setPadding(0, 0, 0, 10);
        intreduction.setTextSize(18);
        text.addView(intreduction);

        Button butContinue = new Button(view.getContext());
        butContinue.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                100));
        butContinue.setHint("Skip tutorial");
        butContinue.setHintTextColor(Color.BLACK);
        text.addView(butContinue);
        butContinue.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               MainActivity.actual_activity.findViewById(R.id.viewPager).setVisibility(View.GONE);
                                               MainActivity.actual_activity.findViewById(R.id.startMenu).setVisibility(View.VISIBLE);
                                           }
                                       }
        );
    }

    @Override
    public void destroyItem(ViewGroup conteiner, int position, Object object){
        conteiner.removeView((RelativeLayout)object);
    }
}
