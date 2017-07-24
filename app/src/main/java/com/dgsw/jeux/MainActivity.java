package com.dgsw.jeux;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.hyunjun.school.School;
import org.hyunjun.school.SchoolException;
import org.hyunjun.school.SchoolMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";

    public static int ExitMonth = 5;
    public static int ExitDay = 19;

    EditText mInputMessage;
    Button mSendMessage;
    TextView mMessageTitle, mMessageContent;
    ImageView mImageView;

    //AnimationDrawable drawable;

    MediaPlayer mediaPlayer;

    List<String> lastWordList = new ArrayList<>();

    static boolean isFirst = true;
    static boolean isOne = true;
    static boolean isLastWord = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        lastWordList.add("스칸듐");
        lastWordList.add("과연소산칼륨");
        lastWordList.add("고사리무늬");
        lastWordList.add("장꾼");

        mInputMessage = (EditText) findViewById(R.id.input_message);
        mSendMessage = (Button) findViewById(R.id.send_message);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mMessageTitle = (TextView) findViewById(R.id.jeux_message_title);
        mMessageContent = (TextView) findViewById(R.id.jeux_message);
        mSendMessage.setOnClickListener(this);

        mImageView.setTag(0);

        //drawable = (AnimationDrawable) mImageView.getBackground();

        Random random = new Random();
        int randNum = random.nextInt(3);

        if (randNum == 0) {
            //faceTimer();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hello_user);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //drawable.stop();
                    if (mediaPlayer != null) mediaPlayer.release();
                }
            });
            mMessageTitle.setText("안녕하십니까 사용자님");
        } else if (randNum == 1) {
            //drawable.start();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bong_hello);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //drawable.stop();
                    if (mediaPlayer != null) mediaPlayer.release();
                }
            });
            mMessageTitle.setText("봉주르");
        } else {
            //drawable.start();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bong_hello_user);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //drawable.stop();
                    if (mediaPlayer != null) mediaPlayer.release();
                }
            });
            mMessageTitle.setText("봉주르~ 사용자님");
        }
        mInputMessage.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    send();
                    return true;
                }
                return false;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isFirst) {
            Toast.makeText(getApplicationContext(), "그렇게 나갈 수 없습니다.", Toast.LENGTH_SHORT).show();
            isFirst = false;
        } else if (isOne) {
            Toast.makeText(getApplicationContext(), "니가 틀렸어.", Toast.LENGTH_SHORT).show();
            isOne = false;
        } else {
            Toast.makeText(getApplicationContext(), "니 생각이 틀렸어.", Toast.LENGTH_SHORT).show();
            isOne = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            this.startActivity(intent);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data.hasExtra(RecognizerIntent.
                EXTRA_RESULTS)) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.
                    EXTRA_RESULTS);
            if (results.size() > 0) {
                mInputMessage.setText(results.get(0));
                send();
            }
        }
    }

    public void playMedia(int rawid) {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), rawid);
        mediaPlayer.start();
    }

    public void setMessage(String Title, String Content) {
        mMessageTitle.setText(Title);
        mMessageContent.setText(Content);
    }

    public boolean isOnline() {
        CheckNetwork checkNetwork = new CheckNetwork(CONNECTION_CONFIRM_CLIENT_URL);
        checkNetwork.start();
        try {
            checkNetwork.join();
            return checkNetwork.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void faceTimer() {
        mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.jeux, null));
        SystemClock.sleep(300);
        mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.jeux2, null));
    }

    private void send() {
        if (mediaPlayer != null) mediaPlayer.release();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Calendar mCalendar = Calendar.getInstance();

        mCalendar.get(Calendar.DAY_OF_MONTH);
        mCalendar.get(Calendar.DAY_OF_WEEK);

        // SEND 버튼이 눌렸을 때의 처리
        String inputText = mInputMessage.getText().toString();
        String lowerInputText = inputText.toLowerCase();
        String[] mTrash = {"\n", " "};
        for (String e : mTrash) {
            lowerInputText = lowerInputText.replace(e, "");
        }



        if (lowerInputText.equals("")) {
            return;
        }

        // TextView를 View의 맨 위에 설정한다.
        if(isLastWord) {
            if (lowerInputText.contains(("졌다."))) {
                isLastWord = false;
                setMessage("", "");
                return;
            } else if (lowerInputText.contains(("졌다"))) {
                Snackbar.make(this.findViewById(R.id.coordinator_layout), "진지하게 \'.\'도 쳐주세요^つ^", Snackbar.LENGTH_LONG).show();
                return;
            }
            //String str = lowerInputText.substring(lowerInputText.length()-1);
            //MessageBox.Show(lastWordList[0]);
            //setMessage(""+lowerInputText.charAt(lowerInputText.length()-2), "");
            for(int i = 0;i < lastWordList.size(); i++) {
                if(lastWordList.get(i).indexOf(lowerInputText.charAt(lowerInputText.length()-1)) == 0) {
                    setMessage(lastWordList.get(i), "");
                    Snackbar.make(this.findViewById(R.id.coordinator_layout), "패배를 인정하려면 졌다를 쳐주세요^つ^", Snackbar.LENGTH_LONG).show();
                }
            }
        } else if (lowerInputText.contains("제욱") || lowerInputText.contains("ㅎㅇ") || lowerInputText.contains("안녕") || lowerInputText.contains("봉주르") || lowerInputText.contains("hi")) {

            Random random = new Random();
            int randNum = random.nextInt(3);
            if (randNum == 0) {
                //drawable.start();
                playMedia(R.raw.bong_hello);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        //rawable.stop();
                        if (mp != null) mp.release();
                    }
                });
                mMessageTitle.setText("봉주르~~");
            } else if (randNum == 1) {
                //drawable.start();
                playMedia(R.raw.bong_hello_user);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        //drawable.stop();
                        if (mp != null) mp.release();
                    }
                });
                mMessageTitle.setText("봉주르! 사용자님");
            } else {
                playMedia(R.raw.snail_cooking);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        //drawable.stop();
                        if (mp != null) mp.release();
                    }
                });

                mMessageTitle.setText("달팽이 요리 드셔보실래요?");
            }
            mInputMessage.setText("");
        } else if (lowerInputText.contains("꺼져")) {

            //drawable.start();
            playMedia(R.raw.finish_app_voice);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    playMedia(R.raw.finish_app);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            //drawable.stop();
                            finish();
                        }
                    });
                }
            });
            //drawable.start();

            mInputMessage.setText("");

        } else if (lowerInputText.contains("비켜")) {
            //drawable.start();
            playMedia(R.raw.a_a_a);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //drawable.stop();
                    if (mp != null) mp.release();
                }
            });
            mInputMessage.setText("");
            this.moveTaskToBack(true);
        } else if (lowerInputText.contains("심심") || lowerInputText.contains("놀아줘") || lowerInputText.contains("끝말잇기")) {

            Random random = new Random();
            int randNum = random.nextInt(2);
            if (randNum == 0) {
                playMedia(R.raw.you_like_play);
                setMessage("놀아드릴까요?", "");
            } else {
                playMedia(R.raw.you_bored);
                setMessage("심심하신가요?", "");
            }

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("야생의 제욱스가 싸움을 걸어왔다!")
                    .setMessage("제욱스와 끝말잇기를 하시겠습니까?")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mMessageTitle.setText("먼저 하시죠.");
                            isLastWord=true;
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            mInputMessage.setText("");
        } else if (lowerInputText.contains("시간")) {
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR); // 시
            int minute = cal.get(Calendar.MINUTE); // 분
            int second = cal.get(Calendar.SECOND); // 초
            setMessage(getString(R.string.time_format, hour, minute, second), null);
            mInputMessage.setText("");
        } else if (lowerInputText.contains(getString(R.string.meal))) {
            if (!isOnline()) {
                mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.jeux_sad, null));
                setMessage(null, "이런, 제욱스가 급식정보를 털어오지 못했습니다.\n흑😢, 넘나 슬픈 것");
            } else {
                try {
                    School api = new School(School.Type.HIGH, School.Region.DAEGU, "D100000282");

                    List<SchoolMenu> menu = api.getMonthlyMenu(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
                   // drawable.start();
                    playMedia(R.raw.mealbug);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            // TODO Auto-generated method stub
                            //drawable.stop();
                            mSendMessage.setClickable(true);
                            if (mp != null) mp.release();
                        }
                    });
                    setMessage("급식충에겐 급식이란...", "[조식]\n" + menu.get(mCalendar.get(Calendar.DAY_OF_MONTH) - 1).breakfast + "\n[중식]\n" + menu.get(mCalendar.get(Calendar.DAY_OF_MONTH) - 1).lunch + "\n[석식]\n" + menu.get(mCalendar.get(Calendar.DAY_OF_MONTH) - 1).dinner);
                } catch (SchoolException e) {
                    e.printStackTrace();
                    setMessage("어랏, 오류가 발생했습니다", null);
                }
            }
            mInputMessage.setText("");
        } else if (lowerInputText.contains("노래해줘")) {
            mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.jeux_sing, null));
            playMedia(R.raw.singing);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.jeux_say, null));
                    if (mp != null) mp.release();
                }
            });
            mInputMessage.setText("");
        } else if (lowerInputText.contains("yee")) {
            mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.jeux_yee, null));
            playMedia(R.raw.yee_song);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.jeux_say, null));
                    if (mp != null) mp.release();
                }
            });
            mInputMessage.setText("");
        } else if (lowerInputText.contains("얼굴")) {
            mImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.foot, null));
            mInputMessage.setText("");
        } else if (lowerInputText.contains("극혐") || (lowerInputText.contains("니") && lowerInputText.contains("애미"))) {

            Random random = new Random();
            int randNum = random.nextInt(4);
            //SystemClock.sleep(200);
            //drawable.start();

            if (randNum == 0) {
                playMedia(R.raw.si_mu_look);
                setMessage("시__무__룩", "");
            } else if (randNum == 1) {
                playMedia(R.raw.hey_user_want_die);
                setMessage("아니 저기요 사용자님 -.- \n뭐하시는거죠 진짜 죽고싶어?", "");
            } else if (randNum == 2) {
                playMedia(R.raw.no_mother);
                setMessage("진짜 왜살아 엄마없어?", "");
            } else {
                playMedia(R.raw.so_why);
                setMessage("왜그러시죠?", "");
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    //drawable.stop();
                    if (mp != null) mp.release();
                }
            });
            mInputMessage.setText("");
        } else if (lowerInputText.contains("man")) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle(R.string.cmd_ex)
                    .setMessage(R.string.cmd_ex_content)
                    .show();
            mInputMessage.setText("");
        } else {
                Random random = new Random();
                int randNum = random.nextInt(9);

                if (randNum == 0) {
                    setMessage("엄마 없어?", "");
                } else if (randNum == 1) {
                    setMessage("하..하...", "");
                } else if (randNum == 2) {
                    setMessage("헤ㅔㅔ~?", "");
                } else if (randNum == 3) {
                    setMessage("왜 살아?", "");
                } else if (randNum == 4) {
                    setMessage("아니 저기요", "");
                } else if (randNum == 5) {
                    setMessage("선.생.님. 뭐하시는 거죠?", "");
                } else if (randNum == 6) {
                    setMessage("죽고싶어?", "");
                } else if (randNum == 7) {
                    setMessage("디질래?", "");
                } else {
                    setMessage("진짜 엄마 없어?", "");
                }
                mInputMessage.setText("");
            }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mSendMessage)) {
            send();
        }
    }
}
