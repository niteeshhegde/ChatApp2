package abc.chatapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class DesignActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private IntroManager introManager;
    private ViewPagerAdapter viewPagerAdapter;
    private int[] layouts;
    private int[] layouts2;
    private TextView[] dots;
    private LinearLayout dotsLayout;
    private Button next,skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        introManager=new IntroManager(this);
        if(!introManager.check()){
            introManager.setFirst(false);
            Intent intent=new Intent(DesignActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        if(Build.VERSION.SDK_INT>=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        setContentView(R.layout.activity_design);
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        dotsLayout=(LinearLayout)findViewById(R.id.layout_dddots);
        skip=(Button)findViewById(R.id.btn_skp);
        next=(Button)findViewById(R.id.btn_nxt);
        layouts=new int[]{R.layout.activity_screen_1,R.layout.activity_screen_2,R.layout.activity_screen_3,R.layout.activity_screen_4,R.layout.activity_screen_5};
        addBottomDots(0);
        changeStatusBarColor();
        viewPagerAdapter=new ViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPageListener);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                introManager.setFirst(false);
                Intent intent=new Intent(DesignActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int current=getItem(+1);
                if(current<layouts.length){
                    viewPager.setCurrentItem(current);
                }
                else{
                    introManager.setFirst(false);
                    Intent intent=new Intent(DesignActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }
    public void changeStatusBarColor(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window= getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
    private void addBottomDots(int position){
        dots=new TextView[layouts.length];
        int[] colorActive=getResources().getIntArray(R.array.dot_active);
        int[] colorInactive=getResources().getIntArray(R.array.dot_inactive);
        dotsLayout.removeAllViews();
        for(int i=0;i<dots.length;i++){

         //   dotsLayout.removeAllViews();
            dots[i]=new TextView(this);
            dots[i].setText("*");
           dots[i].setTextSize(20);
            dots[i].setTextColor(colorActive[position]);
           dotsLayout.addView(dots[i]);

        }

        if(dots.length>0){
            dots[position].setTextColor(colorInactive[position]);
        }

    }
    private int getItem(int i){
        return viewPager.getCurrentItem()+i;
    }
    ViewPager.OnPageChangeListener viewPageListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if(position==layouts.length-1){
                next.setText("proceed");
                skip.setVisibility(View.GONE);

            }
            else{
                next.setText("next");
                skip.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    public class ViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        @Override
        public Object instantiateItem(ViewGroup containter, int position){

            layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v=layoutInflater.inflate(layouts[position],containter,false);
            containter.addView(v);
            return v;

        }
      //  private Object in;

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
          //  super.destroyItem(container, position, object);
            View v =(View)object;
            container.removeView(v);
        }
    }

}
