package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static java.lang.Float.parseFloat;

public class MainActivity extends Activity {

    private static final int REQ_CODE_SELECT_IMAGE = 100;
    @RequiresApi(api = Build.VERSION_CODES.M)

    ImageView imageView1;
    Button btn_save_photo;
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Uri image_uri;
    String Subject; // 다음 과목을 넣기 위해.
    public String downString; //받은 문자열 저장

    PieChart pieChart;
    private TextView downText;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView1 = findViewById(R.id.imageView1);
        btn_save_photo = findViewById(R.id.photograph);
        Button Send_To_Jsp = (Button) findViewById(R.id.send_photo);
        downText = (TextView) findViewById(R.id.downText);

        /* ======================파이차트 부분==================== */
        pieChart = (PieChart)findViewById(R.id.piechart);

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(60f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        /* 값 조절 */
        String v1, v2, v3 ,v4 ,v5 ,v6, v7, v8, v9, v10, v11, v12; //값

        /* 문자열 저장 변수 */

        v1 = "34f"; v2 = "30f"; v3 = "15f";
        v4 = "40f"; v5 = "50f"; v6 = "70f";
        v7 = "34f"; v8 = "30f"; v9 = "15f";
        v10 = "40f"; v11 = "50f"; v12 = "70f";

        /* 리스트에 값 삽입 */
        yValues.add(new PieEntry(parseFloat(v1),"10대 남"));
        yValues.add(new PieEntry(parseFloat(v2),"20대 남"));
        yValues.add(new PieEntry(parseFloat(v3),"30대 남"));
        yValues.add(new PieEntry(parseFloat(v4),"40대 남"));
        yValues.add(new PieEntry(parseFloat(v5),"50대 남"));
        yValues.add(new PieEntry(parseFloat(v6),"60대 남"));
        yValues.add(new PieEntry(parseFloat(v7),"10대 여"));
        yValues.add(new PieEntry(parseFloat(v8),"20대 여"));
        yValues.add(new PieEntry(parseFloat(v9),"30대 여"));
        yValues.add(new PieEntry(parseFloat(v10),"40대 여"));
        yValues.add(new PieEntry(parseFloat(v11),"50대 여"));
        yValues.add(new PieEntry(parseFloat(v12),"60대 여"));


        Description description = new Description();
        description.setText("Test Label"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);


        PieDataSet dataSet = new PieDataSet(yValues,"Test values");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(1f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
        /*================================================================ */

        Send_To_Jsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        btn_save_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){// 빌드 버젼을 체크하고
                    if(checkSelfPermission(Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==  // 다음에 인터넷에 사용 허가를 받는것을 추가 하였다.~~~~~~~~ 변경 된거....
                                    PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
                        String [] permission ={Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        open_camera();
                    }
                }
                else{
                    open_camera();
                }
            }
        });
    }

    private void tokenizer(){ //토큰 분리 함수
        StringTokenizer token1 = new StringTokenizer(downString , "o");
        while(token1.hasMoreTokens()) {
            System.out.println(token1.nextToken() + " ");
        }
    }

    private void open_camera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NEW PICTURE");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE); // 다음 결과 값을 받고 새로운 액티비티를 생성하기 위해 다음과 같은걸 쓴다.
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    open_camera();
                }
                else{
                    Toast.makeText(this,"저장소와 카메라를 허용승인이 필요합니다.",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            imageView1.setImageURI(image_uri); // 비트맵 객체를 이용해서 보여줌.
            Toast.makeText(this,"사진을 저장하였습니다.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"사진이 찍히지 않았습니다.",Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getBaseContext(), "resultCode : " + data, Toast.LENGTH_SHORT).show();
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String img_path=null;
                    img_path = getImagePathToUri(data.getData()); //이미지의 URI를 얻어 경로값으로 반환.
                    String subject = Subject;
                    //아래 함수가 사진을 보내기위한 함수,
                    DoTextUpload("http://rbghoneroom402.iptime.org:48526/JSP/Test.jsp", subject); // 해당 함수를 통해 텍스트를 전송한다. 다음 url로 다음 subject의 값을 넘긴다.
                    DoFileUpload("http://rbghoneroom402.iptime.org:48526/JSP/Test.jsp", img_path);  //해당 함수를 통해 이미지 전송.  경로를 설정. 다음 JSP로 보내기위한.


                    //===================위의 논리 이유 ==========
                    //    과목을 먼저 선정을 하여 다음
                    //
                    //
                    //===================위의 놀리 이유 ===========
                    //DoStringUpload(subject); // 과목을 가져온다.
                    //=====================11===========11===========11==========11==========11=================================
                    /*
                    String sendmsg = "vision_write";
                    String result = "값"; //자신이 보내고싶은 값을 보내시면됩니다
                    try{
                        String rst = new Task(sendmsg).execute(result,"vision_write").get();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                     */
                    Toast.makeText(getBaseContext(), "img_path : " + img_path, Toast.LENGTH_SHORT).show(); //다음 이미지 저장공간 경로를 보여줌.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    int reWidth = (int) (getWindowManager().getDefaultDisplay().getWidth());
                    int reHeight = (int) (getWindowManager().getDefaultDisplay().getHeight());

                    Bitmap image_bitmap_copy = Bitmap.createScaledBitmap(image_bitmap, 400, 300, true); // 사이즈를 조절
                    ImageView image = (ImageView) findViewById(R.id.imageView1);  //이미지를 띄울 위젯 ID값, 조절된 이미지가 띄워짐.
                    image.setImageBitmap(image_bitmap_copy); // image에 다음 비트맵의 이미지가 씌워짐.

                } catch (Exception e) {
                    e.printStackTrace(); // 에러 메시지를 발생시키게 하는 문장. 근원지를 찾아 단계별로 찾게하는 문장.
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data); // super는 부모에게 있는 변수를 사용하기 위해 있는 선언자이다.
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT) // 다음 버전에서 사용할 수 있다는 뜻.
    public String HttpURLConnection(String urlString, String params, String fileName) {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            FileInputStream mFileInputStream = new FileInputStream(new File(fileName));
            URL connectUrl = new URL(urlString);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream);
            // HttpURLConnection 통신
            HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            mFileInputStream.close();
            dos.flush();
            // finish upload...
            // get response
            StringBuffer b;
            b = new StringBuffer();
            DataInputStream dis = new DataInputStream(conn.getInputStream());
            try {
                for (String ch; (ch = dis.readUTF()) != null;)  {
                    b.append(ch);
                    b.append(lineEnd);
                }
            } catch (EOFException e) {
                b.delete(b.length()-lineEnd.length(),b.length());
            }
            return b.toString();
        } catch (Exception e) {
            System.out.print(e);
            return null;
            // TODO: handle exception
        }
    }

    public String getImagePathToUri(Uri data) {  // 절대경로 설정 하는 곳
        //사용자가 선택한 이미지의 정보를 받아옴
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        //이미지의 경로 값
        String imgPath = cursor.getString(column_index);
        Log.d("test", imgPath);
        return imgPath; // imgPath를 가져온다.
    }

    public void DoFileUpload(String apiUrl, String absolutePath) {
        NetworkTask networkTask = new NetworkTask(apiUrl, absolutePath); // 다음 은 NetworkTask타입의 networkTask를 만든다는 뜻. 그리고 apiURI와 absolutewPath를 매개변수로 넣는다.
        networkTask.execute(); // NetworkTask를 실행한다는 뜻,
    }

    public void  DoTextUpload(String apiUrl, String text){
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        private String url, fileName;

        public NetworkTask(String url, String fileName) { //다음 URI와 FileName과 subject를 보냄.
            this.url = url;
            this.fileName = fileName;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(Void... params) {
            String result; // 요청 결과를 저장할 변수.
            result = HttpURLConnection (url, "", fileName); // 해당 URL로 부터 결과물을 얻어온다. //
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
            downText.setText(s);
            downString = s;
            tokenizer(); //받은 문자열 분리
        }
    }

    public class Task extends AsyncTask<String, Void, String> {
        String sendMsg;
        String receiveMsg;
        String serverip = "http://rbghoneroom402.iptime.org:48526/JSP/Test.jsp"; // 연결할 jsp주소

        Task(String sendmsg){
            this.sendMsg = sendmsg;
        }
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                URL url = new URL(serverip);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                OutputStreamWriter dos1= new OutputStreamWriter(conn.getOutputStream());

                if(sendMsg.equals("vision_write")){
                    sendMsg = "vision_write="+strings[0]+"&type="+strings[1]; //다음 전달 받은 값이 vision_write라는 값이 왔다? 그럼 넘겨준다,
                }else if(sendMsg.equals("vision_list")){
                    sendMsg = "&type="+strings[0];
                }
                dos1.write(sendMsg);
                dos1.flush();

                if(conn.getResponseCode() == conn.HTTP_OK) { // 다음 연결이 되었는지 확인.
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();
                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러"); //다음 연결이 안됬을때. 나타나는 LOGCAT
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg; // 연결이 되서 결과를 받았으면 receiveMsg 를 받는다.
        }
    }
}
