package com.example.examination_portal.Examee;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.examination_portal.R;
import com.example.examination_portal.adapter.EQuestionAdapter;
import com.example.examination_portal.adapter.GroupAdapter;
import com.example.examination_portal.model.Group;
import com.example.examination_portal.model.Property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EGroupActivity extends AppCompatActivity {
//    LAYOUT VARIABLES
    RecyclerView egarv;

    private String URL_GROUPLISTING = Property.domain+"showAllGroups.php";

    GroupAdapter groupAdapter;
    private List<Group> groupList = new ArrayList<>();

    //    SHAREDPREFERENCE
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egroup);

//        get shared preference
        sharedPreferences = getApplicationContext().getSharedPreferences("UserPref",0);

        hideActionBar();
        getAllGroups();
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void getAllGroups() {
        groupList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GROUPLISTING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Log.e("groupActivity",jsonObject.toString());
                        int groupID = jsonObject.getInt("group_id");
                        String groupName = jsonObject.getString("group_name");
                        groupList.add(new Group(groupID,groupName));
                    }
                    initialization();
                    eventsManagement();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("loginActivity", error.getMessage().toString());
//                loading.setVisibility(View.GONE);
            }
        });
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> params = new HashMap<>();
//                params.put("group_email",sharedPreferences.getString(Property.user_email,null));
//                return params;
//            }
//        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void eventsManagement() {
    }

    private void initialization() {
//        egarv = findViewById(R.id.egarv);
//        eqabtnSubmit = findViewById(R.id.eqabtnSubmit);

        egarv = findViewById(R.id.egarv);
//        Log.e("GroupActivity","size - "+this.groupList.size());
        groupAdapter = new GroupAdapter(groupList,this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        egarv.setLayoutManager(mLayoutManager);
        egarv.setItemAnimator(new DefaultItemAnimator());
        egarv.setAdapter(groupAdapter);

    }
}
