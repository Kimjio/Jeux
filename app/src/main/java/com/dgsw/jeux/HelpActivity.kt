package com.dgsw.jeux

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.os.Bundle

import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList

class HelpActivity : MaterialAboutActivity() {

    override fun getMaterialAboutList(activityContext: Context): MaterialAboutList {
        val faqCardBuilder = MaterialAboutCard.Builder()

        faqCardBuilder.title("FAQ")

        faqCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_help_black_24px)
                .text("Q: 제욱스가 종료되기를 싫어합니다.")
                .subText("A: 제욱스는 일반적인 방법으로 꺼지지 않습니다.")
                .setOnLongClickAction {
                    Snackbar.make((activityContext as HelpActivity).findViewById(R.id.mal_material_about_activity_coordinator_layout), "제욱스한테 \'꺼져\'라고 하세요", Snackbar.LENGTH_INDEFINITE)
                            .setAction("알겠습니다") { }.show()
                }
                .build())
        faqCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .text("Q: 제욱스가 할 수 있는 것은 명령 예시에 있는 것이 다인가요?")
                .subText("A: 아니요, 제욱스가 할 수 있는 것은 명령 예시에 있는 것만이 아닙니다.")
                .build())

        val helpCardBuilder = MaterialAboutCard.Builder()

        helpCardBuilder.title("지원")

        helpCardBuilder.addItem(MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_code_black_24px)
                .text("명령 예시")
                .subText("명령 예시를 봅니다.")
                .setOnClickAction {
                    val alertDialog = AlertDialog.Builder(this@HelpActivity)
                    alertDialog.setTitle(R.string.cmd_ex)
                            .setMessage(R.string.cmd_ex_content)
                            .show()
                }
                .build())

        return MaterialAboutList.Builder()
                .addCard(faqCardBuilder.build())
                .addCard(helpCardBuilder.build())
                .build()
    }

    override fun getActivityTitle(): CharSequence? {
        return getString(R.string.help)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
