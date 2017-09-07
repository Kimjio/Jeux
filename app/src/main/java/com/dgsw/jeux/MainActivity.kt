package com.dgsw.jeux

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.os.StrictMode
import android.os.SystemClock
import android.speech.RecognizerIntent
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import org.hyunjun.school.School
import org.hyunjun.school.SchoolException
import org.hyunjun.school.SchoolMenu

import java.util.ArrayList
import java.util.Calendar
import java.util.Random

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204"

    internal lateinit var mInputMessage: EditText
    internal lateinit var mSendMessage: Button
    internal lateinit var mMessageTitle: TextView
    internal lateinit var mMessageContent: TextView
    internal lateinit var mImageView: ImageView
    internal lateinit var manTextView: TextView

    lateinit var drawable: AnimationDrawable;

    internal var mediaPlayer: MediaPlayer? = null

    internal var lastWordList: MutableList<String> = ArrayList()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolBar)
        setSupportActionBar(toolbar)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        lastWordList.add("스칸듐")
        lastWordList.add("과연소산칼륨")
        lastWordList.add("고사리무늬")
        lastWordList.add("장꾼")

        mInputMessage = findViewById<EditText>(R.id.input_message)
        mSendMessage = findViewById<Button>(R.id.send_message)
        mImageView = findViewById<ImageView>(R.id.imageView)
        mMessageTitle = findViewById<TextView>(R.id.jeux_message_title)
        mMessageContent = findViewById<TextView>(R.id.jeux_message)
        if (getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_90 || getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_270) {
            manTextView = findViewById<TextView>(R.id.cmd_ex_text)
        }
        mSendMessage.setOnClickListener(this)

        mImageView.tag = 0

        drawable = mImageView.getBackground() as AnimationDrawable

        val random = Random()
        val randNum = random.nextInt(3)

        if (randNum == 0) {
            //faceTimer();
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.hello_user)
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener {
                // TODO Auto-generated method stub
                drawable.stop()
                drawable.selectDrawable(0)
                //mImageView.setImageDrawable(getDrawable(R.drawable.jeux_say))
                if (mediaPlayer != null) mediaPlayer!!.release()
            }
            mMessageTitle.text = "안녕하십니까 사용자님"
        } else if (randNum == 1) {
            drawable.start();
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.bong_hello)
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener {
                // TODO Auto-generated method stub
                drawable.stop()
                drawable.selectDrawable(0)
                if (mediaPlayer != null) mediaPlayer!!.release()
            }
            mMessageTitle.text = "봉주르"
        } else {
            drawable.start();
            mediaPlayer = MediaPlayer.create(applicationContext, R.raw.bong_hello_user)
            mediaPlayer!!.start()
            mediaPlayer!!.setOnCompletionListener {
                // TODO Auto-generated method stub
                drawable.stop()
                drawable.selectDrawable(0)
                if (mediaPlayer != null) mediaPlayer!!.release()
            }
            mMessageTitle.text = "봉주르~ 사용자님"
        }
        mInputMessage.setOnKeyListener(View.OnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                send()
                return@OnKeyListener true
            }
            false
        })
    }

    override fun onBackPressed() {
        if (isFirst) {
            Toast.makeText(applicationContext, "그렇게 나갈 수 없습니다.", Toast.LENGTH_SHORT).show()
            isFirst = false
        } else if (isOne) {
            Toast.makeText(applicationContext, "니가 틀렸어.", Toast.LENGTH_SHORT).show()
            isOne = false
        } else {
            Toast.makeText(applicationContext, "니 생각이 틀렸어.", Toast.LENGTH_SHORT).show()
            isOne = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.fullscreen) {
            //TODO

            return true
        }
        if (id == R.id.action_help) {
            val intent = Intent(this, HelpActivity::class.java)
            this.startActivity(intent)
            return true
        }
        if (id == R.id.action_settings) {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data.hasExtra(RecognizerIntent.EXTRA_RESULTS)) {
            val results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (results.size > 0) {
                mInputMessage.setText(results[0])
                send()
            }
        }
    }

    fun playMedia(rawid: Int) {
        mediaPlayer = MediaPlayer.create(applicationContext, rawid)
        mediaPlayer!!.start()
    }

    fun setMessage(Title: String?, Result: String?) {
        if (!Title.equals(null))
            mMessageTitle.text = Title
        if (!Result.equals(null))
            mMessageContent.text = Result
    }

    fun setMessage(Text: String?, Type: Int) {
        if (Type == 0) {
            mMessageTitle.text = Text
        } else if (Type == 1) {
            mMessageContent.text = Text
        } else {
            Log.e("setMessage", "Invalid Type")
        }
    }

    val isOnline: Boolean
        get() {
            val checkNetwork = CheckNetwork(CONNECTION_CONFIRM_CLIENT_URL)
            checkNetwork.start()
            try {
                checkNetwork.join()
                return checkNetwork.isSuccess
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }

    fun faceTimer() {
        mImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.jeux, null))
        SystemClock.sleep(300)
        mImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.jeux2, null))
    }

    private fun send() {
        if (mediaPlayer != null) mediaPlayer!!.release()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val mCalendar = Calendar.getInstance()

        mCalendar.get(Calendar.DAY_OF_MONTH)
        mCalendar.get(Calendar.DAY_OF_WEEK)

        // SEND 버튼이 눌렸을 때의 처리
        val inputText = mInputMessage.text.toString()
        var lowerInputText = inputText.toLowerCase()
        val mTrash = arrayOf("\n", " ")
        for (e in mTrash) {
            lowerInputText = lowerInputText.replace(e, "")
        }

        // TextView를 View의 맨 위에 설정한다.
        if (isLastWord) {
            if (lowerInputText.contains("졌다.")) {
                isLastWord = false
                setMessage("", null)
                return
            } else if (lowerInputText.contains("졌다")) {
                Snackbar.make(this.findViewById(R.id.coordinator_layout), "\'.\'도 쳐주세요^つ^", Snackbar.LENGTH_LONG).show()
                return
            }
            //String str = lowerInputText.substring(lowerInputText.length()-1);
            //MessageBox.Show(lastWordList[0]);
            //setMessage(""+lowerInputText.charAt(lowerInputText.length()-2), "");
            for (i in lastWordList.indices) {
                if (lastWordList[i].indexOf(lowerInputText[(lowerInputText.length - 1)], 0) == 0) {
                    setMessage(lastWordList[i], "")
                    Snackbar.make(this.findViewById(R.id.coordinator_layout), "패배를 인정하려면 졌다를 쳐주세요^つ^", Snackbar.LENGTH_LONG).show()
                }
            }
        } else if (lowerInputText.contains("제욱") || lowerInputText.contains("ㅎㅇ") || lowerInputText.contains("안녕") || lowerInputText.contains("봉주르") || lowerInputText.contains("hi")) {

            val random = Random()
            val randNum = random.nextInt(3)
            if (randNum == 0) {
                drawable.start();
                playMedia(R.raw.bong_hello)
                mediaPlayer!!.setOnCompletionListener { mp ->
                    // TODO Auto-generated method stub
                    drawable.stop()
                    drawable.selectDrawable(0)
                    mp?.release()
                }
                mMessageTitle.text = "봉주르~~"
            } else if (randNum == 1) {
                drawable.start();
                playMedia(R.raw.bong_hello_user)
                mediaPlayer!!.setOnCompletionListener { mp ->
                    // TODO Auto-generated method stub
                    drawable.stop()
                    drawable.selectDrawable(0)
                    mp?.release()
                }
                mMessageTitle.text = "봉주르! 사용자님"
            } else {
                playMedia(R.raw.snail_cooking)
                mediaPlayer!!.setOnCompletionListener { mp ->
                    // TODO Auto-generated method stub
                    drawable.stop()
                    drawable.selectDrawable(0)
                    mp?.release()
                }

                mMessageTitle.text = "달팽이 요리 드셔보실래요?"
            }
            mInputMessage.setText("")
        } else if (lowerInputText.contains("꺼져")) {

            drawable.start();
            playMedia(R.raw.finish_app_voice)
            mediaPlayer!!.setOnCompletionListener {
                playMedia(R.raw.finish_app)
                mediaPlayer!!.setOnCompletionListener {
                    drawable.stop()
                    drawable.selectDrawable(0)
                    finishAffinity()
                }
            }
            drawable.start();

            mInputMessage.setText("")

        } else if (lowerInputText.contains("비켜")) {
            drawable.start();
            playMedia(R.raw.a_a_a)
            mediaPlayer!!.setOnCompletionListener { mp ->
                // TODO Auto-generated method stub
                drawable.stop()
                drawable.selectDrawable(0)
                mp?.release()
            }
            mInputMessage.setText("")
            this.moveTaskToBack(true)
        } else if (lowerInputText.contains("심심") || lowerInputText.contains("놀아줘") || lowerInputText.contains("끝말잇기")) {

            val randNum = Random().nextInt(2)
            if (randNum == 0) {
                playMedia(R.raw.you_like_play)
                setMessage("놀아드릴까요?", null)
            } else {
                playMedia(R.raw.you_bored)
                setMessage("심심하신가요?", null)
            }

            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("야생의 제욱스가 싸움을 걸어왔다!")
                    .setMessage("제욱스와 끝말잇기를 하시겠습니까?")
                    .setPositiveButton(R.string.yes) { _, _ ->
                        mMessageTitle.text = "먼저 하시죠."
                        isLastWord = true
                    }
                    .setNegativeButton(R.string.no, null)
                    .show()
            mInputMessage.setText("")
        } else if (lowerInputText.contains("시간")) {
            val cal = Calendar.getInstance()
            val hour = cal.get(Calendar.HOUR) // 시
            val minute = cal.get(Calendar.MINUTE) // 분
            val second = cal.get(Calendar.SECOND) // 초
            setMessage(getString(R.string.time_format, hour, minute, second), null)
            mInputMessage.setText("")
        } else if (lowerInputText.contains(getString(R.string.meal))) {
            if (!isOnline) {
                mImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.jeux_sad, null))
                setMessage(null, "이런, 제욱스가 급식정보를 털어오지 못했습니다.\n흑😢, 넘나 슬픈 것")
            } else {
                try {
                    val menu = School(School.Type.HIGH, School.Region.DAEGU, "D100000282").getMonthlyMenu(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1)
                    drawable.start()
                    playMedia(R.raw.mealbug)
                    mediaPlayer!!.setOnCompletionListener { mp ->
                        // TODO Auto-generated method stub
                        drawable.stop()
                        drawable.selectDrawable(0)
                        //Log.d("TAG", ""+)
                        mSendMessage.isClickable = true
                        mp?.release()
                    }

                    setMessage("급식충에겐 급식이란...", menu[mCalendar.get(Calendar.DAY_OF_MONTH) - 1].toString())
                } catch (e: SchoolException) {
                    e.printStackTrace()
                    setMessage(null, "어랏, 오류가 발생했습니다")
                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                    setMessage(null, "나이스에 급식 정보가 없습니다")
                }

            }
            mInputMessage.setText("")
        } else if (lowerInputText.contains("노래해줘")) {
            mImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.jeux_sing, null))
            playMedia(R.raw.singing)
            mediaPlayer!!.setOnCompletionListener { mp ->
                // TODO Auto-generated method stub
                mImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.jeux_say, null))
                mp?.release()
            }
            mInputMessage.setText("")
        } else if (lowerInputText.contains("yee")) {
            mImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.jeux_yee, null))
            playMedia(R.raw.yee_song)
            mediaPlayer!!.setOnCompletionListener { mp ->
                // TODO Auto-generated method stub
                mImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.jeux_say, null))
                mp?.release()
            }
            mInputMessage.setText("")
        } else if (lowerInputText.contains("얼굴")) {
            mImageView.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.foot, null))
            mInputMessage.setText("")
        } else if (lowerInputText.contains("극혐") || lowerInputText.contains("씨발") || lowerInputText.contains("니") && lowerInputText.contains("애미")) {
// com.android.internal.app.ShutdownActivity
            val random = Random()
            val randNum = random.nextInt(4)
            //SystemClock.sleep(200);
            drawable.start();

            if (randNum == 0) {
                playMedia(R.raw.si_mu_look)
                setMessage("시__무__룩", null)
            } else if (randNum == 1) {
                playMedia(R.raw.hey_user_want_die)
                setMessage("아니 저기요 사용자님 -.- \n뭐하시는거죠 진짜 죽고싶어?", null)
            } else if (randNum == 2) {
                playMedia(R.raw.no_mother)
                setMessage("진짜 왜살아 엄마없어?", null)
            } else {
                playMedia(R.raw.so_why)
                setMessage("왜그러시죠?", null)
            }
            mediaPlayer!!.setOnCompletionListener { mp ->
                // TODO Auto-generated method stub
                drawable.stop();drawable.stop()
                drawable.selectDrawable(0)
                mp?.release()
            }
            mInputMessage.setText("")
        } else if (lowerInputText.contains("man")) {
            if (getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_90 || getWindowManager().getDefaultDisplay().getRotation() == Surface.ROTATION_270) {
                manTextView.setText(R.string.cmd_ex_content)
            } else {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle(R.string.cmd_ex)
                        .setMessage(R.string.cmd_ex_content)
                        .show()
            }
            mInputMessage.setText("")
        } else {
            val random = Random()
            val randNum = random.nextInt(9)

            if (randNum == 0) {
                playMedia(R.raw.no_mother_short)
                setMessage("엄마 없어?", null)
            } else if (randNum == 1) {
                setMessage("하..하...", null)
            } else if (randNum == 2) {
                playMedia(R.raw.hae)
                setMessage("헤ㅔㅔ~?", null)
            } else if (randNum == 3) {
                playMedia(R.raw.why_live)
                setMessage("왜 살아?", null)
            } else if (randNum == 4) {
                playMedia(R.raw.hey)
                setMessage("아니 저기요", null)
            } else if (randNum == 5) {
                playMedia(R.raw.what_doing)
                setMessage("선.생.님. 뭐하시는 거죠?", null)
            } else if (randNum == 6) {
                playMedia(R.raw.want_die)
                setMessage("죽고싶어?", null)
            } else if (randNum == 7) {
                setMessage("디질래?", null)
            } else {
                playMedia(R.raw.real_no_mother)
                setMessage("진짜 엄마 없어?", null)
            }
            mInputMessage.setText("")
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    override fun onClick(v: View) {
        if (v == mSendMessage) {
            send()
        }
    }

    companion object {

        var ExitMonth = 5 //현재 날짜, 이 값은 해커톤 기준 시연용 임의 값
        var ExitDay = 19 //현재 날짜, 이 값은 해커톤 기준 시연용 임의 값

        internal var isFirst = true
        internal var isOne = true
        internal var isLastWord = false
    }
}