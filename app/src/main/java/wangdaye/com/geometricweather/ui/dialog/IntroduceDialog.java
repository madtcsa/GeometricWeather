package wangdaye.com.geometricweather.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.ui.activity.MainActivity;
import wangdaye.com.geometricweather.ui.adapter.ViewPagerAdapter;
import wangdaye.com.geometricweather.ui.widget.CircularIndicator;
import wangdaye.com.geometricweather.ui.widget.IntroduceViewPager;

/**
 * Introduce dialog.
 * */

public class IntroduceDialog extends DialogFragment
        implements View.OnClickListener, ViewPager.OnPageChangeListener {
    // widget
    private IntroduceViewPager viewPager;
    private TextView text;
    private Button button;
    private CircularIndicator indicator;

    // data
    private String[] texts;
    private int pageNow = 1;
    private float oldOffset;
    private final int PAGE_NUM = 4;

    /** <br> life cycle. */

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        texts = new String[] {
                getString(R.string.introduce_text_1),
                getString(R.string.introduce_text_2),
                getString(R.string.introduce_text_3),
                getString(R.string.introduce_text_4)};

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_introduce, null, false);
        this.initWidget(view);
        this.initViewPager();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Activity container = getActivity();
        if (container instanceof MainActivity) {
            ((MainActivity) container).buildUI();
        }
    }

    /** <br> UI. */

    private void initWidget(View view) {
        this.viewPager = (IntroduceViewPager) view.findViewById(R.id.fragment_introduce_viewPager);

        this.indicator = (CircularIndicator) view.findViewById(R.id.fragment_introduce_indicator);
        indicator.setData(PAGE_NUM, 1, 2, 0);

        this.text = (TextView) view.findViewById(R.id.fragment_introduce_text);
        text.setText(texts[0]);

        this.button = (Button) view.findViewById(R.id.fragment_introduce_button);
        button.setText(getString(R.string.next));
        button.setOnClickListener(this);
    }

    @SuppressLint("InflateParams")
    private void initViewPager() {
        View[] views = new View[4];
        for (int i = 0; i < views.length; i ++) {
            views[i] = LayoutInflater
                    .from(getActivity())
                    .inflate(R.layout.container_introduce_page, null, false);
        }

        ImageView[] images = new ImageView[views.length];
        for (int i = 0; i < images.length; i ++) {
            images[i] = (ImageView) views[i].findViewById(R.id.container_introduce_image);
        }
        Glide.with(this)
                .load(R.drawable.introduction_1)
                .into(images[0]);
        Glide.with(this)
                .load(R.drawable.introduction_2)
                .into(images[1]);
        Glide.with(this)
                .load(R.drawable.introduction_3)
                .into(images[2]);
        Glide.with(this)
                .load(R.drawable.introduction_4)
                .into(images[3]);


        final List<View> viewList = new ArrayList<>();
        viewList.add(views[0]);
        viewList.add(views[1]);
        viewList.add(views[2]);
        viewList.add(views[3]);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
    }

    /** <br> interface. */

    // on click listener.

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_introduce_button:
                if (pageNow < PAGE_NUM) {
                    viewPager.setCurrentItem(pageNow ++);
                } else {
                    dismiss();
                }
                break;
        }
    }

    // on page changed listener.

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicator.setData(PAGE_NUM, position + 1, position + 2, positionOffset);
        if (positionOffset < 0.5) {
            text.setAlpha((float) (1 - positionOffset * 2.0));
        } else {
            text.setAlpha((float) ((positionOffset - 0.5) * 2.0));
        }

        if (positionOffset > oldOffset && positionOffset > 0.5) {
            // to next page && offset more than half page.
            if (!text.getText().toString().equals(texts[position + 1])) {
                text.setText(texts[position + 1]);
            }
        } else if (positionOffset < oldOffset && positionOffset < 0.5) {
            // to last page && offset more than half page.
            if (!text.getText().toString().equals(texts[position])) {
                text.setText(texts[position]);
            }
        }
        oldOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {
        pageNow = position + 1;
        if (position + 1 == PAGE_NUM) {
            button.setText(getString(R.string.done));
        } else {
            button.setText(getString(R.string.next));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // do nothing.
    }
}
