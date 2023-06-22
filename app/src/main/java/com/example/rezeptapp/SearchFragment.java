package com.example.rezeptapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class SearchFragment extends Fragment {

    Button searchButton;
    SearchManager searchmanager = new SearchManager();
    private ArrayList<ShortInfo> shortInfoList = new ArrayList<>();
    HashMap<String, String> general = new HashMap<>();
    HashMap<String, String> macronutrients = new HashMap<>();
    HashMap<String, String> micronutrients = new HashMap<>();
    HashMap<String, String> vitamins = new HashMap<>();

    EditText name;
    EditText ingredient;
    EditText excludedIngredient;

    //Diet
    Button diet;
    String[] dietItems;
    boolean[] checkedDiets;
    ArrayList<Integer> selectedDietsList = new ArrayList<>();
    boolean dietOr = false;

    //Cuisine
    Button cuisine;
    String[] cuisineItems;
    boolean[] checkedCuisines;
    ArrayList<Integer> selectedCuisineList = new ArrayList<>();
    boolean excludeCuisine = false;

    //Ingredient
    Button addIngredientButton;
    Button removeIngredientButton;
    LinearLayout ingredientLayout;
    Space spaceIngredient;
    ArrayList<EditText> ingredientEditList = new ArrayList<>();

    //Exclude Ingredient
    Button addExcludeIngredientButton;
    Button removeExcludeIngredientButton;
    LinearLayout excludeIngredientLayout;
    Space spaceExcludeIngredient;
    ArrayList<EditText> excludeIngredientEditList = new ArrayList<>();

    //Macro Nutrients
    LinearLayout macroLayout ;
    ArrayList<LinearLayout> macroNutrientList = new ArrayList<>();
    Button macroButton;

    //Micro Nutrients
    LinearLayout microLayout ;
    ArrayList<LinearLayout> microNutrientList = new ArrayList<>();
    Button microButton;
    CardView microCard;

    //Vitamins
    LinearLayout vitaminLayout ;
    ArrayList<LinearLayout> vitaminList = new ArrayList<>();
    Button vitaminButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Button searchButton = (Button) view.findViewById(R.id.buttonSearch);
        createSearchHashmaps();

        name = view.findViewById(R.id.editTextName);

        //Diet
        diet  = view.findViewById(R.id.DietButton);
        dietItems = getResources().getStringArray(R.array.diet_list);
        checkedDiets = new boolean[dietItems.length];

        //Cuisine
        cuisine = view.findViewById(R.id.CuisineButton);
        cuisineItems = getResources().getStringArray(R.array.cuisine_list);
        checkedCuisines = new boolean[cuisineItems.length];

        //Ingredients
        ingredient = view.findViewById(R.id.editTextIngredient);
        addIngredientButton = view.findViewById(R.id.addIngredientButton);
        removeIngredientButton = view. findViewById(R.id.removeIngredientButton);
        ingredientLayout = view.findViewById(R.id.ingredientsLayout);
        spaceIngredient = view.findViewById(R.id.spaceIngredient);

        //Excluded Ingredients
        excludedIngredient = view.findViewById(R.id.editTextExcludedIngredient);
        addExcludeIngredientButton = view.findViewById(R.id.addExcludeIngredientButton);
        removeExcludeIngredientButton = view.findViewById(R.id.removeExcludeIngredientButton);
        excludeIngredientLayout = view.findViewById(R.id.excludeIngredientsLayout);
        spaceExcludeIngredient = view.findViewById(R.id.spaceExcludeIngredient);

        //Nutrients+Vitamins
        macroLayout = view.findViewById(R.id.MacroLayout);
        macroButton = view. findViewById(R.id.macroButton);
        microLayout = view.findViewById(R.id.MicroLayout);
        microButton = view.findViewById(R.id.microButton);
        microCard = view.findViewById(R.id.MicroCard);
        vitaminLayout = view.findViewById(R.id.VitaminLayout);
        vitaminButton = view.findViewById(R.id.vitaminButton);



//Buttons: Add/Remove Ingredient Edit Text Fields -------------------------------------------------------------------
        removeIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ingredientEditList.size()>0){
                    ingredientLayout.removeView(ingredientEditList.get(ingredientEditList.size()-1));
                    ingredientEditList.remove(ingredientEditList.get(ingredientEditList.size()-1));
                }
                if(ingredientEditList.size()==0){
                    removeIngredientButton.setEnabled(false);
                    removeIngredientButton.setVisibility(View.GONE);
                }

            }
        });
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newEditText = new EditText(getActivity());
                newEditText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                newEditText.setHint("Ingredient");
                ingredientLayout.addView(newEditText, ingredientLayout.indexOfChild(spaceIngredient));
                ingredientEditList.add(newEditText);
                Log.d("sizep ingredient", String.valueOf(ingredientLayout.indexOfChild(spaceIngredient)));
                Log.d("sizep button", String.valueOf(ingredientLayout.indexOfChild(removeIngredientButton)));
                Log.d("sizep editlist", String.valueOf(ingredientLayout.indexOfChild(ingredientEditList.get(0))));
                if(ingredientEditList.size()==1){
                    removeIngredientButton.setEnabled(true);
                    removeIngredientButton.setVisibility(View.VISIBLE);
                }
            }
        });

