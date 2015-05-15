package se.killergameab.phonster;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;


public class HighscoresActivity extends ActionBarActivity {

    MediaPlayer mp_menu_song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mp_menu_song = MediaPlayer.create(this, R.raw.mainmenusong);
        //mp_menu_song.setLooping(true);
        //mp_menu_song.start();

        setTitle("Highscores");
        setContentView(R.layout.activity_highscore_list);
        ListView view = (ListView) findViewById(R.id.list);

        ArrayList<User> data = new ArrayList<>();
        User johan = new User("Johan", 543);
        User baloo = new User("Baloo", 377);
        User hanna = new User("Hanna", 99);
        data.add(0,johan);
        data.add(1,baloo);
        data.add(2,hanna);

        UserAdapter adapter = new UserAdapter(this, R.layout.highscore_list_item, data);
        view.setAdapter(adapter);
    }

    private class UserAdapter extends ArrayAdapter<User> {
        private ArrayList<User> data;

        public UserAdapter(Context context, int textViewResourceId, ArrayList<User> data){
            super(context, textViewResourceId, data);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public User getItem(int position) {
            if (data != null) {
                return data.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null){
               LayoutInflater li = LayoutInflater.from(getContext());
                v = li.inflate(R.layout.highscore_list_item, null);
            }

            User u = data.get(position);
            if (u != null) {
                TextView user = (TextView) v.findViewById(R.id.username);
                TextView score = (TextView) v.findViewById(R.id.score);

                if (user != null) {
                    user.setText(u.getName());
                }

                if (score != null){
                    String stringScore = Integer.toString(u.getScore());
                    score.setText(stringScore);
                }
            }
            return v;
        }
    }

    private class User {
        private String name;
        private int score;

        public User(String name, int score){
            this.name = name;
            this.score = score;
        }

        public String getName(){
            return name;
        }

        public int getScore(){
            return score;
        }
    }

}
