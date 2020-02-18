package com.cookandroid.attendandroidapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class adapterActivity extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<itemActivity> m_oData = null;
    private int nListCnt = 0;

    public adapterActivity(ArrayList<itemActivity> _oData)
    {
        m_oData = _oData;
        nListCnt = m_oData.size();
    }

    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {

            final Context context = parent.getContext();

            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            }
            convertView = inflater.inflate(R.layout.list_item, parent, false);
        }

        CheckBox check = convertView.findViewById(R.id.check);
        check.setChecked(((ListView)parent).isItemChecked(position));
        check.setFocusable(false);
        check.setClickable(false);


        TextView data = convertView.findViewById(R.id.data);

        data.setText(m_oData.get(position).data);

        return convertView;
    }
}



