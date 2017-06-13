package com.example.administrator.viewlearning;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class FragmentB extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    Button btn_grid, btn_linear, btn_add, btn_delete;
    MyRecycleAdapter myRecycleAdapter = new MyRecycleAdapter();
    ArrayList<String> data = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_b, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        btn_grid = (Button) rootView.findViewById(R.id.btn_grid);
        btn_linear = (Button) rootView.findViewById(R.id.btn_linear);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_delete = (Button) rootView.findViewById(R.id.btn_delete);
        return rootView;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();

        btn_linear.setOnClickListener(this);
        btn_grid.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_add.setOnClickListener(this);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(myRecycleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_grid:
                if (recyclerView != null) {
                    recyclerView.removeAllViews();
                }
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                recyclerView.setAdapter(myRecycleAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                break;
            case R.id.btn_linear:
                if (recyclerView != null) {
                    recyclerView.removeAllViews();
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(myRecycleAdapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                break;
            case R.id.btn_add:
                myRecycleAdapter.addData(1);
                break;
            case R.id.btn_delete:
                myRecycleAdapter.deleteData(1);
                break;
            default:
                break;

        }
    }


    public void initData() {
        for (int i = 'A'; i < 'z'; i++) {
            data.add("" + (char) i);
        }
    }


    class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {

        @Override
        public MyRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item, viewGroup, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyRecycleAdapter.MyViewHolder viewHolder, int i) {
            viewHolder.tv_item.setText(data.get(i));
            viewHolder.tv_item.setHeight(new Random().nextInt(50) + 60);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void addData(int position) {

            data.add(position, "one");
            //notifyItemChanged(position);
            notifyItemInserted(position);
            notifyItemRangeChanged(position, data.size());


        }

        public void deleteData(int position) {
            if (data.size() != 1) {
                data.remove(position);
                notifyItemRemoved(position);
            }

        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv_item;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv_item = (TextView) itemView.findViewById(R.id.tv_item);
            }
        }
    }

}
