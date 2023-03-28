package com.xcp.testchatgpt;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.xcp.testchatgpt.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mbinding;
    private static final String API_KEY = BuildConfig.chatgpt_api_key;
    private static final String API_BASE_URL = BuildConfig.chatgpt_api_base_url;

    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mbinding.btnSend.setOnClickListener(this);
        mbinding.btnClear.setOnClickListener(this);
        client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .build();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                new Thread() {
                    @Override
                    public void run() {
                        String text = mbinding.edtInput.getText().toString().trim();
                        try {
                            String response = sendQuestionToChatGPT(text);
                            setText(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                            setText(e.getMessage());
                        }
                    }
                }.start();

                Toast.makeText(MainActivity.this, "已发送问题，请耐心等待回复", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_clear:
                mbinding.edtInput.setText("");
                mbinding.tvResponse.setText("");
                break;
        }

    }

    private void setText(String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "responseBody="+response);
                mbinding.tvResponse.setText(response);
            }
        });
    }

    public String sendQuestionToChatGPT(String prompt) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");

        RequestBody body = RequestBody.create(
                mediaType,
                "{\n" +
                        "    \"questions\":[ \"" + prompt + "\"]\n" +
//                        "    \"temperature\": 0.5,\n" +
//                        "    \"max_tokens\": 1000,\n" +
//                        "    \"n\": 1,\n" +
//                        "    \"stop\": null,\n" +
//                        "    \"model\": \"text-davinci-003\"\n" +
                        "}");

        Request request = new Request.Builder()

                .url(API_BASE_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("agora-service-key", API_KEY)
                .build();

        Response response = client.newCall(request)
                .execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected response code: " + response);
        }

        String responseBody = response.body().string();
        return responseBody;
//        return responseBody.substring(
//                responseBody.indexOf("\"text\": \"") + 9,
//                responseBody.indexOf("\"", responseBody.indexOf("\"text\": \"") + 10)
//        );
    }

}