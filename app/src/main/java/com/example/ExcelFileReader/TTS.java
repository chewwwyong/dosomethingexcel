package com.example.ExcelFileReader;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TTS extends AppCompatActivity implements  TextToSpeech.OnInitListener{
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTextToSpeech();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    speakText("语音设备检测结果正常。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {

            // setLanguage设置语言
            int result = tts.setLanguage(Locale.CHINA);
            int result2 = tts.setLanguage(Locale.CHINESE);
            Log.e("tts：", "语言设置结果："+result+":"+result2);
            // TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失
            // TextToSpeech.LANG_NOT_SUPPORTED：不支持
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }else {
                //不支持中文就将语言设置为英文
                tts.setLanguage(Locale.US);
            }
        }
    }

    private void initTextToSpeech() {
        // 参数Context,TextToSpeech.OnInitListener
        tts = new TextToSpeech(this, this);
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        tts.setPitch(1.0f);
        // 设置语速
//        tts.setSpeechRate(0.5f);

    }
    public void speakText(final String text) {

        Log.e("资源文件路径：", "准备朗读" );
        // validate
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(TTS.this, "请您输入要朗读的文字", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO validate success, do something
        if (tts != null && !tts.isSpeaking()) {
            /*
                TextToSpeech的speak方法有两个重载。
                // 执行朗读的方法
                speak(CharSequence text,int queueMode,Bundle params,String utteranceId);
                // 将朗读的的声音记录成音频文件
                synthesizeToFile(CharSequence text,Bundle params,File file,String utteranceId);
                第二个参数queueMode用于指定发音队列模式，两种模式选择
                （1）TextToSpeech.QUEUE_FLUSH：该模式下在有新任务时候会清除当前语音任务，执行新的语音任务
                （2）TextToSpeech.QUEUE_ADD：该模式下会把新的语音任务放到语音任务之后，
                等前面的语音任务执行完了才会执行新的语音任务
             */
            Log.e("资源文件路径：", "开始朗读");

            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 不管是否正在朗读TTS都被打断
        tts.stop();
        // 关闭，释放资源
        tts.shutdown();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        super.onDestroy();
    }

}
