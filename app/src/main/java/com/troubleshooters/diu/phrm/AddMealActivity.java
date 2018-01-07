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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class AddMealActivity extends AppCompatActivity {


    ListView listView_food;
    SearchView searchView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);

        listView_food=(ListView)findViewById(R.id.list_view_food);
        searchView=(SearchView)findViewById(R.id.search_view);
        ArrayList<String> food_suggestion=new ArrayList<>();
        food_suggestion.addAll(Arrays.asList(getResources().getStringArray(R.array.food_search)));
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

                        String name=(listView_food.getItemAtPosition(position)).toString();

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
                                String gained_calorie=sharedPreferences.getString("gained_calorie","");
                                String gained_fat=sharedPreferences.getString("gained_fat","");
                                String gained_carbohydrate=sharedPreferences.getString("gained_carbohydrate","");
                                String gained_protein=sharedPreferences.getString("gained_protein","");

                                Double value=0.0;
                                if(gained_calorie.equals(""))
                                {
                                    value=Double.parseDouble(calorie);
                                    String s=new DecimalFormat("##.#").format(value);
                                    editor.putString("gained_calorie",s);
                                    editor.commit();
                                    value=0.0;
                                }
                                else
                                {
                                    value=(Double.parseDouble(gained_calorie)+Double.parseDouble(calorie));
                                    String s=new DecimalFormat("##.#").format(value);
                                    editor.putString("gained_calorie",value.toString());
                                    editor.commit();
                                    value=0.0;
                                }

                                if(gained_fat.equals(""))
                                {
                                    value=Double.parseDouble(fat);
                                    String s=new DecimalFormat("##.#").format(value);
                                    editor.putString("gained_fat",s);
                                    value=0.0;
                                    editor.commit();
                                }
                                else
                                {
                                    value=(Double.parseDouble(gained_fat)+Double.parseDouble(fat));
                                    String s=new DecimalFormat("##.#").format(value);
                                    editor.putString("gained_fat",s);
                                    value=0.0;
                                    editor.commit();
                                }
                                if(gained_carbohydrate.equals(""))
                                {
                                    value=Double.parseDouble(carbohydrate);
                                    String s=new DecimalFormat("##.#").format(value);
                                    editor.putString("gained_carbohydrate",s);
                                    value=0.0;
                                    editor.commit();
                                }
                                else
                                {
                                    value=Double.parseDouble(gained_carbohydrate)+Double.parseDouble(carbohydrate);
                                    String s=new DecimalFormat("##.#").format(value);
                                    editor.putString("gained_carbohydrate",s);
                                    value=0.0;
                                    editor.commit();
                                }
                                if (gained_protein.equals(""))
                                {
                                    value=Double.parseDouble(protein);
                                    String s=new DecimalFormat("##.#").format(value);
                                    editor.putString("gained_protein",s);
                                    value=0.0;
                                    editor.commit();
                                }
                                else
                                {
                                    value=(Double.parseDouble(gained_protein)+Double.parseDouble(protein));
                                    String s=new DecimalFormat("##.#").format(value);
                                    editor.putString("gained_protein",s);
                                    value=0.0;
                                    editor.commit();
                                }
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
