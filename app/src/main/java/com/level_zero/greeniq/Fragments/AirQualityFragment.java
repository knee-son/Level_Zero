package com.level_zero.greeniq.Fragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.level_zero.greeniq.AirQuality.AirQualityActivity;
import com.level_zero.greeniq.R;
import com.level_zero.greeniq.databinding.FragmentAirQualityBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class AirQualityFragment extends Fragment {

    private FragmentAirQualityBinding binding;
    private TextView AQI, nearestCityTv;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAirQualityBinding.inflate(inflater, container, false);
        AQI = binding.tv;
        nearestCityTv = binding.nearestCityTv;
        Button button = binding.button4;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get(v);
            }
        });
        return binding.getRoot();
    }

    public void get(View v){
        String apikey="28b92be4-41a2-4905-983f-280e20cef31a";
        String url="https://api.airvisual.com/v2/nearest_city?key=28b92be4-41a2-4905-983f-280e20cef31a";
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject data = response.getJSONObject("data");
                    String city = data.getString("city");
                    nearestCityTv.setText(city);

                    JSONObject current = data.getJSONObject("current");
                    JSONObject pollution = current.getJSONObject("pollution");
                    int aqius = pollution.getInt("aqius");
                    AQI.setText(String.valueOf(aqius));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}