package com.androidcalls.reports;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.androidcall.model.CallingModel;
import com.androidcalls.R;

public class ReportListAdapter extends ArrayAdapter<CallingModel> {

	ArrayList<CallingModel> list;

	public ReportListAdapter(Context context, int resource,
			ArrayList<CallingModel> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		list = objects;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public CallingModel getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.cell_report, null);
			holder = new ViewHolder();
			holder.txtMessage = (TextView) convertView
					.findViewById(R.id.txt_number);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CallingModel model = getItem(position);

		holder.txtMessage.setText(model.phone_number);

		return convertView;
	}

	class ViewHolder {

		TextView txtMessage;

	}
}
