package com.brewhog.android.tradepractice;

import android.content.Context;
import android.widget.TextView;

import com.brewhog.android.tradepractice.GuideItem;

import java.util.ArrayList;
import java.util.List;

public class GuideItemPack {
    private List<GuideItem> instruction;
    public static final int TEXT_TYPE = 0;
    public static final int IMAGE_TYPE = 1;

    public GuideItemPack() {
        instruction = loadInstruction();
    }

    public List<GuideItem> getInstruction() {
        return instruction;
    }

    private List<GuideItem> loadInstruction(){
        //Добавляем части статьи, которые могут быть изображением, либо текстом
        //поле Info будет содержать либо ссылку на ресурс изображения,
        //либо индекс в массиве строк

        List<GuideItem> guideItemList = new ArrayList<>();

        GuideItem zero = new GuideItem(IMAGE_TYPE);
        guideItemList.add(zero);

        GuideItem one = new GuideItem(TEXT_TYPE);
        one.setInfo(0);
        guideItemList.add(one);

        GuideItem oneTwo = new GuideItem(TEXT_TYPE);
        oneTwo.setInfo(1);
        guideItemList.add(oneTwo);

        GuideItem two = new GuideItem(IMAGE_TYPE);
        two.setInfo(R.drawable.guide_theory);
        guideItemList.add(two);

        GuideItem three = new GuideItem(TEXT_TYPE);
        three.setInfo(2);
        guideItemList.add(three);

        GuideItem four = new GuideItem(IMAGE_TYPE);
        four.setInfo(R.drawable.guide_practice);
        guideItemList.add(four);

        GuideItem five = new GuideItem(TEXT_TYPE);
        five.setInfo(3);
        guideItemList.add(five);

        GuideItem six = new GuideItem(IMAGE_TYPE);
        six.setInfo(R.drawable.guide_chart);
        guideItemList.add(six);

        GuideItem seven = new GuideItem(TEXT_TYPE);
        seven.setInfo(4);
        guideItemList.add(seven);

        GuideItem eight = new GuideItem(IMAGE_TYPE);
        eight.setInfo(R.drawable.guide_lesson);
        guideItemList.add(eight);

        GuideItem nine = new GuideItem(TEXT_TYPE);
        nine.setInfo(5);
        guideItemList.add(nine);

        return guideItemList;
    }
}
