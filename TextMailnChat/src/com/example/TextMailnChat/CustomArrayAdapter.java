package com.example.TextMailnChat;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<OneComment> {

	private TextView Name;
	private List<OneComment> values = new ArrayList<OneComment>();
	private LinearLayout wrapper;

	@Override
	public void add(OneComment object) {
		values.add(object);
		super.add(object);
	}

	public CustomArrayAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public int getCount() {
		return this.values.size();
	}

	public OneComment getItem(int index) {
		return this.values.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.bubble_item, parent, false);
		}

		wrapper = (LinearLayout) row.findViewById(R.id.wrapper);

		OneComment coment = getItem(position);

		Name = (TextView) row.findViewById(R.id.comment);

		Name.setText(coment.comment);

		if(coment.mine)
		{
			Name.setBackgroundResource(R.drawable.bubble_green);
			wrapper.setGravity(Gravity.RIGHT);
		}
		else
		{
			Name.setBackgroundResource(R.drawable.bubble_yellow);
			wrapper.setGravity(Gravity.LEFT);
		}

		return row;
	}

	public Bitmap decodeToBitmap(byte[] decodedByte) {
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

}
