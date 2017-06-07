package com.app.justclap.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import com.app.justclap.fragment_types.FragmentShareRideType;
import com.app.justclap.fragment_types.Fragment_AddCity;
import com.app.justclap.fragment_types.Fragment_Address;
import com.app.justclap.fragment_types.Fragment_AntiTypeQues;
import com.app.justclap.fragment_types.Fragment_CheckboxTypeQues;
import com.app.justclap.fragment_types.Fragment_Description;
import com.app.justclap.fragment_types.Fragment_GetDestinationLocation;
import com.app.justclap.fragment_types.Fragment_GetLocation;
import com.app.justclap.fragment_types.Fragment_GetLocationAddress;
import com.app.justclap.fragment_types.Fragment_RadioTypeQues;
import com.app.justclap.fragment_types.Fragment_SkillType;
import com.app.justclap.fragment_types.Fragment_TextArea;
import com.app.justclap.fragment_types.Fragment_select_Date;
import com.app.justclap.fragment_types.Fragment_select_Day;
import com.app.justclap.fragment_types.Fragment_select_Time;
import com.app.justclap.models.ModelAddCityType;
import com.app.justclap.models.ModelAddressType;
import com.app.justclap.models.ModelAntiType;
import com.app.justclap.models.ModelCheckboxType;
import com.app.justclap.models.ModelDescriptionType;
import com.app.justclap.models.ModelGetDestinationLocationType;
import com.app.justclap.models.ModelGetLocationAddressType;
import com.app.justclap.models.ModelGetLocationType;
import com.app.justclap.models.ModelRadioType;
import com.app.justclap.models.ModelRideLocationType;
import com.app.justclap.models.ModelSelectDateType;
import com.app.justclap.models.ModelSelectDayType;
import com.app.justclap.models.ModelSelectTimeType;
import com.app.justclap.models.ModelSkillType;
import com.app.justclap.models.ModelTextAreaType;

/**
 * Created by admin on 27-11-2015.
 */
public class AdapterTabs extends FragmentPagerAdapter {

    ArrayList<Object> quesList;
    int count;
    String quesId = "";

    public AdapterTabs(FragmentManager fm, int count, ArrayList<Object> quesList) {
        super(fm);
        this.count = count;
        this.quesList = quesList;
    }


    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        Log.e("QuesList", quesList.size() + "**");

        if (quesList.get(position) instanceof ModelCheckboxType) {
            fragment = new Fragment_CheckboxTypeQues();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelDescriptionType) {
            fragment = new Fragment_Description();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelAddressType) {
            fragment = new Fragment_Address();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelRadioType) {
            fragment = new Fragment_RadioTypeQues();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelSelectDateType) {
            fragment = new Fragment_select_Date();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelGetLocationType) {
            fragment = new Fragment_GetLocation();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelSelectDayType) {
            fragment = new Fragment_select_Day();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelGetLocationAddressType) {
            fragment = new Fragment_GetLocationAddress();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelTextAreaType) {
            fragment = new Fragment_TextArea();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelSelectTimeType) {
            fragment = new Fragment_select_Time();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelAddCityType) {
            fragment = new Fragment_AddCity();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelGetDestinationLocationType) {
            fragment = new Fragment_GetDestinationLocation();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        } else if (quesList.get(position) instanceof ModelSkillType) {
            fragment = new Fragment_SkillType();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        }else if (quesList.get(position) instanceof ModelAntiType) {
            fragment = new Fragment_AntiTypeQues();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        }else if (quesList.get(position) instanceof ModelRideLocationType) {
            fragment = new FragmentShareRideType();
            Bundle b = new Bundle();
            b.putInt("position", position);
            fragment.setArguments(b);
        }


       /*else if (quesList.get(position).getInput_type().equalsIgnoreCase("2")) {
            com.app.justclap.fragment = new Fragment_RadioTypeQues();
            Bundle b = new Bundle();
            b.putString("ques", quesList.get(position).getQuestionArray());
            com.app.justclap.fragment.setArguments(b);
            // return fragment_radioTypeQues;

        } else if (quesList.get(position).getInput_type().equalsIgnoreCase("4")) {
            com.app.justclap.fragment = new Fragment_GetLocation();
            Bundle b = new Bundle();
            b.putString("ques", quesList.get(position).getQuestionArray());
            com.app.justclap.fragment.setArguments(b);
            // return fragmentGetLocation;
        } else if (quesList.get(position).getInput_type().equalsIgnoreCase("7")) {
            com.app.justclap.fragment = new Fragment_GetLocationAddress();
            Bundle b = new Bundle();
            b.putString("ques", quesList.get(position).getQuestionArray());
            com.app.justclap.fragment.setArguments(b);
            // return fragmentGetLocation;
        } else if (quesList.get(position).getInput_type().equalsIgnoreCase("8")) {
            com.app.justclap.fragment = new Fragment_TextArea();
            Bundle b = new Bundle();
            b.putString("ques", quesList.get(position).getQuestionArray());
            com.app.justclap.fragment.setArguments(b);
            // return fragmentGetLocation;
        } else if (quesList.get(position).getInput_type().equalsIgnoreCase("3")) {
            com.app.justclap.fragment = new Fragment_select_Date();
            Bundle b = new Bundle();
            b.putString("ques", quesList.get(position).getQuestionArray());
            com.app.justclap.fragment.setArguments(b);
            // return fragmentGetLocation;
        } else if (quesList.get(position).getInput_type().equalsIgnoreCase("1")) {
            com.app.justclap.fragment = new Fragment_Address();
            Bundle b = new Bundle();
            b.putString("ques", quesList.get(position).getQuestionArray());
            com.app.justclap.fragment.setArguments(b);
            // return fragmentGetLocation;
        } else if (quesList.get(position).getInput_type().equalsIgnoreCase("9")) {
            com.app.justclap.fragment = new Fragment_select_Time();
            Bundle b = new Bundle();
            b.putString("ques", quesList.get(position).getQuestionArray());
            com.app.justclap.fragment.setArguments(b);
            // return fragmentGetLocation;
        } else if (quesList.get(position).getInput_type().equalsIgnoreCase("6")) {
            com.app.justclap.fragment = new Fragment_select_Day();
            Bundle b = new Bundle();
            b.putString("ques", quesList.get(position).getQuestionArray());
            com.app.justclap.fragment.setArguments(b);
            // return fragmentGetLocation;
        } else if (quesList.get(position).getInput_type().equalsIgnoreCase("0")) {
            com.app.justclap.fragment = new Fragment_Description();
            Bundle b = new Bundle();
            b.putString("ques", quesList.get(position).getQuestionArray());
            com.app.justclap.fragment.setArguments(b);
            // return fragmentGetLocation;
        }
*/

        return fragment;


    }


    @Override
    public int getCount() {
        return count;
    }

}