//-----------------------------------------------------------------------------------------------------------------------


//Buttons: Add/Remove Excluded Ingredient Edit Text Fields -------------------------------------------------------------------
        removeExcludeIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(excludeIngredientEditList.size()>0){
                    excludeIngredientLayout.removeView(excludeIngredientEditList.get(excludeIngredientEditList.size()-1));
                    excludeIngredientEditList.remove(excludeIngredientEditList.get(excludeIngredientEditList.size()-1));
                }
                if(excludeIngredientEditList.size()==0){
                    removeExcludeIngredientButton.setEnabled(false);
                    removeExcludeIngredientButton.setVisibility(View.GONE);
                }

            }
        });
        addExcludeIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText newEditText = new EditText(getActivity());
                newEditText.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                newEditText.setHint("Excluded Ingredient");
                excludeIngredientLayout.addView(newEditText, excludeIngredientLayout.indexOfChild(spaceExcludeIngredient));
                excludeIngredientEditList.add(newEditText);
                if(excludeIngredientEditList.size()==1){
                    removeExcludeIngredientButton.setEnabled(true);
                    removeExcludeIngredientButton.setVisibility(View.VISIBLE);
                }
            }
        });

//-----------------------------------------------------------------------------------------------------------------------


//Button: Cuisine Selection -----------------------------------------------------------------------------------------------------
        cuisine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder cuisineAlert = new AlertDialog.Builder(getActivity());
                if(!excludeCuisine)
                    cuisineAlert.setTitle("Select Include Cuisines");
                else
                    cuisineAlert.setTitle("Select Exclude Cuisines");
                cuisineAlert.setMultiChoiceItems(cuisineItems, checkedCuisines, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemPosition, boolean isChecked) {
                        if(isChecked){
                            if(!selectedCuisineList.contains(itemPosition)){
                                selectedCuisineList.add(itemPosition);
                            }
                        }
                        else{
                            for (int i = 0; i < selectedCuisineList.size(); i++)
                                if (selectedCuisineList.get(i) == itemPosition) {
                                    selectedCuisineList.remove(i);
                                    break;}
                        }
                    }

                });
                cuisineAlert.setCancelable(true);
                cuisineAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder cuisineString= new StringBuilder();
                        for(int i=0;i<selectedCuisineList.size(); i++){
                            cuisineString.append(cuisineItems[selectedCuisineList.get(i)]);
                            if(i != selectedCuisineList.size()-1){
                                cuisineString.append(",");
                            }
                        }
                        if(excludeCuisine){
                            general.put("excludedCuisine", cuisineString.toString());
                            general.put("cuisine", "");
                        }
                        else{
                            general.put("cuisine", cuisineString.toString());
                            general.put("excludedCuisine", "");
                        }
                        Log.d("Selected Cuisines", cuisineString.toString());
                        Log.d("hash Cuisines", general.get("cuisine"));
                        Log.d("hash ExcludedCuisines", general.get("excludedCuisine"));
                    }
                });
                cuisineAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                cuisineAlert.setNeutralButton("In-/Exclude", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(excludeCuisine){
                            cuisineAlert.setTitle("Select Include Cuisines");
                            excludeCuisine=false;
                        }
                        else{
                            cuisineAlert.setTitle("Select Exclude Cuisines");
                            excludeCuisine=true;
                        }
                        cuisineAlert.show();

                    }
                });
                cuisineAlert.show();
            }
        });

//-----------------------------------------------------------------------------------------------------------------------

