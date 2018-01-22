package com.single.marquee3dview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.xiangcheng.marquee3dview.Marquee3DView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Marquee3DView marquee3DView = findViewById(R.id.marquee3DView);
        final Marquee3DView marquee3DView2 = findViewById(R.id.marquee3DView2);
        final List<String> labels = new ArrayList<>();
        labels.add("春眠不觉晓,");
        labels.add("处处闻啼鸟.");
        labels.add("夜来风雨声,");
        labels.add("花落知多少.");
        final List<String> labels1 = new ArrayList<>();
        labels1.add("锄禾日当午,");
        labels1.add("汗滴禾下土.");
        labels1.add("谁知盘中餐,");
        labels1.add("粒粒皆辛苦.");
        marquee3DView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                marquee3DView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                marquee3DView.setMarqueeLabels(labels);
                marquee3DView.setOnWhereItemClick(new Marquee3DView.OnWhereItemClick() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(MainActivity.this, labels.get(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        marquee3DView2.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                marquee3DView2.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                marquee3DView2.setMarqueeLabels(labels1);
                marquee3DView2.setOnWhereItemClick(new Marquee3DView.OnWhereItemClick() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(MainActivity.this, labels1.get(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
