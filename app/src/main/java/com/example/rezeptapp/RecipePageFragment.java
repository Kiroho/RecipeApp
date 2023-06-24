package com.example.rezeptapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipePageFragment extends Fragment {
    String recipeID ="";
    Recipe recipe = new Recipe();
    DBHandler dbHandler;

    ImageView recipeImage;
    LinearLayout ingredientLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null){
            recipeID = bundle.getString("foundRecipe");
            Log.d("result5", "Recipe ID: "+ recipeID);
        }
        /*try {
            SearchManager searchmanager = new SearchManager();
            ArrayList<Recipe> recipeList = searchmanager.getRandomRecipe(0);
            recipe = recipeList.get(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        recipe.setId(10);
        recipe.setTitle("Recipe Number Ten");
        recipe.setImage("Recipelink.url");
        recipe.setFavorite(false);
        ArrayList<Recipe.ExtendedIngredient> ingrList = new ArrayList<>();
        for(int i=0; i<7; i++){
            Recipe.ExtendedIngredient ingr = new Recipe.ExtendedIngredient();
            ingr.setId(i);
            ingr.setName("Ingredient" + i);
            ingr.setAmount(i+1);
            ingr.setUnit("g");
            ingrList.add(ingr);
        }

        recipe.setExtendedIngredients(ingrList);

        dbHandler = new DBHandler(getContext());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_page, container, false);

        ingredientLayout = view.findViewById(R.id.RecipeIngredientsLayout);
        setUpIngredients();

        recipeImage = view.findViewById(R.id.bigRecipeImage);
        Picasso.get().load("https://spoonacular.com/recipeImages/638174-312x231.jpg").into(recipeImage);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(recipe.getTitle());

        //Options Menu
        requireActivity().addMenuProvider(new MenuProvider() {

              @Override
              public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                  menuInflater.inflate(R.menu.recipe_menu, menu);

                  //menu.findItem(R.id.favoriteItem).setVisible(false);
              }
              @Override
              public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                  if (menuItem.getItemId() == R.id.recipeSaveItem) {
                      int id = recipe.getId();
                      String title = recipe.getTitle();
                      String image = recipe.getImage();
                      boolean favorite = recipe.isFavorite();

                      if(id==0){
                          Snackbar.make(getView(), "Error!\nRecipe can't be saved", Snackbar.LENGTH_SHORT).show();
                          return true;
                      }

                      if(dbHandler.addNewShortInfo(id, title, image, favorite)){
                      //if(dbHandler.addNewShortInfo(5, "Title", "image.url", false)){
                          Snackbar.make(getView(), "Recipe saved", Snackbar.LENGTH_SHORT).show();
                      }
                      else{

                          Snackbar.make(getView(), "Error!\nRecipe can't be saved", Snackbar.LENGTH_SHORT).show();
                      }

                  }
                  else if(menuItem.getItemId() == R.id.favoriteItem){
                      boolean newFavoriteState;
                      if(recipe.isFavorite()){
                          newFavoriteState=false;
                      }
                      else{
                          newFavoriteState=true;
                      }
                      recipe.setFavorite(newFavoriteState);

                      if(dbHandler.updateShortInfoFavorite(recipe.getId(), newFavoriteState)){
                          if(newFavoriteState){
                              menuItem.setIcon(R.drawable.favorite_on);
                              Snackbar.make(getView(), "Favorite Set", Snackbar.LENGTH_SHORT).show();
                          }
                          else{
                              menuItem.setIcon(R.drawable.favorite_off);
                              Snackbar.make(getView(), "Favorite Removed", Snackbar.LENGTH_SHORT).show();
                          }

                      }

                      //menuItem.setVisible(false);
                      //Testausgabe
                      Log.d("load5","Looaaadd---------");
                      ArrayList<ShortInfo> shortInfoList = dbHandler.getAllShortInfo();
                      Log.d("load5", String.valueOf(shortInfoList.size()));
                      for(int i=0; i<shortInfoList.size();i++){
                          Log.d("load5", String.valueOf(shortInfoList.get(i).getId()));
                          Log.d("load5",shortInfoList.get(i).getTitle());
                          Log.d("load5",shortInfoList.get(i).getImage());
                          Log.d("load5", String.valueOf(shortInfoList.get(i).isFavorite()));
                      }
                  }
                  else if(menuItem.getItemId() == R.id.recipeUpdateItem){
                      if(dbHandler.updateShortInfo(5,"newTitle", "newImage.url", true)){
                          Snackbar.make(getView(), "Recipe Updated", Snackbar.LENGTH_SHORT).show();
                      }
                      else{
                          Snackbar.make(getView(), "Error!\nRecipe not updated", Snackbar.LENGTH_SHORT).show();
                      }
                  }
                  else if(menuItem.getItemId() == R.id.recipeDeleteItem){
                      if(dbHandler.deleteShortInfo(5)){
                          Snackbar.make(getView(), "Recipe Deleted", Snackbar.LENGTH_SHORT).show();
                      }
                      else{
                          Snackbar.make(getView(), "Error!\nRecipe not deleted", Snackbar.LENGTH_SHORT).show();
                      }
                  }
                  return true;
              }
          }, getViewLifecycleOwner()
        );


        // Inflate the layout for this fragment
        return view;
    }

    private void setUpIngredients(){
        ArrayList<Recipe.ExtendedIngredient> ingredientList = recipe.getExtendedIngredients();
        if(ingredientList!=null){
            for(int i=0; i<ingredientList.size();i++){
                ingredientLayout.addView(
                        createIngredientLine(ingredientList.get(i).getName(),
                            String.valueOf(ingredientList.get(i).getAmount()),
                            ingredientList.get(i).getUnit())
                );
            }
        }

    }

    private LinearLayout createIngredientLine(String ingredient, String measure, String unit){
        int sizeInDP =0;
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        sizeInDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        linearLayout.setPadding(0,sizeInDP,0,sizeInDP);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

    // First TextView (RecipeIngredient)
        TextView recipeIngredientTextView = new TextView(getContext());
        recipeIngredientTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        recipeIngredientTextView.setGravity(Gravity.START);
        sizeInDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
        int sizeInDP2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        recipeIngredientTextView.setPadding(sizeInDP, 0, 0, sizeInDP2);
        recipeIngredientTextView.setText(ingredient);

    // Second TextView (RecipeMeasure)
        TextView recipeMeasureTextView = new TextView(getContext());
        recipeMeasureTextView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
        recipeMeasureTextView.setGravity(Gravity.END);
        sizeInDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
        recipeMeasureTextView.setPadding(0, 0, sizeInDP, sizeInDP2);
        recipeMeasureTextView.setText(measure + " " + unit);

        // Füge die TextViews dem LinearLayout hinzu
        linearLayout.addView(recipeIngredientTextView);
        linearLayout.addView(recipeMeasureTextView);

        //ingredientLayout.addView(linearLayout,1);
        return linearLayout;
    }


}