//Button: Diet Selection -----------------------------------------------------------------------------------------------------
        diet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dietAlert = new AlertDialog.Builder(getActivity());
                if(!dietOr)
                    dietAlert.setTitle("Include all Diets");
                else
                    dietAlert.setTitle("Include at least one Diet");
                dietAlert.setMultiChoiceItems(dietItems, checkedDiets, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int itemPosition, boolean isChecked) {
                        if(isChecked){
                            if(!selectedDietsList.contains(itemPosition)){
                                selectedDietsList.add(itemPosition);
                            }
                        }
                        else{
                            for (int i = 0; i < selectedDietsList.size(); i++)
                                if (selectedDietsList.get(i) == itemPosition) {
                                    selectedDietsList.remove(i);
                                    break;}
                        }
                    }
                });
                dietAlert.setCancelable(true);
                dietAlert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder dietString= new StringBuilder();
                        for(int i=0;i<selectedDietsList.size(); i++){
                            dietString.append(dietItems[selectedDietsList.get(i)]);
                            if(i != selectedDietsList.size()-1){
                                dietString.append(",");
                            }
                        }
                        if(dietOr){
                            //Befehl für OR Suche anhängen
                            general.put("diet", String.valueOf(dietString));
                        }
                        else{
                            //Befehl für AND Suche anhängen
                            general.put("diet", String.valueOf(dietString));
                        }
                        Log.d("search diet",general.get("diet") );
                    }
                });
                dietAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dietAlert.setNeutralButton("Mode", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(dietOr){
                            dietAlert.setTitle("Include all Diets");
                            dietOr=false;
                        }
                        else{
                            dietAlert.setTitle("Include at least one Diet");
                            dietOr=true;
                        }
                        dietAlert.show();

                    }
                });
                dietAlert.show();
            }
        });

//-----------------------------------------------------------------------------------------------------------------------



// Button: Search -------------------------------------------------------------------------------------------------------------------
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread() {
                    public void run() {
                        try {
                            putValuesInHashmap();
                            shortInfoList = searchmanager.searchRecipes(general, macronutrients, micronutrients, vitamins);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
                thread.start();

        /*Thread thread = new Thread(){
            public void run(){

                try {
                    recipe = searchmanager.getRecipeByID("637897");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        thread.start();*/

                SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, searchResultsFragment).commit();
            }
        });
//-----------------------------------------------------------------------------------------------------------------------

//Create Nutrient Fields-----------------------------------------------------------------------------------------------
        createMacroNutrientList();
        createMicroNutrientList();
        createVitaminList();

//-----------------------------------------------------------------------------------------------------------------------

//Nutrient Buttons-----------------------------------------------------------------------------------------------
        macroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(macroNutrientList.get(0).getVisibility()==View.GONE){
                    for(int i=0; i<macroNutrientList.size(); i++){
                        macroNutrientList.get(i).setVisibility(view.VISIBLE);
                    }
                    macroButton.setText("/\\");
                }
                else{
                    for(int i=0; i<macroNutrientList.size(); i++){
                        macroNutrientList.get(i).setVisibility(view.GONE);
                    }
                    macroButton.setText("V");
                }
            }
        });

        microButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(microNutrientList.get(0).getVisibility()==View.GONE){
                    //TransitionManager.beginDelayedTransition(view.findViewById(R.id.MicroCard), new AutoTransition());
                    for(int i=0; i<microNutrientList.size(); i++){
                        microNutrientList.get(i).setVisibility(view.VISIBLE);
                    }
                    microButton.setText("/\\");
                }
                else{
                    for(int i=0; i<microNutrientList.size(); i++){
                        microNutrientList.get(i).setVisibility(view.GONE);
                    }
                    microButton.setText("V");
                }
            }
        });

        vitaminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vitaminList.get(0).getVisibility()==View.GONE){
                    for(int i=0; i<vitaminList.size(); i++){
                        vitaminList.get(i).setVisibility(view.VISIBLE);
                    }
                    vitaminButton.setText("/\\");
                }
                else{
                    for(int i=0; i<vitaminList.size(); i++){
                        vitaminList.get(i).setVisibility(view.GONE);
                    }
                    vitaminButton.setText("V");
                }
            }
        });

//-----------------------------------------------------------------------------------------------------------------------



//End of Create View

        return view;
    }

