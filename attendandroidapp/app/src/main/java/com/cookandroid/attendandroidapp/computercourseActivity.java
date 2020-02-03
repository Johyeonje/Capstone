package com.cookandroid.attendandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class computercourseActivity<computer> extends AppCompatActivity {

    ArrayList<Computer> computer;
    ListView computer_list;
    computerAdapter adapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.computer_course);

        computer = new ArrayList<Computer>();
        Computer course;
        course = new Computer(R.drawable.unnamed,"신효정","201720550");
        computer.add(course);
        course = new Computer(R.drawable.unnamed,"임세민","201620550");
        computer.add(course);
        course = new Computer(R.drawable.unnamed,"조다솜","201620550");
        computer.add(course);
        course = new Computer(R.drawable.unnamed,"김지윤","201720550");
        computer.add(course);
        course = new Computer(R.drawable.unnamed,"황하영","201920550");
        computer.add(course);

        computer_list = findViewById(R.id.computer_list);
        computerAdapter adapter = new computerAdapter(this,R.layout.computer_item,computer);
        computer_list.setAdapter(adapter);



    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.login_btn:
                Intent intent1 = new Intent(getApplicationContext(), loginActivity.class);

                startActivity(intent1);
                break;

            case R.id.home_btn:
                Intent intent2 = new Intent(getApplicationContext(), com.cookandroid.attendandroidapp.MainActivity.class);
                startActivity(intent2);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}

class Computer {
    int Icon;
    String name;
    String number;

    Computer(int lcons, String Name, String Number) {
        Icon = lcons;
        name = Name;
        number = Number;
    }
}

class computerAdapter extends BaseAdapter {

    Context con;
    LayoutInflater inflater;
    ArrayList<Computer> arD;
    int layout;

    public computerAdapter(Context context, int alayout, ArrayList<Computer> arrD) {
        con = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        arD = arrD;
        layout = alayout;
    }

    @Override
    public int getCount() {
        return arD.size();
    }

    @Override
    public Object getItem(int position) {
        return arD.get(position).name;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false);

        }
        ImageView image = convertView.findViewById(R.id.stu_image);
        image.setImageResource(arD.get(position).Icon);

        TextView label = convertView.findViewById(R.id.title);
        label.setText(arD.get(position).name);

        TextView content = convertView.findViewById(R.id.content);
        content.setText(arD.get(position).number);


        return convertView;
    }



}
