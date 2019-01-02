package ru.rofleksey.intflex.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.rofleksey.intflex.R;
import ru.rofleksey.intflex.runtime.IntFlex;
import ru.rofleksey.intflex.runtime.Result;

public class ResultActivity extends AppCompatActivity {

    static final String INPUT_CODE = "rofleksey.inputcode";

    ArrayList<String> results = new ArrayList<>();
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        recyclerView = findViewById(R.id.resultRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(results);
        recyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        String realInput = intent.getStringExtra(INPUT_CODE);
        IntFlex.execute(realInput, new IntFlex.IntFlexCallback() {
            @Override
            public void onDone(Result result) {
                results.addAll(result.getResults());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onPercentage(float percent) {

            }

            @Override
            public void onError(Exception e) {
                results.add(e.getMessage());
                adapter.notifyDataSetChanged();
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<String> data;

        MyAdapter(ArrayList<String> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_layout, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String s = data.get(position);
            holder.mTextView.setText(s);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;
            LinearLayout mLinearLayout;

            ViewHolder(LinearLayout l) {
                super(l);
                mLinearLayout = l;
                mTextView = l.findViewById(R.id.textView);
                mLinearLayout.setOnClickListener((v) -> {

                });
            }
        }
    }
}
