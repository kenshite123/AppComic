package com.ggg.common.ui.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ggg.common.R;
import com.ggg.common.utils.DateTimeUtil;
import com.ggg.common.utils.LanguageManager;
import com.ggg.common.utils.StringUtil;
import com.github.underscore.$;



import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by tuannguyen on 10/19/17.
 */

public class BaseInfoCell extends ConstraintLayout {

    public BaseInfoCellModel viewModel;
    public WeakReference<Context> mContext;
    protected ImageView mRightIcon;
    Disposable subscription;
    Disposable subscriptionObject;
    HashMap<String, Object> sectionDisplay;
    protected ContentActionListenter listenter;


    public BaseInfoCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseInfoCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        mContext = new WeakReference<>(context);
    }

    public void setSectionDisplay(HashMap<String, Object> displayInfo, boolean expanded){
        this.sectionDisplay = displayInfo;
    }
    public void setViewModel(BaseInfoCellModel viewModel) {
        this.viewModel = viewModel;
        this.setLeftTitle(viewModel.leftTitle);
        this.setRightTitle(viewModel.rightTitle);
        this.setCellAccessoryType(viewModel.cellAccessoryType);
        this.setLeftTextColor(viewModel.leftColor);
        this.setRightTextColor(viewModel.rightColor);
        this.binding();
        if (viewModel.isDisable){
            this.disableStatus();
        }
        setSectionDisplay(viewModel.displayInfo, false);
    }

    //region support function

    public void setRightTitle(String value) {

    }

    public void setLeftTitle(String value) {

    }

    public void setLeftTextColor(int value) {

    }

    public void setRightTextColor(int value) {

    }

    public void disableStatus(){

    }


    public void binding() {
        if (subscription != null) {
            subscription.dispose();
        }
        subscription = viewModel.valueString.subscribe(value -> {
            onValueChange(value);
        });
    }

    public void onValueChange(String value) {

    }

    public void setCellAccessoryType(CellConstant.CellAccessoryType value) {
        if (mRightIcon != null) {
            switch (value) {
                case none: {
                    mRightIcon.setVisibility(GONE);
                    break;
                }
                case checkMark: {
                    break;
                }
                case detailButton: {
                    mRightIcon.setVisibility(VISIBLE);
                    mRightIcon.setImageResource(R.drawable.vector_drawable_checked_information);
                    android.view.ViewGroup.LayoutParams layoutParams = mRightIcon.getLayoutParams();
                    layoutParams.width = layoutParams.height = 60;
                    mRightIcon.setLayoutParams(layoutParams);
                    break;
                }
                case nextIndicator: {
                    mRightIcon.setVisibility(VISIBLE);
                    mRightIcon.setImageResource(R.drawable.ic_arrow_next_grey);
                    android.view.ViewGroup.LayoutParams layoutParams = mRightIcon.getLayoutParams();
                    layoutParams.width = layoutParams.height = 30;
                    mRightIcon.setLayoutParams(layoutParams);
                    break;
                }
                case downIndicator: {
                    mRightIcon.setVisibility(VISIBLE);
                    mRightIcon.setImageResource(R.drawable.ic_arrow_down_grey);
                    android.view.ViewGroup.LayoutParams layoutParams = mRightIcon.getLayoutParams();
                    layoutParams.width = layoutParams.height = 55;
                    mRightIcon.setLayoutParams(layoutParams);
                    break;
                }
            }
        }
    }
    //endregion

    //region event

    protected void showDatePicker() {
        Long minValue = null;
        Long maxValue = null;
        if (viewModel.minValue!=null&&viewModel.minValue.length() > 0) {
            minValue = Long.parseLong(viewModel.minValue);
        }
        if (viewModel.maxValue!=null&&viewModel.maxValue.length()>0) {
            maxValue = Long.parseLong(viewModel.maxValue);
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext.get(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    long date = DateTimeUtil.convertYearMonthDaytoMilisecond(year, monthOfYear, dayOfMonth);
                    date = DateTimeUtil.convertMiliSecondFromClientToServer(date);
                    viewModel.valueString.onNext(date + "");
                }, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        if (minValue != null)
            datePickerDialog.getDatePicker().setMinDate(minValue);
        if (maxValue != null)
            datePickerDialog.getDatePicker().setMaxDate(maxValue);
        datePickerDialog.show();
    }

    protected void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(mContext.get(), (timePicker, selectedHour, selectedMinute) -> {
            //set data
            String hourStr = selectedHour > 9 ? String.valueOf(selectedHour) : "0" + selectedHour;
            String minuteStr = selectedMinute > 9 ? String.valueOf(selectedMinute) : "0" + selectedMinute;
            String time = hourStr + ":" + minuteStr;
            viewModel.valueString.onNext(time);
        }, hour, minute, true);
        mTimePicker.setTitle(viewModel.leftTitle);
        mTimePicker.show();
    }

    protected void objectChange(int which){
        if (viewModel.DataSourceObject.get(which) != viewModel.valueObject.getValue()) {
            String selectedValue = viewModel.DataSource.get(which);
            viewModel.valueString.onNext(selectedValue);
            viewModel.valueObject.onNext(viewModel.DataSourceObject.get(which));
        }
    }
    protected void showList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(viewModel.leftTitle);
        String[] types = new String[viewModel.DataSource.size()];
        types = viewModel.DataSource.toArray(types);
        builder.setItems(types, (dialog, which) -> {
            objectChange(which);
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    protected void showMultiList() {

        String[] dataSource = new String[viewModel.DataSource.size()];
        dataSource = viewModel.DataSource.toArray(dataSource);

        boolean[] selectData = new boolean[viewModel.DataSource.size()];
        List<Object> listObjectSelected = new ArrayList<>();
        for (int i = 0; i < viewModel.DataSource.size(); i++) {
            selectData[i] = false;
            if (viewModel.valueString.getValue() != null && viewModel.valueString.getValue().contains(viewModel.DataSource.get(i))) {
                selectData[i] = true;
                listObjectSelected.add(viewModel.DataSourceObject.get(i));
            }
        }
        if (viewModel.valueObject.getValue() == null) {
            viewModel.valueObject.onNext(listObjectSelected);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        boolean[] finalSelectData = selectData;
        builder.setMultiChoiceItems(dataSource, selectData, (dialog, which, isChecked) -> finalSelectData[which] = isChecked);
        builder.setCancelable(false);
        builder.setTitle(viewModel.leftTitle);
        builder.setPositiveButton(LanguageManager.getValue("OK"), (dialog, which) -> {
            List<String> selectedList = new ArrayList<>();
            List<Object> selectedObject = new ArrayList<>();
            for (int i = 0; i < finalSelectData.length; i++) {
                if (finalSelectData[i]) {
                    selectedList.add(viewModel.DataSource.get(i));
                    selectedObject.add(viewModel.DataSourceObject.get(i));
                }
            }
            String listString = $.join(selectedList, ", ");
            viewModel.valueString.onNext(listString);
            viewModel.valueObject.onNext(selectedObject);
        });
        builder.setNeutralButton(LanguageManager.getValue("Cancel"), (dialog, which) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    //endregion

    //region Data support

    public static void setDate(BaseInfoCellModel viewModel, Long value){
        viewModel.valueString.onNext(StringUtil.toStringObject(value));
    }

    public static Long getDate(BaseInfoCellModel viewModel){
        try {
            return Long.parseLong(viewModel.valueString.getValue());
        }catch (Exception e){
            return 0L;
        }
    }

    public static void setTime(BaseInfoCellModel viewModel, String value){
        viewModel.valueString.onNext(value);
    }
    public static String getTime(BaseInfoCellModel viewModel){
        return viewModel.valueString.getValue();
    }

    protected void notifyDoAction(String action){
        if (listenter != null){
            listenter.onActionListener(action);
        }
    }

    public void setContentActionListener(ContentActionListenter listener){
        this.listenter = listener;
    }

    public interface ContentActionListenter{
        void onActionListener(String action);
    }

    //endregion

}
