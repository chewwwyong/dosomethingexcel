package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nuwarobotics.lib.voice.ifly.model.answer.Answer;
import com.nuwarobotics.service.IClientId;
import com.nuwarobotics.service.agent.NuwaRobotAPI;
import com.nuwarobotics.service.agent.RobotEventCallback;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Review extends AppCompatActivity {

    private NuwaRobotAPI mRobotAPI;
    private IClientId mClientId;

    TextView qA,qB,qC,qD,title;
    ImageView p1,p2,p3,p4;

    Row row;
    Sheet sheet;
    InputStream is;
    XSSFWorkbook workbook;

    int current_sheet = 0;
    int all_sheet = 0;
    int num= 0 ;
    int times=0;
    int score = 0;

    DataFormatter fmt;

    ArrayList<String> Question = new ArrayList<>();
    ArrayList<String> Motion = new ArrayList<>();

    ArrayList<String> Type = new ArrayList<>();
    ArrayList<String> Text = new ArrayList<>();
    String nowchoose = "Z";
    String ans;
    ArrayList<String> Picture = new ArrayList<>();
    ArrayList<String> Answer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //Step 1 : Initial Nuwa API Object
        mClientId = new IClientId(this.getPackageName());
        mRobotAPI = new NuwaRobotAPI(this, mClientId);
        mRobotAPI.registerRobotEventListener(robotEventCallback); //listen callback of robot service event

        qA = findViewById(R.id.qA);
        qB = findViewById(R.id.qB);
        qC = findViewById(R.id.qC);
        qD = findViewById(R.id.qD);
        title = findViewById(R.id.title);

        p1 = findViewById(R.id.p1);
        p2 = findViewById(R.id.p2);
        p3 = findViewById(R.id.p3);
        p4 = findViewById(R.id.p4);

        qA.setOnClickListener(v -> {
            nowchoose = "A";
            if(ans.equals(nowchoose)){
                Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                times--;
                score++;
                exe();
            }
            else{
                Toast.makeText(Review.this,"答錯了",Toast.LENGTH_SHORT).show();
                times--;
                exe();
            }
        });

        qB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nowchoose = "B";
                if(ans.equals(nowchoose)){
                    times--;
                    score++;
                    exe();
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(Review.this,"答錯了",Toast.LENGTH_SHORT).show();
                    times--;
                    exe();
                }
            }
        });

        qC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "C";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    score++;
                    times--;
                    exe();
                }
                else{
                    Toast.makeText(Review.this,"答錯了",Toast.LENGTH_SHORT).show();
                    times--;
                    exe();
                }
            }
        });

        qD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "D";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    score++;
                    times--;
                    exe();
                }
                else{
                    Toast.makeText(Review.this,"答錯了",Toast.LENGTH_SHORT).show();
                    times--;
                    exe();
                }
            }
        });

        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "A";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    score++;
                    times--;
                    exe();
                }
                else{
                    Toast.makeText(Review.this,"答錯了",Toast.LENGTH_SHORT).show();
                    times--;
                    exe();
                }
            }
        });

        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "B";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    score++;
                    times--;
                    exe();
                }
                else{
                    Toast.makeText(Review.this,"答錯了",Toast.LENGTH_SHORT).show();
                    times--;
                    exe();
                }
            }
        });

        p3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "C";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    score++;
                    times--;
                    exe();
                }
                else{
                    Toast.makeText(Review.this,"答錯了",Toast.LENGTH_SHORT).show();
                    times--;
                    exe();
                }
            }
        });

        p4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowchoose = "D";
                if(ans.equals(nowchoose)){
                    Toast.makeText(Review.this,"答對了",Toast.LENGTH_SHORT).show();
                    score++;
                    exe();
                }
                else{
                    Toast.makeText(Review.this,"答錯了",Toast.LENGTH_SHORT).show();
                    times--;
                    exe();
                }
            }
        });

        Intent it = Review.this.getIntent();
        current_sheet = it.getIntExtra("current_sheet",2);
        setexcel();
        sheet = workbook.getSheet(String.valueOf(row.getCell(current_sheet)));
        times = sheet.getPhysicalNumberOfRows();
        deal_Review(sheet);
        exe();


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

    public void deal_Review(Sheet sheet){

        //處理Question
        int numrow = 1;
        while(numrow <= (sheet.getPhysicalNumberOfRows()- 1) ){
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String ssss = fmt.formatCellValue(row.getCell(0, Row.RETURN_BLANK_AS_NULL));
            if (!ssss.trim().isEmpty()) {
                Question.add(ssss);
                // Toast.makeText(MainActivity.this,Question.get(numrow-1),Toast.LENGTH_SHORT).show();
                numrow++;
            }
        }

        //處理Motion
        numrow = 1;
        while(numrow <= sheet.getPhysicalNumberOfRows() -1 ){
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String ssss = fmt.formatCellValue(row.getCell(1, Row.RETURN_BLANK_AS_NULL));
            if (!ssss.trim().isEmpty()) {
                Motion.add(ssss);
                //   Toast.makeText(MainActivity.this,Question.get(numrow-1),Toast.LENGTH_SHORT).show();
                numrow++;
            }
        }

        //處理Type
        numrow = 1;
        while(numrow <= sheet.getPhysicalNumberOfRows() -1 ){
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String ssss = fmt.formatCellValue(row.getCell(3, Row.RETURN_BLANK_AS_NULL));
            if (!ssss.trim().isEmpty()) {
                Type.add(ssss);
                //   Toast.makeText(MainActivity.this,Question.get(numrow-1),Toast.LENGTH_SHORT).show();
                numrow++;
            }
        }

        //處理Text
        numrow = 1;
        while(numrow <= sheet.getPhysicalNumberOfRows() -1  ) {
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String s1 = fmt.formatCellValue(row.getCell(4, Row.RETURN_BLANK_AS_NULL));
            String s2 = fmt.formatCellValue(row.getCell(5, Row.RETURN_BLANK_AS_NULL));
            String s3 = fmt.formatCellValue(row.getCell(6, Row.RETURN_BLANK_AS_NULL));
            String s4 = fmt.formatCellValue(row.getCell(7, Row.RETURN_BLANK_AS_NULL));

            if(!s1.isEmpty()){
                Text.add(s1);
                Text.add(s2);
                Text.add(s3);
                Text.add(s4);
            }


            // Toast.makeText(MainActivity.this,Question.get(numrow-1),Toast.LENGTH_SHORT).show();
            numrow++;
        }

        //處理Picture
        numrow = 1;
        while(numrow <= sheet.getPhysicalNumberOfRows() -1 ) {
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String s1 = fmt.formatCellValue(row.getCell(8, Row.RETURN_BLANK_AS_NULL));
            String s2 = fmt.formatCellValue(row.getCell(9, Row.RETURN_BLANK_AS_NULL));
            String s3 = fmt.formatCellValue(row.getCell(10, Row.RETURN_BLANK_AS_NULL));
            String s4 = fmt.formatCellValue(row.getCell(11, Row.RETURN_BLANK_AS_NULL));

            if(!s1.isEmpty()){
                String str1 = s1.substring(0, s1.indexOf("."));
                String str2 = s2.substring(0, s2.indexOf("."));
                String str3 = s3.substring(0, s3.indexOf("."));
                String str4 = s4.substring(0, s4.indexOf("."));
                int Id1 = getResources().getIdentifier(str1,  "drawable", getPackageName());
                String uri1 = "android.resource://" + getPackageName() + "/" + Id1;

                int Id2 = getResources().getIdentifier(str2,  "drawable", getPackageName());
                String uri2 = "android.resource://" + getPackageName() + "/" + Id2;

                int Id3 = getResources().getIdentifier(str3,  "drawable", getPackageName());
                String uri3 = "android.resource://" + getPackageName() + "/" + Id3;

                int Id4 = getResources().getIdentifier(str4,  "drawable", getPackageName());
                String uri4 = "android.resource://" + getPackageName() + "/" + Id4;

                Picture.add(uri1);
                Picture.add(uri2);
                Picture.add(uri3);
                Picture.add(uri4);
            }


            numrow++;
        }

        //處理Answer
        numrow = 1;
        while(numrow <= sheet.getPhysicalNumberOfRows() -1 ){
            row = sheet.getRow(numrow);
            //Toast.makeText(MainActivity.this,String.valueOf(row.getCell(0)),Toast.LENGTH_SHORT).show();
            String ssss = fmt.formatCellValue(row.getCell(12, Row.RETURN_BLANK_AS_NULL));
            if (!ssss.trim().isEmpty()) {
                Answer.add(ssss);
                //   Toast.makeText(MainActivity.this,Question.get(numrow-1),Toast.LENGTH_SHORT).show();
                numrow++;
            }
        }
    }

    public void exe(){
        //num = 0;
        if(times>1){
            if(Type.get(num).equals("Text")){
                mRobotAPI.motionPlay(Motion.get(num), true);
                ans = Answer.get(num);
                initview();
                title.setText(Question.get(num));
                qA.setText(Text.get(num));
                qB.setText(Text.get(num+1));
                qC.setText(Text.get(num+2));
                qD.setText(Text.get(num+3));
                num++;

            }

            else if (Type.get(num).equals("Picture")){
                mRobotAPI.motionPlay(Motion.get(num), true);
                ans = Answer.get(num);
                //Toast.makeText(Review.this,"123",Toast.LENGTH_SHORT).show();
                title.setText(Question.get(num));

                initview();

                p1.setVisibility(View.VISIBLE);
                p2.setVisibility(View.VISIBLE);
                p3.setVisibility(View.VISIBLE);
                p4.setVisibility(View.VISIBLE);

                p1.setImageURI(Uri.parse(Picture.get(num)));
                p2.setImageURI(Uri.parse(Picture.get(num+1)));
                p3.setImageURI(Uri.parse(Picture.get(num+2)));
                p4.setImageURI(Uri.parse(Picture.get(num+3)));

                num++;

            }
        }
        else{
            Toast.makeText(Review.this,String.valueOf(score),Toast.LENGTH_SHORT).show();
            Intent it = new Intent(Review.this,Ending.class);
            it.putExtra("score",score);
            it.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(it);
        }


    }

    public void initview(){
        title.setText("");
        qA.setText("");
        qB.setText("");
        qC.setText("");
        qD.setText("");

        p1.setVisibility(View.INVISIBLE);
        p2.setVisibility(View.INVISIBLE);
        p3.setVisibility(View.INVISIBLE);
        p4.setVisibility(View.INVISIBLE);

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
    private void showEventMsg(String status){
        runOnUiThread(()->{
            //mTexPlayStatus.append(status);
           // mTexPlayStatus.append("\n");
            // Log.d(TAG, status);
        });

    }
}