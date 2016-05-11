package com.zb.mytest;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @ClassName: MainActivity2 
 *  @Description: RecyclerView 示例
 *  @author 翟彬
 *  @date 2016年5月4日 上午11:03:49
 */
public class MainActivity2 extends ActionBarActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
		ArrayList<String> datas = new ArrayList<String>();
		int a = 10 + new Random().nextInt(1);
		for (int i = 0; i < a; i++) {
			datas.add("数据：：" + i);
		}
		LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
		layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutmanager);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(new MyAdapter(datas));
	}

	@Override
	public void onClick(View v) {

	}

	class MyViewHodler extends RecyclerView.ViewHolder {

		public MyViewHodler(View itemView) {
			super(itemView);
		}

		public TextView tView;
		public Button button1;
		public Button button2;

	}

	class MyAdapter extends RecyclerView.Adapter<MyViewHodler> {
		private ArrayList<String> datas;

		public MyAdapter(ArrayList<String> datas) {
			this.datas = datas;
		}

		@Override
		public int getItemCount() {
			return datas.size();
		}

		@Override
		public void onBindViewHolder(final MyViewHodler arg0, final int arg1) {
			arg0.tView.setText(datas.get(arg1));
			arg0.button1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Toast.makeText(arg0.itemView.getContext(), datas.get(arg1), Toast.LENGTH_SHORT).show();
					arg0.tView.postDelayed(new Runnable() {
						public void run() {
							datas.set(arg1, null);
							notifyItemRangeRemoved(arg1, 1);
						}
					}, 1000);
				}
			});
			arg0.button2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Toast.makeText(arg0.itemView.getContext(), datas.get(arg1), Toast.LENGTH_SHORT).show();
					arg0.tView.postDelayed(new Runnable() {
						public void run() {
							datas.add(arg1, "插入到" + arg1);
							notifyItemRangeInserted(arg1, 1);
						}
					}, 1000);
				}
			});
		}

		@Override
		public MyViewHodler onCreateViewHolder(ViewGroup arg0, int arg1) {
			View view = LayoutInflater.from(arg0.getContext()).inflate(R.layout.item_recyclerview, null);
			MyViewHodler myViewHodler = new MyViewHodler(view);
			myViewHodler.tView = (TextView) view.findViewById(R.id.textView1);
			myViewHodler.button1 = (Button) view.findViewById(R.id.button1);
			myViewHodler.button2 = (Button) view.findViewById(R.id.button2);
			return myViewHodler;
		}

	}

}
