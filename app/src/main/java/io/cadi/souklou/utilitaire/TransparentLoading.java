package io.cadi.souklou.utilitaire;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.cadi.souklou.AppContext;
import io.cadi.souklou.R;

/**
 * Created by arcadius on 9/14/2016.
 */
public class TransparentLoading {

    View view;
    private static TransparentLoading instance;
    public TransparentLoading() {
        view = LayoutInflater.from(AppContext.getInstance().getApplicationContext()).inflate(R.layout.loading,null);
    }

    public static TransparentLoading getInstance() {
        if (instance == null){
            instance = new TransparentLoading();
            return instance;
        }
        else
            return instance;
    }

    public void addViewto(View v) {

        ((ProgressBar)view.findViewById(R.id.progressLoading))
                .getIndeterminateDrawable()
                .setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                (LinearLayout.LayoutParams.MATCH_PARENT), (LinearLayout.LayoutParams.MATCH_PARENT));
        view.setLayoutParams(lp);
        if (v instanceof LinearLayout) {
            ((LinearLayout) v).addView(view);
        }else if (v instanceof RelativeLayout){
            ((RelativeLayout) v).addView(view);
        }
    }

    public void removeView() {
        ((ViewGroup)view.getParent()).removeView(view);
    }

    public void changeText(String str) {
        TextView txt = (TextView) view.findViewById(R.id.txtLoadingText);
        txt.setText(str);
    }

}
