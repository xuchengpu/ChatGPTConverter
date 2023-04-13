package com.agora.gpt;

import com.blankj.utilcode.util.ThreadUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


class ChatGPTManager {
    private final String Tag=getClass().getSimpleName();
    private static final ChatGPTManager ourInstance = new ChatGPTManager();
    private ConvertListener listener;
    private boolean isInited=false;
    private static final String API_KEY =BuildConfig.chatgpt_api_key;
    private static final String API_BASE_URL = BuildConfig.chatgpt_api_base_url;
    private OkHttpClient client;
    MediaType mediaType = MediaType.parse("application/json");
    private RequestBodyBean requestBodyBean=new RequestBodyBean();

    static ChatGPTManager getInstance() {
        return ourInstance;
    }

    private ChatGPTManager() {
    }

    public void init(){
        if (isInited) {
            return;
        }
        isInited = true;
        client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .build();
        requestBodyBean.setMessages(new FixSizeList<>(10));

    }

    public void setChatGPTListener(ConvertListener listener){
        this.listener =listener;
    }

    public void sendQuestionToChatGPT(int index,String question)  {
        if(!isInited) {
            return;
        }
        ThreadUtils.getCachedPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String response = executeRequest(question);
                    if(listener!=null) {
                        listener.onQues2AnsSuccess(index,question,response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if(listener!=null) {
                        listener.onFailure(0,e.getMessage());
                    }
                }
            }
        });

    }

    private String executeRequest(String question) throws IOException {
        RequestBodyBean.Messages messages=new RequestBodyBean.Messages();
        messages.setRole("user");
        messages.setContent(question);
        requestBodyBean.getMessages().add(messages);
        String messagesBody = new Gson().toJson(requestBodyBean);
        RequestBody body = RequestBody.create(
                mediaType,
                messagesBody
                );
//        Log.e(Tag, "convert messagesBody="+messagesBody);

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
        ChatGPTBean result = new Gson().fromJson(responseBody, ChatGPTBean.class);
        RequestBodyBean.Messages messages_response=new RequestBodyBean.Messages();
        messages_response.setRole("assistant");
        messages_response.setContent(result.getAnswer());
        requestBodyBean.getMessages().add(messages_response);
        return result.getAnswer();
    }
}
