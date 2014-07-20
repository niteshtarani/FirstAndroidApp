package com.example.TextMailnChat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String>
{
	private final Activity context;
	private final String[] msgs;
	private final String[] addr;
	public CustomList(Activity context,String[] msgs, String[] addr) {
	super(context, R.layout.single_list, msgs);
	this.context = context;
	this.msgs = msgs;
	this.addr = addr;
}
@Override
public View getView(int position, View view, ViewGroup parent)
{
	LayoutInflater inflater = context.getLayoutInflater();
	View rowView= inflater.inflate(R.layout.single_list, null, true);
	TextView msgTitle = (TextView) rowView.findViewById(R.id.txt);
	TextView addrTitle = (TextView) rowView.findViewById(R.id.img);
	msgTitle.setText(msgs[position]);
	addrTitle.setText(addr[position]);
	return rowView;
}
}
