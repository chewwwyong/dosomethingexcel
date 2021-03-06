package com.example.ExcelFileReader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventCallback;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Ending extends AppCompatActivity  implements TextToSpeech.OnInitListener{

    private static final long FACE_MOUTH_SPEED = 200;//set larger value for slower mouth speed

    private NuwaRobotAPI mRobotAPI;
    private IClientId mClientId;

    private final String MOTION_SAMPLE = "987_EN_HappyBirthdayWaltz";
    private TextView mTexPlayStatus;

    int current_sheet = 0;
    int all_sheet = 0;

    ImageView iv;

    Row row;
    Sheet sheet;
    InputStream is;
    XSSFWorkbook workbook;

    int score = 0;
    int start = 0;
    int end = 0;

    private TextToSpeech textToSpeech; // TTS对象

    private SoundPoolHelper soundPoolHelper;

    //Video
    private VideoView videoView;
    private MediaController mediaController;

    //timer
    Timer timer;
    int time_Total = 0;
    int time_now = 0;

    //one time
    int once = 1;

    DataFormatter fmt;

    int row_cell = 0;
    int num_cell = 1;

    ArrayList<String> Face = new ArrayList<>();
    ArrayList<String> Face_clock = new ArrayList<>();

    ArrayList<String> Video = new ArrayList<>();
    ArrayList<String> Video_clock = new ArrayList<>();

    ArrayList<String> Voice = new ArrayList<>();
    ArrayList<String> Voice_clock = new ArrayList<>();

    ArrayList<String> Image = new ArrayList<>();
    ArrayList<String> Image_clock = new ArrayList<>();

    ArrayList<String> Subtitle = new ArrayList<>();
    ArrayList<String> Subtitle_clock = new ArrayList<>();

    ArrayList<String> Motion = new ArrayList<>();
    ArrayList<String> Motion_clock = new ArrayList<>();

    ArrayList<String> Question = new ArrayList<>();
    ArrayList<String> Motion_R = new ArrayList<>();

    ArrayList<String> Type = new ArrayList<>();
    ArrayList<String> Text = new ArrayList<>();
    String nowchoose = "";
    ArrayList<String> Picture = new ArrayList<>();
    ArrayList<String> Answer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
        Intent it = Ending.this.getIntent();
        score = it.getIntExtra("score",2);
        current_sheet = it.getIntExtra("current_sheet",2);

        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);
        mRobotAPI.registerRobotEventListener(robotEventCallback); //listen callback of robot service event

        timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    time_Total++;
                    time_now++;

                    if((time_now <=25)){
                        if (once != 0) {
                            setexcel();
                            initValue();
                            sheet = workbook.getSheet(String.valueOf(row.getCell(current_sheet)));
                            setTitle(String.valueOf(row.getCell(current_sheet)));
                            loadSoundFile();
                            switch (score){
                                case 0:
                                    start = 0;
                                    end = 2;
                                    break;
                                case 1:
                                    start = 3;
                                    end = 5;
                                    break;
                                case 2:
                                    start = 6;
                                    end = 8;
                                    break;
                                case 3:
                                    start = 9;
                                    end = 11;
                                    break;
                                case 4:
                                    start = 12;
                                    end = 14;
                                    break;
                                case 5:
                                    start = 15;
                                    end = 17;
                                    break;
                                case 6:
                                    start = 18;
                                    end = 20;
                                    break;
                                case 7:
                                    start = 21;
                                    end = 23;
                                    break;
                            }
                                deal_Section(sheet);
                                once = 0;
                                current_sheet++;
                        }
                    }

                    if(time_now > 25){
                        Toast.makeText(Ending.this,"結束了",Toast.LENGTH_SHORT).show();
                        videoView.setVisibility(View.INVISIBLE);
                        videoView = findViewById(R.id.videoView);
                        videoView.suspend();
                        iv.setVisibility(View.INVISIBLE);
                    }
                            /*if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                                // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                                textToSpeech.setPitch(1.0f);
                                //设定语速 ，默认1.0正常语速
                                textToSpeech.setSpeechRate(1.0f);
                                //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
                                textToSpeech.speak("I AM", TextToSpeech.QUEUE_FLUSH, null);
                            }*/

                    for (int i = start; i <= end; i++) {
                        if (!Voice_clock.isEmpty() && time_now == Integer.parseInt(Voice_clock.get(i))) {
                            String str = Voice.get(i).substring(0, Voice.get(i).indexOf("."));
                            soundPoolHelper.play(str, false);
                        }
                    }

                    for (int i = start; i <= end; i++) {
                        if (!Video_clock.isEmpty() && time_now == Integer.parseInt(Video_clock.get(i))) {
                            play_video(Video.get(i));
                        }
                    }

                    for (int i = start; i <= end; i++) {
                        if (time_now == Integer.parseInt(Image_clock.get(start/3))) {

                            videoView = findViewById(R.id.videoView);
                            videoView.setVisibility(View.INVISIBLE);
                            videoView.suspend();

                            String str = Image.get(start/3).substring(0, Image.get(start/3).indexOf("."));
                            int Id = getResources().getIdentifier(str,  "drawable", getPackageName());
                            String uri = "android.resource://" + getPackageName() + "/" + Id;
                            iv.setImageURI(Uri.parse(uri));
                        }
                    }
                    for (int i = start; i <= end; i++) {
                        if (!Subtitle_clock.isEmpty() && time_now == Integer.parseInt(Subtitle_clock.get(i))) {
                            mRobotAPI.startTTS(Subtitle.get(i));
                            //showface(Subtitle.get(i));
                        }
                    }
                    for (int i = start; i <= end; i++) {
                        if (!Motion_clock.isEmpty() && time_now == Integer.parseInt(Motion_clock.get(i)) && !Motion.isEmpty()) {
                            //Step 2 : Execute "Play motion"
                            mRobotAPI.motionPlay(Motion.get(i), true);
                        }
                    }
                });
            }
        };
        //幾秒做一次(單位：毫秒)
        timer.schedule(task, 1000, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPoolHelper.release();
    }

    public void initValue() {
        Face = new ArrayList<>();
        Face_clock = new ArrayList<>();

        Video = new ArrayList<>();
        Video_clock = new ArrayList<>();

        Voice = new ArrayList<>();
        Voice_clock = new ArrayList<>();

        Image = new ArrayList<>();
        Image_clock = new ArrayList<>();

        Subtitle = new ArrayList<>();
        Subtitle_clock = new ArrayList<>();

        Motion = new ArrayList<>();
        Motion_clock = new ArrayList<>();

        Question = new ArrayList<>();
        Motion_R = new ArrayList<>();

        Type = new ArrayList<>();
        Text = new ArrayList<>();
    }

    public void deal_Section(Sheet sheet) {

        //處理Face
        row = sheet.getRow(0);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Face.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Face_clock
        row = sheet.getRow(1);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Face_clock.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Voice
        row = sheet.getRow(2);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Voice.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }


        //處理Voice_clock
        row = sheet.getRow(3);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Voice_clock.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Video
        row = sheet.getRow(4);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Video.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }


        //處理Video_clock
        row = sheet.getRow(5);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Video_clock.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Image
        row = sheet.getRow(6);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Image.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Image_clock
        row = sheet.getRow(7);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Image_clock.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Subtitle
        row = sheet.getRow(10);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Subtitle.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Subtitle_clock
        row = sheet.getRow(11);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Subtitle_clock.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Motion
        row = sheet.getRow(12);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Motion.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Motion_clock
        row = sheet.getRow(13);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while (row_cell > 0) {
            if (String.valueOf(row.getCell(num_cell)).length() != 0) {
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell, Row.RETURN_BLANK_AS_NULL));
                if (!ssss.trim().isEmpty()) {
                    Motion_clock.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }
    }

    public void setexcel() {
        // 取得excel文件流
        is = getResources().openRawResource(R.raw.blessing_09182020_revised);
        // 取得workbook對象
        workbook = null;
        try {
            workbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 取得sheet對象
        all_sheet = workbook.getNumberOfSheets();
        //Toast.makeText(MainActivity.this,String.valueOf(all_sheet),Toast.LENGTH_SHORT).show();
        sheet = workbook.getSheetAt(0);
        // 取得row對象
        row = sheet.getRow(0);
        fmt = new DataFormatter();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }


    protected void onStop() {
        super.onStop();
        //textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        //textToSpeech.shutdown(); // 关闭，释放资源

        //Step 4 : Release robotAPI before closing activity
        if(mRobotAPI != null){
            mRobotAPI.release();
        }
    }

    private void initView() {

        mTexPlayStatus = findViewById(R.id.play_status);

        videoView = findViewById(R.id.videoView);
        iv = findViewById(R.id.iv);
      //  textToSpeech = new TextToSpeech(Ending.this, Ending.this); // 参数Context,TextToSpeech.OnInitListener

        soundPoolHelper = new SoundPoolHelper(30, SoundPoolHelper.TYPE_MUSIC)
                .setRingtoneType(SoundPoolHelper.RING_TYPE_MUSIC);
    }


    private void play_video(String str) {
        iv.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.VISIBLE);
        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        str = str.substring(0,str.indexOf("."));
        int rawId = getResources().getIdentifier(str,  "raw", getPackageName());
        String uri = "android.resource://" + getPackageName() + "/" + rawId;
        videoView.setVideoURI(Uri.parse(uri));
        //videoView.setMediaController(mediaController);
        //mediaController.setMediaPlayer(videoView);
        //videoView.requestFocus();
        videoView.start();

        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("API123", "What " + what + " extra " + extra);
                return false;
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVisibility(View.INVISIBLE);
                iv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showEventMsg(String status){
        runOnUiThread(()->{
            //mTexPlayStatus.append(status);
            //mTexPlayStatus.append("\n");
            // Log.d(TAG, status);
        });

    }

    private RobotEventCallback robotEventCallback = new RobotEventCallback() {
        @Override
        public void onStartOfMotionPlay(String s) {
            showEventMsg("Start Playing Motion...");
        }

        @Override
        public void onStopOfMotionPlay(String s) {
            showEventMsg("Stop Playing Motion...");
        }

        @Override
        public void onCompleteOfMotionPlay(String s) {
            showEventMsg("Play Motion Complete!!!");

            //Step 3 : If (the parameter of motionPlay)auto_fadein is ture,
            // the transparent view must be closed after motion is complete, error or other case.
            if(mRobotAPI != null){
                mRobotAPI.hideWindow(true);
            }
        }

        @Override
        public void onPlayBackOfMotionPlay(String s) {
            showEventMsg("Playing Motion...");
        }

        @Override
        public void onErrorOfMotionPlay(int i) {
            showEventMsg("When playing Motion, error happen!!! error code: " + i);

            if(mRobotAPI != null){
                mRobotAPI.hideWindow(true);
            }
        }
    };


    private void showface(String tts) {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().showUnity();//lunch face
            mRobotAPI.startTTS(tts);//speak a TTS
            mouthOn(FACE_MOUTH_SPEED);//starting mouth on animation
        }
    }

    private void hideface() {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().hideUnity();//hide face
        }
    }

    private void mouthOn(long speed) {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().mouthOn(speed);
        }
    }

    private void mouthOff() {
        if (mRobotAPI != null) {
            mRobotAPI.UnityFaceManager().mouthOff();
        }
    }
    private void loadSoundFile(){
        for (int i = 0; i < Voice_clock.size(); i++) {
            String str = Voice.get(i).substring(0, Voice.get(i).indexOf("."));
            int Id = getResources().getIdentifier(str,  "raw", getPackageName());
            soundPoolHelper.load(Ending.this, str,  Id);
            try {
                Thread.sleep(50);// 給予初始化音樂文件足夠時間
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}