package com.xcp.testchatgpt;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.agora.gpt.ConvertListener;
import com.agora.gpt.ConvertClient;

public class TestGPTActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gptactivity);

        ConvertClient.getInstance().init(this);
        ConvertClient.getInstance().setChatGPTListener(new MyConverListener());
        ConvertClient.getInstance().startVoice2Text();
//        ConvertClient.getInstance().startText2Voice("欢迎体验chat-gpt");
    }

    static class MyConverListener implements ConvertListener {
        private final String TAG = getClass().getSimpleName();

        @Override
        public void onVoice2Text(int index,String text) {
            //语音转文字回调结果
//            Log.i(TAG, "onVoice2Text: text=" + text);
        }

        @Override
        public void onText2Voice(int index,String pcmFilePath) {
            //文字转语音回调结果
            Log.i(TAG, "onText2Voice: pcmFilePath=" + pcmFilePath);
        }

        @Override
        public void onQues2AnsSuccess(int index,String question, String answer) {
            //问chatGPT回调结果
            Log.i(TAG, "question=" + question + ",answer=" + answer);
        }

        @Override
        public void onFailure(int errorCode, String message) {
            //各种错误回调
            Log.i(TAG, "onFailure ,message=" + message);
        }
    }
}