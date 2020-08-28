package com.example.myapplication;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    TextView txv;
    TextView txv2;

    Row row;
    Sheet sheet;
    InputStream is;
    XSSFWorkbook workbook;

    ImageView iv;
    int[] imgId={R.drawable.under_bridge,R.drawable.photo1,R.drawable.photo2,R.drawable.photo3};
    String[] imgname = {"under_bridge.jpg","photo1.jpg","photo2.jpg","photo3.jpg"};

    int[] voiId={R.raw.into,R.raw.operator,R.raw.demilitarized,R.raw.danzon};
    String[] voiname = {"OldMan_v1.mp3","OldMan_v2.mp3","OldMan_v3.mp3","Girl_v1.mp3"};

    private SoundPoolHelper soundPoolHelper;

    //timer
    Timer timer;
    int time_Total=0;
    int time_now=0;
    //one time
    int once=1;
    int once_1=1;
    int once_2=1;

    DataFormatter fmt;

    int row_cell = 0;
    int num_cell = 1;

    String filename="";

    ArrayList<String> Voice = new ArrayList<>();
    ArrayList<String> Voice_clock = new ArrayList<>();

    ArrayList<String> Image = new ArrayList<>();
    ArrayList<String> Image_clock = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txv = findViewById(R.id.txv);
        txv2 = findViewById(R.id.txv2);
        iv = (ImageView)this.findViewById(R.id.imageViewObj);

        soundPoolHelper = new SoundPoolHelper(6,SoundPoolHelper.TYPE_MUSIC)
                .setRingtoneType(SoundPoolHelper.RING_TYPE_MUSIC)
                .load(MainActivity.this,"OldMan_v1.mp3",R.raw.into)
                .load(MainActivity.this,"OldMan_v2.mp3",R.raw.operator)
                .load(MainActivity.this,"OldMan_v3.mp3",R.raw.demilitarized)
                .load(MainActivity.this,"Girl_v1.mp3",R.raw.danzon)
                .load(MainActivity.this,"audio.mp3",R.raw.audio)
                .load(MainActivity.this,"audio2.mp3",R.raw.audio2);

        timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time_Total++;
                        time_now++;

                        if(time_Total<=180){
                            txv.setText("Time："+time_Total+"s / 180s");
                            if(once!=0) {
                                setexcel();
                                initValue();
                                sheet = workbook.getSheet(String.valueOf(row.getCell(0)));
                                setTitle(String.valueOf(row.getCell(0)));

                                imgId = new int[]{R.drawable.under_bridge,R.drawable.photo1,R.drawable.photo2,R.drawable.photo3};
                                imgname = new String[]{"under_bridge.jpg","photo1.jpg","photo2.jpg","photo3.jpg"};

                                voiId = new int[]{R.raw.into,R.raw.operator,R.raw.demilitarized,R.raw.danzon};
                                voiname = new String[]{"OldMan_v1.mp3","OldMan_v2.mp3","OldMan_v3.mp3","Girl_v1.mp3"};

                                deal(sheet);
                                once=0;
                            }
                        }
                        else if(time_Total>180 && time_Total<360){
                            txv.setText("Time："+time_now+"s / 180s");
                            if(once_1!=0){
                                setexcel();
                                initValue();
                                time_now=0;

                                sheet = workbook.getSheet(String.valueOf(row.getCell(1)));
                                setTitle(String.valueOf(row.getCell(1)));

                                Toast.makeText(MainActivity.this,"已經切換到第二張",Toast.LENGTH_SHORT).show();

                                imgId= new int[]{R.drawable.review, R.drawable.end2};
                                imgname = new String[]{"review.jpg","end2.jpg"};

                                voiId = new int[]{R.raw.audio,R.raw.audio2};
                                voiname = new String[]{"audio.mp3","audio2.mp3"};


                                deal(sheet);
                                once_1=0;
                            }
                        }
                        else {
                            if (once_2 != 0) {
                                sheet = workbook.getSheet(String.valueOf(row.getCell(2)));
                                txv2.setText(String.valueOf(row.getCell(2)));
                                Toast.makeText(MainActivity.this, "已經切換到第三張", Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(MainActivity.this, Question.class);
                                startActivity(it);
                                once_2 = 0;
                            }
                        }

                        for(int i=0;i<Voice_clock.size();i++){
                            switch(i)
                            {
                                case 0 :
                                    if(time_now==Integer.parseInt(Voice_clock.get(0))){
                                        for (String s : voiname) {
                                            if (s.equals(Voice.get(0))) {
                                                soundPoolHelper.play(voiname[0],false);
                                                Toast.makeText(MainActivity.this,voiname[0],Toast.LENGTH_SHORT).show();
                                                filename+=Voice.get(0)+"  --> ";
                                                txv2.setText(filename);
                                            }
                                        }
                                    }
                                    break;
                                case 1 :
                                    if(time_now==Integer.parseInt(Voice_clock.get(1))){
                                        for (String s : voiname) {
                                            if (s.equals(Voice.get(1))) {
                                                soundPoolHelper.play(voiname[1], false);
                                                filename+=Voice.get(1)+"  --> ";
                                                txv2.setText(filename);
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    if(time_now==Integer.parseInt(Voice_clock.get(2))){
                                        for (String s : voiname) {
                                            if (s.equals(Voice.get(2))) {
                                                soundPoolHelper.play(voiname[2], false);
                                                filename+=Voice.get(2)+"  --> ";
                                                txv2.setText(filename);
                                            }
                                        }
                                    }
                                    break;
                                case 3:
                                    if(time_now==Integer.parseInt(Voice_clock.get(3))){
                                        for (String s : voiname) {
                                            if (s.equals(Voice.get(3))) {
                                                soundPoolHelper.play(voiname[3], false);
                                                filename+=Voice.get(3)+"  --> ";
                                                txv2.setText(filename);
                                            }
                                        }
                                    }
                                    break;
                                default :
                                    break;
                            }
                        }

                        for(int i=0;i<Image_clock.size();i++){
                            switch(i)
                            {
                                case 0 :
                                    if(time_now==Integer.parseInt(Image_clock.get(0))){
                                        for (String s : imgname) {
                                            if (s.equals(Image.get(0))) {
                                                iv.setImageResource(imgId[0]);
                                                filename+=Image.get(0)+"  --> ";
                                                txv2.setText(filename);
                                            }
                                        }
                                    }
                                    break;
                                case 1 :
                                    if(time_now==Integer.parseInt(Image_clock.get(1))){
                                        for (String s : imgname) {
                                            if (s.equals(Image.get(1))) {
                                                iv.setImageResource(imgId[1]);
                                                filename+=Image.get(1)+"  --> ";
                                                txv2.setText(filename);
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    if(time_now==Integer.parseInt(Image_clock.get(2))){
                                        for (String s : imgname) {
                                            if (s.equals(Image.get(2))) {
                                                iv.setImageResource(imgId[2]);
                                                filename+=Image.get(2)+"  --> ";
                                                txv2.setText(filename);
                                            }
                                        }
                                    }
                                    break;
                                case 3:
                                    if(time_now==Integer.parseInt(Image_clock.get(3))){
                                        for (String s : imgname) {
                                            if (s.equals(Image.get(3))) {
                                                iv.setImageResource(imgId[3]);
                                                filename+=Image.get(3)+"  --> ";
                                                txv2.setText(filename);
                                            }
                                        }
                                    }
                                    break;
                                default :
                                    break;
                            }
                        }
                    }
                });
            }
        };
        //幾秒做一次(單位：毫秒)
        timer.schedule(task,500,500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPoolHelper.release();
    }
    public void initValue() {
        Voice = new ArrayList<>();
        Voice_clock = new ArrayList<>();
        Image = new ArrayList<>();
        Image_clock = new ArrayList<>();
    }

    public void deal(Sheet sheet){
        //處理Voice
        row = sheet.getRow(0);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while(row_cell>0){
            if(String.valueOf(row.getCell(num_cell)).length()!=0){
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                if(!ssss.trim().isEmpty()){
                    Voice.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }


        //處理Voice_clock
        row = sheet.getRow(1);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while(row_cell>0){
            if(String.valueOf(row.getCell(num_cell)).length()!=0){
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                if(!ssss.trim().isEmpty()){
                    Voice_clock.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Image
        row = sheet.getRow(2);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while(row_cell>0){
            if(String.valueOf(row.getCell(num_cell)).length()!=0){
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                if(!ssss.trim().isEmpty()){
                    Image.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }

        //處理Image_clock
        row = sheet.getRow(3);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell =1;
        while(row_cell>0){
            if(String.valueOf(row.getCell(num_cell)).length()!=0){
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                if(!ssss.trim().isEmpty()){
                    Image_clock.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }
    }

    public void setexcel(){
        // 取得excel文件流
        is = getResources().openRawResource(R.raw.testing);
        // 取得workbook對象
        workbook = null;
        try {
            workbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 取得sheet對象
        sheet = workbook.getSheetAt(0);
        // 取得row對象
        row = sheet.getRow(0);
        fmt = new DataFormatter();
    }
}