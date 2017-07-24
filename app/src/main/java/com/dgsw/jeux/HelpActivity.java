package com.dgsw.jeux;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;

public class HelpActivity extends MaterialAboutActivity {

    @Override
    @NonNull
    protected MaterialAboutList getMaterialAboutList(@NonNull final Context activityContext) {
        MaterialAboutCard.Builder faqCardBuilder = new MaterialAboutCard.Builder();

        faqCardBuilder.title("FAQ");

        faqCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_help_black_24px)
                .text("Q: 제욱스가 종료되기를 싫어합니다.")
                .subText("A: 제욱스는 일반적인 방법으로 꺼지지 않습니다.")
                .setOnLongClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Snackbar.make(((HelpActivity) activityContext).findViewById(R.id.mal_material_about_activity_coordinator_layout), "제욱스한테 \'꺼져\'라고 하세요", Snackbar.LENGTH_INDEFINITE)
                                .setAction("알겠습니다", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {}
                        }).show();
                    }
                })
                .build());
        faqCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Q: 제욱스가 할 수 있는 것은 명령 예시에 있는 것이 다인가요?")
                .subText("A: 아니요, 제욱스가 할 수 있는 것은 명령 예시에 있는 것만이 아닙니다.")
                .build());

        MaterialAboutCard.Builder helpCardBuilder = new MaterialAboutCard.Builder();

        helpCardBuilder.title("지원");

        helpCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .icon(R.drawable.ic_code_black_24px)
                .text("명령 예시")
                .subText("명령 예시를 봅니다.")
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HelpActivity.this);
                        alertDialog.setTitle(R.string.cmd_ex)
                    .setMessage(R.string.cmd_ex_content)
                    .show();
                    }
                })
                .build());

        return new MaterialAboutList.Builder()
                .addCard(faqCardBuilder.build())
                .addCard(helpCardBuilder.build())
                .build();
    }

    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.help);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
