package com.troubleshooters.diu.phrm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import io.paperdb.Paper;

public class AddMealActivity extends AppCompatActivity {


    ListView listView_food;
    SearchView searchView;
    ArrayAdapter<String> adapter;
    String[] bnFoodSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Paper.init(this);

        setContentView(R.layout.activity_add_meal);
        bnFoodSearch = getResources().getStringArray(R.array.food_search);

        listView_food=(ListView)findViewById(R.id.list_view_food);
        searchView=(SearchView)findViewById(R.id.search_view);
        ArrayList<String> food_suggestion=new ArrayList<>();
        food_suggestion.addAll(Arrays.asList(getResources().getStringArray(R.array.bn_food_search)));
        adapter=new ArrayAdapter<String>(AddMealActivity.this,android.R.layout.simple_list_item_1,food_suggestion);
        listView_food.setAdapter(adapter);
        searchView.setOnQueryTextListener(
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            }
        );

        listView_food.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String lang = Paper.book().read("language");
                    String name;
                    if(lang != "en"){
                        name = bnFoodSearch[position];
                    }
                    else{
                        name=(listView_food.getItemAtPosition(position)).toString();
                    }
                    getDataFromFireBase(name);
                }
            }
        );
    }

    private void getDataFromFireBase(final String name)
    {
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference("nutrition");
        ref.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snap:dataSnapshot.getChildren())
                    {

                        if(snap.getKey().equals(name.toLowerCase()))
                        {

                            String calorie=snap.child("calorie").getValue().toString();
                            String fat=snap.child("fat").getValue().toString();
                            String protein=snap.child("protein").getValue().toString();
                            String carbohydrate=snap.child("carbohydrate").getValue().toString();


                            SharedPreferences sharedPreferences=getSharedPreferences("nutrition", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            float gained_calorie=sharedPreferences.getFloat("gained_calorie",0.0f);
                            float gained_fat=sharedPreferences.getFloat("gained_fat",0.0f);
                            float gained_carbohydrate=sharedPreferences.getFloat("gained_carbohydrate",0.0f);
                            float gained_protein=sharedPreferences.getFloat("gained_protein",0.0f);

                            //Double value=0.0;
                            float value=0.0f;
                            if(gained_calorie==0)
                            {
                                value=Float.parseFloat(calorie);
                                editor.putFloat("gained_calorie",value);
                                editor.commit();
                                value=0.0f;
                            }
                            else
                            {
                                value=(gained_calorie+Float.parseFloat(calorie));
                                editor.putFloat("gained_calorie",value);
                                editor.commit();
                                value=0.0f;
                            }

                            if(gained_fat==0)
                            {
                                value=Float.parseFloat(fat);
                                editor.putFloat("gained_fat",value);
                                editor.commit();
                                value=0.0f;
                            }
                            else
                            {
                                value=(gained_fat+Float.parseFloat(fat));
                                editor.putFloat("gained_fat",value);
                                editor.commit();
                                value=0.0f;
                            }
                            if(gained_carbohydrate==0)
                            {
                                value=Float.parseFloat(carbohydrate);
                                editor.putFloat("gained_carbohydrate",value);
                                editor.commit();
                                value=0.0f;
                            }
                            else
                            {
                                value=(gained_carbohydrate+Float.parseFloat(carbohydrate));
                                editor.putFloat("gained_carbohydrate",value);
                                editor.commit();
                                value=0.0f;
                            }
                            if (gained_protein==0)
                            {
                                value=Float.parseFloat(protein);
                                editor.putFloat("gained_protein",value);
                                editor.commit();
                                value=0.0f;
                            }
                            else
                            {
                                //String s=new DecimalFormat("##.#").format(value);
                                value=(gained_protein+Float.parseFloat(protein));
                                editor.putFloat("gained_protein",value);
                                editor.commit();
                                value=0.0f;
                            }

                            NutritionActivity.nutrition.finish();
                            Intent intent=new Intent(AddMealActivity.this,NutritionActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Error","loadPost:onCancelled",databaseError.toException());

                }
            }
        );
    }

}
