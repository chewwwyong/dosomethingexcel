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
    int tt=0;
    int t=0;
    int once=1;
    int once_2=1;

    DataFormatter fmt;

    int num_sheet = 1;
    int row_cell = 0;
    int num_cell = 1;

    ArrayList<String> Voice = new ArrayList<String>();
    ArrayList<String> Voive_clock = new ArrayList<String>();

    ArrayList<String> Image = new ArrayList<String>();
    ArrayList<String> Image_clock = new ArrayList<String>();

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txv2 = findViewById(R.id.txv2);

        soundPoolHelper = new SoundPoolHelper(4,SoundPoolHelper.TYPE_MUSIC)
                .setRingtoneType(SoundPoolHelper.RING_TYPE_MUSIC)
                //加载默认音频，因为上面指定了，所以其默认是：RING_TYPE_MUSIC
                //happy1,happy2
                .load(MainActivity.this,"OldMan_v1.mp3",R.raw.into)
                .load(MainActivity.this,"OldMan_v2.mp3",R.raw.operator)
                .load(MainActivity.this,"OldMan_v3.mp3",R.raw.demilitarized)
                .load(MainActivity.this,"Girl_v1.mp3",R.raw.danzon);
        soundPoolHelper.play("happy2",false);





        timer = new Timer();
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tt++;
                        t++;
                        txv.setText(tt+"s");


                        if(tt<=90){
                            // 获取excel文件流
                            is = getResources().openRawResource(R.raw.testing);
                            // 获取workbook对象
                            workbook = null;
                            try {
                                workbook = new XSSFWorkbook(is);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // 获取sheet对象
                            sheet = workbook.getSheetAt(0);
                            // 获取row对象
                            row = sheet.getRow(0);
                            sheet = workbook.getSheet(String.valueOf(row.getCell(0)));
                            setTitle(String.valueOf(row.getCell(0)));

                        }
                        else if(tt>90 && tt<140){

                            if(once!=0){
                                // 获取excel文件流
                                is = getResources().openRawResource(R.raw.testing);
                                // 获取workbook对象
                                workbook = null;
                                try {
                                    workbook = new XSSFWorkbook(is);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                // 获取sheet对象
                                sheet = workbook.getSheetAt(0);
                                // 获取row对象
                                row = sheet.getRow(0);
                                sheet = workbook.getSheet(String.valueOf(row.getCell(1)));
                                setTitle(String.valueOf(row.getCell(1)));


                                Toast.makeText(MainActivity.this,"已經切換到第二張",Toast.LENGTH_SHORT).show();
                                init();
                                t=0;
                                imgId= new int[]{R.drawable.review, R.drawable.end2};
                                imgname = new String[]{"review.jpg","end2.jpg"};

                                voiId = new int[]{R.raw.audio,R.raw.audio2};
                                voiname = new String[]{"audio.mp3","audio2.mp3"};
                                once=0;
                            }

                        }
                        else{

                            if(once_2!=0){
                                sheet = workbook.getSheet(String.valueOf(row.getCell(2)));
                                txv2.setText(String.valueOf(row.getCell(2)));
                                Toast.makeText(MainActivity.this,"已經切換到第三張",Toast.LENGTH_SHORT).show();
                                Intent it = new Intent(MainActivity.this,Question.class);
                                startActivity(it);
                            }
                            once_2=0;
                        }

                        for(int i=0;i<Voive_clock.size();i++){
                            switch(i)
                            {
                                case 0 :
                                    if(t==Integer.parseInt(Voive_clock.get(0))){
                                        for (String s : voiname) {
                                            if (s.equals(Voice.get(0))) {
                                                soundPoolHelper.play(voiname[0],false);
                                            }
                                        }
                                    }
                                    break;
                                case 1 :
                                    if(t==Integer.parseInt(Voive_clock.get(1))){
                                        for (String s : voiname) {
                                            if (s.equals(Voice.get(1))) {
                                                soundPoolHelper.play(voiname[1], false);
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    if(t==Integer.parseInt(Voive_clock.get(2))){
                                        for (String s : voiname) {
                                            if (s.equals(Voice.get(2))) {
                                                soundPoolHelper.play(voiname[2], false);
                                            }
                                        }
                                    }
                                    break;
                                case 3:
                                    if(t==Integer.parseInt(Voive_clock.get(3))){
                                        for (String s : voiname) {
                                            if (s.equals(Voice.get(3))) {
                                                soundPoolHelper.play(voiname[3], false);
                                            }
                                        }
                                    }
                                    break;
                                default :
                                    //要執行動作
                                    break;
                            }
                        }

                        for(int i=0;i<Voive_clock.size();i++){
                            switch(i)
                            {
                                case 0 :
                                    if(t==Integer.parseInt(Image_clock.get(0))){
                                        for (String s : imgname) {
                                            if (s.equals(Image.get(0))) {
                                                iv.setImageResource(imgId[0]);
                                            }
                                        }
                                    }
                                    break;
                                case 1 :
                                    if(t==Integer.parseInt(Image_clock.get(1))){
                                        for (String s : imgname) {
                                            if (s.equals(Image.get(1))) {
                                                iv.setImageResource(imgId[1]);
                                            }
                                        }
                                    }
                                    break;
                                case 2:
                                    if(t==Integer.parseInt(Image_clock.get(2))){
                                        for (String s : imgname) {
                                            if (s.equals(Image.get(2))) {
                                                iv.setImageResource(imgId[2]);
                                            }
                                        }
                                    }
                                    break;
                                case 3:
                                    if(t==Integer.parseInt(Image_clock.get(3))){
                                        for (String s : imgname) {
                                            if (s.equals(Image.get(3))) {
                                                iv.setImageResource(imgId[3]);
                                            }
                                        }
                                    }
                                    break;
                                default :
                                    //要執行動作
                                    break;
                            }
                        }

                    }
                });
            }
        };
        timer.schedule(task,100,100);

        txv = findViewById(R.id.textView);
        iv = (ImageView)this.findViewById(R.id.imageViewObj);

        // 获取excel文件流
        is = getResources().openRawResource(R.raw.testing);
        // 获取workbook对象
        workbook = null;
        try {
            workbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fmt = new DataFormatter();

        // 获取sheet对象
        sheet = workbook.getSheetAt(1);
        // 获取row对象
        row = sheet.getRow(0);
        // 获取cell对象
        Cell cell = row.getCell(0);

        //處理Voice
        row = sheet.getRow(0);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while(row_cell>0){
            if(String.valueOf(row.getCell(num_cell)).length()!=0){
                //Toast.makeText(this,String.valueOf(row.getCell(num_cell)),Toast.LENGTH_SHORT).show();
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                //String ssss = String.valueOf(row.getCell(num_cell));
                if(!ssss.trim().isEmpty()){
                    Voice.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }
        //Toast.makeText(MainActivity.this,String.valueOf(Voice.size()),Toast.LENGTH_SHORT).show();


        //處理Voice_clock
        row = sheet.getRow(1);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while(row_cell>0){
            if(String.valueOf(row.getCell(num_cell)).length()!=0){
                //Toast.makeText(this,String.valueOf(row.getCell(num_cell)),Toast.LENGTH_SHORT).show();
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                if(!ssss.trim().isEmpty()){
                    Voive_clock.add(ssss);
                }

                //Toast.makeText(this,String.valueOf(Image_clock.get(0)),Toast.LENGTH_SHORT).show();
                row_cell--;
            }
            num_cell++;
        }
        //Toast.makeText(MainActivity.this,"已經切換到第二張",Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this,String.valueOf(Voive_clock.size()),Toast.LENGTH_SHORT).show();

        //處理Image_clock
            row = sheet.getRow(3);
            row_cell = row.getPhysicalNumberOfCells();
            num_cell =1;
            while(row_cell>0){
                if(String.valueOf(row.getCell(num_cell)).length()!=0){
                    //Toast.makeText(this,String.valueOf(row.getCell(num_cell)),Toast.LENGTH_SHORT).show();
                    //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                    String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                    if(!ssss.trim().isEmpty()){
                        Image_clock.add(ssss);
                    }
                    //Toast.makeText(this,String.valueOf(Image_clock.get(0)),Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(this,String.valueOf(row.getCell(num_cell)),Toast.LENGTH_SHORT).show();
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                //String ssss = String.valueOf(row.getCell(num_cell));
                if(!ssss.trim().isEmpty()){
                    Image.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }
        //Toast.makeText(this,String.valueOf(Image.size()),Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,String.valueOf(row.getCell(8)),Toast.LENGTH_SHORT).show();
    //        numof_firstcol--;
     //  }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100){
            // 有選擇檔案
            if ( resultCode == RESULT_OK )
            {
                // 取得檔案的 Uri
                Uri uri = data.getData();
                if( uri != null )
                {

                    try{
                        InputStream in = getContentResolver().openInputStream(uri);
                        //將BufferedReader與FileReader做連結
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String readData = "";
                        String temp = br.readLine(); //readLine()讀取一整行
                        while (temp!=null){
                            if(temp.equals("apple")){
                                ImageView iv = (ImageView)this.findViewById(R.id.imageViewObj);
                                iv.setImageResource(R.mipmap.apple);
                            }
                            else if(temp.equals("banana")){
                                ImageView iv = (ImageView)this.findViewById(R.id.imageViewObj);
                                iv.setImageResource(R.mipmap.banana);
                            }

                            readData+=temp+="\n";
                            temp=br.readLine();

                        }
                        txv.setText(readData);
                        Toast.makeText(this,readData,Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPoolHelper.release();
    }
    public void init() {
        Voice = new ArrayList<String>();
        Voive_clock = new ArrayList<String>();
        Image = new ArrayList<String>();
        Image_clock = new ArrayList<String>();


        // 获取excel文件流
        is = getResources().openRawResource(R.raw.testing);
        // 获取workbook对象
        workbook = null;
        try {
            workbook = new XSSFWorkbook(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fmt = new DataFormatter();

        // 获取sheet对象
        sheet = workbook.getSheetAt(2);
        // 获取row对象
        row = sheet.getRow(0);
        // 获取cell对象
        Cell cell = row.getCell(0);

        //處理Voice
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while(row_cell>0){
            if(String.valueOf(row.getCell(num_cell)).length()!=0){
                //Toast.makeText(this,String.valueOf(row.getCell(num_cell)),Toast.LENGTH_SHORT).show();
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                //String ssss = String.valueOf(row.getCell(num_cell));
                if(!ssss.trim().isEmpty()){
                    Voice.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }
        //Toast.makeText(MainActivity.this,String.valueOf(Voice.size()),Toast.LENGTH_SHORT).show();


        //處理Voice_clock
        row = sheet.getRow(1);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell = 1;
        while(row_cell>0){
            if(String.valueOf(row.getCell(num_cell)).length()!=0){
                //Toast.makeText(this,String.valueOf(row.getCell(num_cell)),Toast.LENGTH_SHORT).show();
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                if(!ssss.trim().isEmpty()){
                    Voive_clock.add(ssss);
                }

                //Toast.makeText(this,String.valueOf(Image_clock.get(0)),Toast.LENGTH_SHORT).show();
                row_cell--;
            }
            num_cell++;
        }
        //Toast.makeText(MainActivity.this,"已經切換到第二張",Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this,String.valueOf(Voive_clock.size()),Toast.LENGTH_SHORT).show();

        //處理Image_clock
        row = sheet.getRow(3);
        row_cell = row.getPhysicalNumberOfCells();
        num_cell =1;
        while(row_cell>0){
            if(String.valueOf(row.getCell(num_cell)).length()!=0){
                //Toast.makeText(this,String.valueOf(row.getCell(num_cell)),Toast.LENGTH_SHORT).show();
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                if(!ssss.trim().isEmpty()){
                    Image_clock.add(ssss);
                }
                //Toast.makeText(this,String.valueOf(Image_clock.get(0)),Toast.LENGTH_SHORT).show();
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
                //Toast.makeText(this,String.valueOf(row.getCell(num_cell)),Toast.LENGTH_SHORT).show();
                //Excel中的數字是( 除了一些邊緣情況) 存儲為浮點數。 Java中的浮點數，當格式化為字元串時，會用一個尾隨的小數點列印
                String ssss = fmt.formatCellValue(row.getCell(num_cell ,Row.RETURN_BLANK_AS_NULL));
                //String ssss = String.valueOf(row.getCell(num_cell));
                if(!ssss.trim().isEmpty()){
                    Image.add(ssss);
                }
                row_cell--;
            }
            num_cell++;
        }
    }
}