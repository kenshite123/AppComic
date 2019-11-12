package com.ggg.common.ui.utils;

import android.text.InputType;
import com.ggg.common.R;
import java.util.HashMap;
import java.util.List;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by tuannguyen on 10/19/17.
 */

public class BaseInfoCellModel {
    public String leftTitle = "";
    public String rightTitle = "";
    public int leftColor = R.color.light_gray;
    public int rightColor = R.color.light_gray;
    public int layoutId;
    public String keyParam = "";
    public CellConstant.CellDataType dataType = CellConstant.CellDataType.STRING;

    public int keyBoardType = InputType.TYPE_CLASS_TEXT;
    public List<String> DataSource;
    public List<Object> DataSourceObject;
    public HashMap<String, Object> displayInfo;
    public CellConstant.CellAccessoryType cellAccessoryType = CellConstant.CellAccessoryType.none;
    public BehaviorSubject<Object> valueObject = BehaviorSubject.create();
    public PublishSubject<Object> eventObject = PublishSubject.create();
    public BehaviorSubject<String> valueString = BehaviorSubject.create();
    public Boolean isClickAble = true;
    public Boolean isChangeColor = false;
    public String minValue = "";
    public String maxValue = "";
    public String hintValue = "";
    public Boolean isDisable = false;
    public int fontSize = 0;

    public BaseInfoCellModel(String leftTitle,
                             String rightTitle,
                             int leftColor,
                             int rightColor,
                             int layoutId,
                             String keyParam,
                             CellConstant.CellDataType dataType,
                             int keyBoardType,
                             List<String> dataSource,
                             List<Object> dataSourceObject,
                             HashMap<String, Object> displayInfo,
                             CellConstant.CellAccessoryType cellAccessoryType,
                             Boolean isClickAble,
                             String min,
                             String max,
                             String hintValue,
                             String value,
                             Object obj,
                             Boolean isDisable,
                             int fontSize,
                             Boolean isChangeColor) {
        if (layoutId == -1) {
            Timber.d("PLEASE SET LAYOUT ID");
        }
        if (keyParam == null || keyParam.length() == 0) {
            Timber.d("PLEASE SET KEY PARAM");
        }
        this.leftTitle = leftTitle;
        this.rightTitle = rightTitle;
        this.leftColor = leftColor == -1 ? R.color.light_gray : leftColor;
        this.rightColor = rightColor == -1 ? R.color.light_gray : rightColor;
        this.layoutId = layoutId == -1 ? R.layout.item_info : layoutId;
        this.keyParam = keyParam;
        this.dataType = dataType;
        this.keyBoardType = keyBoardType == -1 ? InputType.TYPE_CLASS_TEXT : keyBoardType;
        DataSource = dataSource;
        DataSourceObject = dataSourceObject;
        this.displayInfo = displayInfo;
        this.cellAccessoryType = cellAccessoryType;
        this.isClickAble = isClickAble;
        this.minValue = min;
        this.maxValue = max;
        this.hintValue = hintValue;
        this.isDisable = isDisable;
        this.fontSize = fontSize;
        this.isChangeColor = isChangeColor;
        if (value != null) valueString.onNext(value);
        if (obj != null) valueObject.onNext(obj);
    }


}
