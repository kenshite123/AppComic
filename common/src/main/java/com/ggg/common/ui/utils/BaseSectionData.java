package com.ggg.common.ui.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by tuannguyen on 10/19/17.
 */

public class BaseSectionData {
    public List<BaseInfoCellModel> listItem = new ArrayList<>();
    public String header = "";
    public boolean headerSimple;
    public String footer = "";
    public boolean isCollapse = false;
    public String keyParam = "";
    public int headerLayoutId = CellConstant.VIEW_TYPE_HEADER;
    public int footerLayoutId = CellConstant.VIEW_TYPE_FOOTER;
    public HashMap<String, Object> headerInfo = new HashMap<>();
    public HashMap<String, Object> footerInfo = new HashMap<>();
    public boolean isClickAble = true;

    public BaseSectionData(List<BaseInfoCellModel> listItem, String header, String footer,
                           boolean isCollapse, String keyParam, int headerLayoutId,
                           int footerLayoutId, HashMap<String, Object> headerInfo,
                           HashMap<String, Object> footerInfo, boolean isClickAble) {
        this(listItem, header, false, footer, isCollapse, keyParam, headerLayoutId,
                footerLayoutId, headerInfo,
                footerInfo, isClickAble);
    }

    public BaseSectionData(List<BaseInfoCellModel> listItem, String header, boolean headerSimple,
                           String footer, boolean isCollapse, String keyParam, int headerLayoutId,
                           int footerLayoutId, HashMap<String, Object> headerInfo,
                           HashMap<String, Object> footerInfo, boolean isClickAble) {
        this.listItem = listItem;
        this.header = header;
        this.headerSimple = headerSimple;
        this.footer = footer;
        this.isCollapse = isCollapse;
        this.keyParam = keyParam;
        this.headerLayoutId = headerLayoutId;
        this.footerLayoutId = footerLayoutId;
        this.headerInfo = headerInfo;
        this.footerInfo = footerInfo;
        this.isClickAble = isClickAble;
    }
}
