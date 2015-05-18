package se.killergameab.phonster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class HighscoresActivityEnd extends Activity {

    MediaPlayer mp_button_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore_list_end);

        mp_button_sound = MediaPlayer.create(this, R.raw.menubutton);

        TextView txtHeader = (TextView) findViewById(R.id.headerHighscoresTextEnd);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/DoubleFeature20.ttf");

        txtHeader.setTypeface(typeface1);

        // Get the score from the intent
        Intent intent = getIntent();
        String player_name = intent.getStringExtra(AddScoreActivity.EXTRA_MESSAGE);
        Bundle extras = getIntent().getExtras();
        int exp = extras.getInt("EXTRA_EXP");

        ListView view = (ListView) findViewById(R.id.list);

        ArrayList<User> data = new ArrayList<>();
        User johan = new User("Johan", 543);
        User baloo = new User("Baloo", 377);
        User hanna = new User("Hanna", 99);
        User player = new User(player_name, exp);

        data.add(0,johan);
        data.add(1,baloo);
        data.add(2,hanna);
        data.add(3, player);

        //Sort list
        Collections.sort(data, new Comparator<User>() {
            @Override
            public int compare(User z1, User z2) {
                if (z1.score < z2.score)
                    return 1;
                if (z1.score > z2.score)
                    return -1;
                return 0;
            }
        });

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

    /** Called when the user clicks the Main Menu button */
    public void menuButton(View view) {
        mp_button_sound.start();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Disable back button
    @Override
    public void onBackPressed() {
    }
}