//Create Nutrient Fields-----------------------------------------------------------------------------------------------
    private void createMicroNutrientList(){
        String[] names = getResources().getStringArray(R.array.micronutrient_list);
        for (String s : names) {
            microNutrientList.add(createNutrientViews(s, microLayout));
        }
    }

    private void createMacroNutrientList(){
        String[] names = getResources().getStringArray(R.array.macronutrient_list);
        for (String s : names) {
            macroNutrientList.add(createNutrientViews(s, macroLayout));
        }
    }

    private void createVitaminList(){
        String[] names = getResources().getStringArray(R.array.vitamin_list);
        for (String s : names) {
            vitaminList.add(createNutrientViews(s, vitaminLayout));
        }
    }

    private LinearLayout createNutrientViews(String name, LinearLayout layout){
        LinearLayout linear = new LinearLayout(getContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(50,layoutParams.topMargin,50,layoutParams.bottomMargin);
        linear.setLayoutParams(layoutParams);
        linear.setOrientation(LinearLayout.HORIZONTAL);

        // Textviews
        TextView textView1 = new TextView(getContext());
        textView1.setText(name);
        textView1.setTextSize(18);
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        textView1.setLayoutParams(new LinearLayout.LayoutParams(
                (int) width,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        TextView textView2 = new TextView(getContext());
        textView2.setText("min.");
        textView2.setTextSize(16);
        textView2.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        //EditText Min
        EditText editText = new EditText(getContext());
        editText.setHint("0");
        editText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setKeyListener(DigitsKeyListener.getInstance(false,true));
        width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        editText.setLayoutParams(new LinearLayout.LayoutParams(
                (int) width,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        TextView textView3 = new TextView(getContext());
        textView3.setText("mg");
        textView3.setTextSize(16);
        textView3.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        //Space
        Space space = new Space(getContext());
        width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
        space.setLayoutParams(new LinearLayout.LayoutParams(
                (int)width,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        TextView textView4 = new TextView(getContext());
        textView4.setText("max.");
        textView4.setTextSize(16);
        textView4.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        //EditText Max
        EditText editText2 = new EditText(getContext());
        editText2.setHint("0");
        editText2.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText2.setKeyListener(DigitsKeyListener.getInstance(false,true));
        width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        editText2.setLayoutParams(new LinearLayout.LayoutParams(
                (int)width,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
        ));

        TextView textView5 = new TextView(getContext());
        textView5.setText("mg");
        textView5.setTextSize(16);
        textView5.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

// Füge alle Views dem LinearLayout hinzu
        linear.addView(textView1);
        linear.addView(textView2);
        linear.addView(editText);
        linear.addView(textView3);
        linear.addView(space);
        linear.addView(textView4);
        linear.addView(editText2);
        linear.addView(textView5);

        layout.addView(linear);
        linear.setVisibility(View.GONE);
        return linear;
    }
//------------------------------------------------------------------------------------------------------------------

//HashMaps: Creating/Updating-----------------------------------------------------------------------------------------------------------------------
    private void putValuesInHashmap() {
        //Name, Ingredients, Excluded Ingredients
        general.put("query", String.valueOf(name.getText()));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ingredient.getText());
        for(int i=0; i<ingredientEditList.size();i++){
            if(!String.valueOf(ingredientEditList.get(i).getText()).equals("")){
                stringBuilder.append(",").append(ingredientEditList.get(i).getText());
            }
        }
        general.put("includeIngredients", String.valueOf(stringBuilder));

        stringBuilder = new StringBuilder();
        stringBuilder.append(excludedIngredient.getText());
        for(int i=0; i<excludeIngredientEditList.size();i++){
            if(!String.valueOf(excludeIngredientEditList.get(i).getText()).equals("")){
                stringBuilder.append(",").append(excludeIngredientEditList.get(i).getText());
            }
        }
        general.put("excludeIngredients", String.valueOf(stringBuilder));

        //Nutrients
        putNutrientToHashmap(macroNutrientList, macronutrients);
        putNutrientToHashmap(microNutrientList, micronutrients);
        putNutrientToHashmap(vitaminList, vitamins);

    }

    private void putNutrientToHashmap(ArrayList<LinearLayout> linear, HashMap<String, String> map){
        for(int i = 0; i< linear.size(); i++){
            EditText editMin = (EditText) linear.get(i).getChildAt(2);
            EditText editMax = (EditText) linear.get(i).getChildAt(6);
            String stringMin = String.valueOf(editMin.getText());
            String stringMax = String.valueOf(editMax.getText());
            TextView nameView = (TextView) linear.get(i).getChildAt(0);
            String name = String.valueOf(nameView.getText());
            if(name.contains("Vitamin "))
                name=name.replace("Vitamin ", "");
            String nameMin = "minimum"+name;
            String nameMax = "maximum"+name;
            if(name.equals("Sat. Fat")){
                nameMin = "minimumSaturatedFat";
                nameMax = "maximumSaturatedFat";
            }

            Log.d("edit", name);
            Log.d("edit", stringMin);
            Log.d("edit", stringMax);
            if(!stringMin.isEmpty()&&!stringMin.equals("0"))
                map.put(nameMin, stringMin);
            if(!stringMax.isEmpty()&&!stringMax.equals("0"))
                map.put(nameMax, stringMax);
        }
    }


    private void createSearchHashmaps() {
        general = new HashMap<>();
        general.put("query", "");
        general.put("cuisine", "");
        general.put("excludeCuisine", "");
        general.put("diet", "");
        general.put("intolerances", "");
        general.put("includeIngredients", "");
        general.put("excludeIngredients", "");
        general.put("type", "");
        general.put("maxReadyTime", "");

        macronutrients = new HashMap<>();
        macronutrients.put("minimumCarbs", "-1.0");
        macronutrients.put("minimumProtein", "-1.0");
        macronutrients.put("minimumFat", "-1.0");
        macronutrients.put("minimumSaturatedFat", "-1.0");
        macronutrients.put("minimumFiber", "-1.0");
        macronutrients.put("minimumSugar", "-1.0");
        macronutrients.put("maximumCarbs", "-1.0");
        macronutrients.put("maximumProtein", "-1.0");
        macronutrients.put("maximumFat", "-1.0");
        macronutrients.put("maximumSaturatedFat", "-1.0");
        macronutrients.put("maximumFiber", "-1.0");
        macronutrients.put("maximumSugar", "-1.0");

        micronutrients = new HashMap<>();
        micronutrients.put("minimumCalcium", "-1.0");
        micronutrients.put("minimumCopper", "-1.0");
        micronutrients.put("minimumIron", "-1.0");
        micronutrients.put("minimumMagnesium", "-1.0");
        micronutrients.put("minimumManganese", "-1.0");
        micronutrients.put("minimumPhosphorus", "-1.0");
        micronutrients.put("minimumPotassium", "-1.0");
        micronutrients.put("minimumSelenium", "-1.0");
        micronutrients.put("minimumSodium", "-1.0");
        micronutrients.put("minimumZinc", "-1.0");
        micronutrients.put("minimumCholine", "-1.0");
        micronutrients.put("minimumFolate", "-1.0");
        micronutrients.put("minimumFolicAcid", "-1.0");
        micronutrients.put("minimumIodine", "-1.0");
        micronutrients.put("maximumCalcium", "-1.0");
        micronutrients.put("maximumCopper", "-1.0");
        micronutrients.put("maximumIron", "-1.0");
        micronutrients.put("maximumMagnesium", "-1.0");
        micronutrients.put("maximumManganese", "-1.0");
        micronutrients.put("maximumPhosphorus", "-1.0");
        micronutrients.put("maximumPotassium", "-1.0");
        micronutrients.put("maximumSelenium", "-1.0");
        micronutrients.put("maximumSodium", "-1.0");
        micronutrients.put("maximumZinc", "-1.0");
        micronutrients.put("maximumCholine", "-1.0");
        micronutrients.put("maximumFolate", "-1.0");
        micronutrients.put("maximumFolicAcid", "-1.0");
        micronutrients.put("maximumIodine", "-1.0");


        vitamins = new HashMap<>();
        vitamins.put("minimumA", "-1.0");
        vitamins.put("minimumC", "-1.0");
        vitamins.put("minimumD", "-1.0");
        vitamins.put("minimumE", "-1.0");
        vitamins.put("minimumK", "-1.0");
        vitamins.put("minimumB1", "-1.0");
        vitamins.put("minimumB2", "-1.0");
        vitamins.put("minimumB3", "-1.0");
        vitamins.put("minimumB5", "-1.0");
        vitamins.put("minimumB6", "-1.0");
        vitamins.put("minimumB12", "-1.0");
        vitamins.put("maximumA", "-1.0");
        vitamins.put("maximumC", "-1.0");
        vitamins.put("maximumD", "-1.0");
        vitamins.put("maximumE", "-1.0");
        vitamins.put("maximumK", "-1.0");
        vitamins.put("maximumB1", "-1.0");
        vitamins.put("maximumB2", "-1.0");
        vitamins.put("maximumB3", "-1.0");
        vitamins.put("maximumB5", "-1.0");
        vitamins.put("maximumB6", "-1.0");
        vitamins.put("maximumB12", "-1.0");


    }

    //------------------------------------------------------------------------------------------------------------------
}


/*


     SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
                     getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,searchResultsFragment).commit();
 */

