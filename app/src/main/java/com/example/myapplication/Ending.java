package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Ending extends AppCompatActivity {

    private static final long FACE_MOUTH_SPEED = 200;//set larger value for slower mouth speed

    private NuwaRobotAPI mRobotAPI;
    private IClientId mClientId;

    private final String MOTION_SAMPLE = "987_EN_HappyBirthdayWaltz";
    private TextView mTexPlayStatus;

    int current_sheet = 0;
    int all_sheet = 0;

    TextView txvA,txvB,txvC,txvD,title;
    TextView txv;
    TextView txv2;
    TextView subtitle;
    TextView motion;
    TextView face;

    Row row;
    Sheet sheet;
    InputStream is;
    XSSFWorkbook workbook;

    private TextToSpeech textToSpeech; // TTS对象

    private SoundPoolHelper soundPoolHelper;

    //Video
    private VideoView videoView;
    private Button btn_start, btn_end;
    private MediaController mediaController;
    int index_video = 0;

    //timer
    Timer timer;
    int time_Total = 0;
    int time_now = 0;

    //one time
    int once = 1;
    Boolean stop = false;

    DataFormatter fmt;

    int row_cell = 0;
    int num_cell = 1;

    String filename = "";

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

        initView();


        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);
        mRobotAPI.registerRobotEventListener(robotEventCallback); //listen callback of robot service event

        Button btn = findViewById(R.id.btn_playmotion);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTexPlayStatus.setText("");

                //Step 2 : Execute "Play motion"
                mRobotAPI.motionPlay(MOTION_SAMPLE, true);
            }
        });

        timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    time_Total++;
                    time_now++;

                    if((time_now <=180) && (current_sheet <= all_sheet  && !stop)){

                        if (once != 0) {
                            setexcel();
                            initValue();
                            sheet = workbook.getSheet(String.valueOf(row.getCell(current_sheet)));
                            setTitle(String.valueOf(row.getCell(current_sheet)));
                            String is_Section = String.valueOf(row.getCell(current_sheet)).substring(0,7);
                            String is_Review = String.valueOf(row.getCell(current_sheet)).substring(0,6);
                            //Toast.makeText(MainActivity.this,is_Review,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MainActivity.this,is_Section,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(MainActivity.this,is_Review,Toast.LENGTH_SHORT).show();
                            if(is_Section.equals("Section")){
                                deal_Section(sheet);
                                once = 0;
                                current_sheet++;
                            }
                            else if(is_Review.equals("Review")){
                                stop = true;
                                Intent it = new Intent(Ending.this,Review.class);
                                it.putExtra("current_sheet",current_sheet);
                                it.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(it);
                            }
                        }
                    }

                    if(time_now > 180 && !stop){
                        time_now %= 180;
                        once = 1;
                    }
                            /*if (textToSpeech != null && !textToSpeech.isSpeaking()) {
                                // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                                textToSpeech.setPitch(1.0f);
                                //设定语速 ，默认1.0正常语速
                                textToSpeech.setSpeechRate(1.0f);
                                //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
                                textToSpeech.speak("I AM", TextToSpeech.QUEUE_FLUSH, null);
                            }*/

                    for (int i = 0; i < Voice_clock.size(); i++) {
                        if (time_now == Integer.parseInt(Voice_clock.get(i))) {
                           // soundPoolHelper.play(voiname[i], false);
                            filename += Voice.get(i) + "  --> ";
                            txv2.setText(filename);
                        }
                    }

                    for (int i = 0; i < Face_clock.size(); i++) {
                        if (time_now == Integer.parseInt(Face_clock.get(i))) {
                            face.setText(Face.get(i));
                            filename += Face.get(i) + "  --> ";
                            txv2.setText(filename);
                        }
                    }

                    for (int i = 0; i < Video_clock.size(); i++) {
                        if (time_now == Integer.parseInt(Video_clock.get(i))) {
                            play_video(Video.get(i));
                            filename += Video.get(i) + "  --> ";
                            txv2.setText(filename);
                        }
                    }

                    for (int i = 0; i < Image_clock.size(); i++) {
                        if (time_now == Integer.parseInt(Image_clock.get(i))) {
                           // iv.setImageResource(imgId[i]);
                            filename += Image.get(i) + "  --> ";
                            txv2.setText(filename);
                        }
                    }
                    for (int i = 0; i < Subtitle_clock.size(); i++) {
                        if (time_now == Integer.parseInt(Subtitle_clock.get(i))) {
                            subtitle.setText(Subtitle.get(i));
                            //mRobotAPI.startTTS(Subtitle.get(i));
                            mRobotAPI.startTTS(Subtitle.get(i));
                            //showface(Subtitle.get(i));
                        }
                    }
                    for (int i = 0; i < Motion_clock.size(); i++) {
                        if (time_now == Integer.parseInt(Motion_clock.get(i))) {
                            motion.setText(Motion.get(i));
                            filename += Motion.get(i) + "  --> ";
                            txv2.setText(filename);
                            mTexPlayStatus.setText("");

                            //Step 2 : Execute "Play motion"
                            //mRobotAPI.motionPlay(Motion.get(i), true);
                        }
                    }
                });
            }
        };
        //幾秒做一次(單位：毫秒)
        timer.schedule(task, 25, 25);

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

    /*@Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }*/

    protected void onStop() {
        super.onStop();
        textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        textToSpeech.shutdown(); // 关闭，释放资源


        //Step 4 : Release robotAPI before closing activity
        if(mRobotAPI != null){
            mRobotAPI.release();
        }
    }

    private void initView() {

        mTexPlayStatus = findViewById(R.id.play_status);

        txv = findViewById(R.id.time);
        txv2 = findViewById(R.id.fileflow);
        subtitle = findViewById(R.id.subtitle);
        motion = findViewById(R.id.motion);
        face = findViewById(R.id.face);

        videoView = findViewById(R.id.videoView);

        textToSpeech = new TextToSpeech(Ending.this, (TextToSpeech.OnInitListener) Ending.this); // 参数Context,TextToSpeech.OnInitListener



        soundPoolHelper = new SoundPoolHelper(6, SoundPoolHelper.TYPE_MUSIC)
                .setRingtoneType(SoundPoolHelper.RING_TYPE_MUSIC)
                .load(Ending.this, "OldMan_v1.mp3", R.raw.into);

    }


    private void play_video(String str) {
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

    }

    private void showEventMsg(String status){
        runOnUiThread(()->{
            mTexPlayStatus.append(status);
            mTexPlayStatus.append("\n");
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
}