package com.example.rezeptapp;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CookingBookFragment extends Fragment {

    LinearLayout bookSitesLayout;
    ArrayList<CardView> recipeCardList = new ArrayList<>();
    ArrayList<ShortInfo> dbResults = new ArrayList<>();
    DBHandler dbHandler;
    public CookingBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new DBHandler(getContext());
        dbResults=dbHandler.getAllShortInfo();
        for(int i=0; i<recipeCardList.size();i++){
            final int index = i;
            CardView cardview = recipeCardList.get(i);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cooking_book, container, false);

        bookSitesLayout = view.findViewById(R.id.BookSitesLayout);
        for(int i=0; i<dbResults.size();i++){
            bookSitesLayout.addView(createRecipeCard(dbResults.get(i).getTitle(), dbResults.get(i).getImage(), dbResults.get(i).getId()));
        }





        // Inflate the layout for this fragment
        return view;
    }


    private CardView createRecipeCard(String title, String url, int id){
        // Creates Card View
        CardView cardView = new CardView(getContext());
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(dpToPx(32), dpToPx(12), dpToPx(32), dpToPx(12));
        cardView.setLayoutParams(layoutParams);
        cardView.setCardElevation(dpToPx(24));
        cardView.setRadius(dpToPx(24));

        // Creates RelativeLayouts
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Creates ImageView
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dpToPx(180)
        ));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.get().load(url).into(imageView);

        // Creates TextView for Background
        TextView backgroundTextView = new TextView(getContext());
        RelativeLayout.LayoutParams outlineParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        outlineParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        backgroundTextView.setLayoutParams(outlineParams);
        backgroundTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        backgroundTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        backgroundTextView.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.outline));

        // Creates TextView for Recipe Name
        TextView titleTextView = new TextView(getContext());
        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        titleParams.setMargins(dpToPx(8), 0, dpToPx(8), 0);
        titleTextView.setLayoutParams(titleParams);
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        titleTextView.setText(title);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleTextView.setTextColor(Color.WHITE);


        // Hinzufügen der Views zum RelativeLayout
        relativeLayout.addView(imageView);
        relativeLayout.addView(backgroundTextView);
        relativeLayout.addView(titleTextView);

        // Hinzufügen des RelativeLayouts zur CardView
        cardView.addView(relativeLayout);

        // Hinzufügen der CardView zum Eltern-View
        //ViewGroup parentView = findViewById(R.id.parentView); // Hier die ID deines Eltern-Views setzen
        //parentView.addView(cardView);
        recipeCardList.add(cardView);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("foundRecipe", String.valueOf(id));//bResults.get(index).getId()));
                RecipePageFragment recipePageFragment = new RecipePageFragment();
                recipePageFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, recipePageFragment).addToBackStack(null).commit();
            }
        });


        return cardView;
    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density);
    }


}