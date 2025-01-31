package com.example.dime.pages;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dime.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Chatbot extends AppCompatActivity {


    private EditText messageEditText;
    private Button sendButton;
    private LinearLayout chatContainer;
    private RequestQueue requestQueue;

    // Replace with your Google Cloud API key (keep it secure)
    private static final String API_KEY = "AIzaSyAN1FZgYYgRlCf1sPp8bGDZ6B1w6PtJQ0w";
    private static final String MODEL_ID = "gemini-1.5-flash";
    private static final String API_ENDPOINT ="https://generativelanguage.googleapis.com/v1beta/models/" + MODEL_ID + ":generateContent?key=" + API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot2);

        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        chatContainer = findViewById(R.id.chat_container);
        requestQueue = Volley.newRequestQueue(this);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = messageEditText.getText().toString().trim();

                if (userMessage.isEmpty()) {
                    Toast.makeText(Chatbot.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Display user message
                displayMessage(userMessage, R.drawable.chat_bubble_user);

                // Create API request body
                try {
                    JSONObject part = new JSONObject();
                    part.put("text", userMessage);

                    JSONObject content = new JSONObject();
                    content.put("parts", new JSONArray().put(part));

                    JSONObject requestBody = new JSONObject();
                    requestBody.put("contents", new JSONArray().put(content));

                    sendMessageToAPI(requestBody);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Chatbot.this, "Error creating request", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendMessageToAPI(JSONObject requestBody) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                API_ENDPOINT,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("API_RESPONSE", response.toString());
                        try {
                            JSONArray candidates = response.getJSONArray("candidates");
                            JSONObject firstCandidate = candidates.getJSONObject(0);
                            JSONArray parts = firstCandidate.getJSONObject("content").getJSONArray("parts");
                            String aiResponse = parts.getJSONObject(0).getString("text");

                            runOnUiThread(() -> displayMessage(aiResponse, R.drawable.chat_bubble_chatbot));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Chatbot.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e("API_ERROR", "Response Code: " + error.networkResponse.statusCode);
                            Log.e("API_ERROR", "Response Data: " + new String(error.networkResponse.data));
                        }
                        Toast.makeText(Chatbot.this, "Network/API error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,  // Timeout in ms
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

    @SuppressLint("ResourceAsColor")
    private void displayMessage(String message, int backgroundResource) {
        LinearLayout messageLayout = new LinearLayout(this);
        messageLayout.setOrientation(LinearLayout.VERTICAL);
        messageLayout.setBackgroundResource(backgroundResource);
        messageLayout.setPadding(16, 16, 16, 16);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 30, 0, 0);
        messageLayout.setLayoutParams(layoutParams);

        TextView messageTextView = new TextView(this);
        messageTextView.setText(message);
        messageTextView.setTextSize(18);
        messageTextView.setTextColor(getResources().getColor(android.R.color.black));

        messageLayout.addView(messageTextView);
        chatContainer.addView(messageLayout);
    }
}
