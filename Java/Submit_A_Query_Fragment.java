package in.kartikhasija.bloodease;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kartik on 27-Jun-16.
 */
public class Submit_A_Query_Fragment extends Fragment implements View.OnClickListener {

    TextView textView_subject,textView_message;
    Button button_submit;
    String subject,message,status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_submit_a_query,container,false);

        textView_subject = (TextView) view.findViewById(R.id.subject_submit_a_query);
        textView_message = (TextView) view.findViewById(R.id.message_submit_a_query);
        button_submit = (Button) view.findViewById(R.id.submit_submit_a_query);

        button_submit.setOnClickListener(this);
        return view;
    }



    @Override
    public void onClick(View v) {
        subject=textView_subject.getText().toString();
        message=textView_message.getText().toString();
        message = message.replaceAll(" ", "_").toLowerCase();
        if(subject.equals("") || message.equals("")){
            Toast.makeText(getActivity(), "Please enter Subject and Message Too", Toast.LENGTH_LONG).show();
        }
        else{
            String url="http://kartikhasija.in/bloodease/saq.php?subject="+subject+"&message="+message+"&user_id="+Log_In.user_id;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        status = response.getString("status");

                        if (status.equals("done")) {
                            Toast.makeText(getActivity(), "Query Submit Successfully \n We will catch you as soon as possible", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getActivity(), status, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "Internal Server Error ", Toast.LENGTH_LONG).show();
                }
            });


            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            queue.add(request);

        }
    }
}
