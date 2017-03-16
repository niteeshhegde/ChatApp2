package abc.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by niteesh on 3/16/2017.
 */

public class IntroManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    public IntroManager(Context context){
        this.context=context;
        this.pref=context.getSharedPreferences("First",1);
        this.editor=pref.edit();
    }
    public void setFirst(Boolean bool){
        editor.putBoolean("check",bool);
        editor.commit();
    }
    public boolean check(){
        return pref.getBoolean("check",true);
    }

}
