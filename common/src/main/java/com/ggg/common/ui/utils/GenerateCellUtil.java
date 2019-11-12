package com.ggg.common.ui.utils;

import com.github.underscore.$;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ggg.common.ui.utils.CellConstant.VIEW_TYPE_FOOTER;
import static com.ggg.common.ui.utils.CellConstant.VIEW_TYPE_HEADER;

/**
 * Created by tuannguyen on 10/20/17.
 */

public class GenerateCellUtil {

    /**
     * this will be update cell value and cell object
     * @param dataList
     * @param key
     * @param value
     * @param obj
     *
     */
    public static void updateCellValueObject(List<BaseSectionData> dataList, String key, String value, Object obj){

        $.each(dataList, section -> {
            $.each(section.listItem, item -> {
                if (item.keyParam.equalsIgnoreCase(key)){
                    if (value != null)
                        item.valueString.onNext(value);
                    if (obj != null) {
                        item.valueObject.onNext(obj);
                    }
                }
            });
        });
    }

    /**
     * this will be update displayInfo base on cell key
     * @param dataList
     * @param key
     * @param displayInfo
     */
    public static void updateDisplayInfo(List<BaseSectionData> dataList, String key, HashMap<String, Object> displayInfo){
        $.each(dataList, section -> {
            $.each(section.listItem, item -> {
                if (item.keyParam.equalsIgnoreCase(key)){
                    item.displayInfo = displayInfo;
                }
            });
        });
    }

    /**
     * this will be update cell value
     * @param dataList
     * @param key
     * @param value
     */
    public static void updateCellValue(List<BaseSectionData> dataList, String key, String value){
        updateCellValueObject(dataList, key, value, null);
    }

    /**
     * this will be update cell object
     * @param dataList
     * @param key
     * @param obj
     */
    public static void updateCellObject(List<BaseSectionData> dataList, String key, Object obj){
        updateCellValueObject(dataList, key, null, obj);
    }

    /**
     * this will be remove all cell with Key
     * @param dataList
     * @param key
     */
    public static void removeCell(List<BaseSectionData> dataList, String key){
        $.each(dataList, section -> {
            section.listItem = $.filter(section.listItem, item -> !item.keyParam.equalsIgnoreCase(key));
        });
    }

    /**
     * this will be remove all cell contains Key
     * @param dataList
     * @param key
     */
    public static List<BaseSectionData> removeCellContains(List<BaseSectionData> dataList, String key){
        List<BaseSectionData> result = new ArrayList<>();
        $.each(dataList, section -> {
            List<BaseInfoCellModel> items = $.filter(section.listItem, item -> !item.keyParam.contains(key));
            BaseSectionData newSection = new BaseSectionData(items, section.header,
                    section.headerSimple, section.footer, section.isCollapse, section.keyParam,
                    section.headerLayoutId, section.footerLayoutId, section.headerInfo,
                    section.footerInfo, section.isClickAble);
            result.add(newSection);
        });
        return result;
    }

    /**
     * this will be remove all section with key
     * @param dataList
     * @param key
     * @return
     */
    public static List<BaseSectionData> removeSection(List<BaseSectionData> dataList, String key){
        return $.filter(dataList, section -> !section.keyParam.equalsIgnoreCase(key));
    }

    /**
     * this will be insert below this key
     * @param dataList
     * @param key
     * @param cell
     */
    public static void insertCell(List<BaseSectionData> dataList, String key, BaseInfoCellModel cell){
        $.each(dataList, section -> {
            int indexPath = searchCellIndexPath(section, key);
            if (indexPath > 0){
                section.listItem.add(indexPath, cell);
            }
        });
    }

    /**
     * this will be insert below this key
     * @param dataList
     * @param key
     * @param sectionData
     */
    public static void insertSection(List<BaseSectionData> dataList, String key, BaseSectionData sectionData){
        int indexPath = searchSectionIndexPath(dataList, key);
        if (indexPath > 0){
            dataList.add(indexPath, sectionData);
        }
    }

    /**
     * this will be insert at index
     * @param dataList
     * @param index
     * @param sectionData
     */
    public static void addSection(List<BaseSectionData> dataList, int index, BaseSectionData sectionData){
        dataList.add(index, sectionData);
    }

    /**
     * this will be insert at last index
     * @param dataList
     * @param sectionData
     */
    public static void addSection(List<BaseSectionData> dataList, BaseSectionData sectionData){
        dataList.add(sectionData);
    }

    /**
     * this will be insert at index
     * @param dataList
     * @param sectionKey
     * @param index
     * @param cell
     */
    public static void addCell(List<BaseSectionData> dataList, String sectionKey, int index, BaseInfoCellModel cell){
        $.each(dataList, section -> {
            if (section.keyParam.equalsIgnoreCase(sectionKey)){
                section.listItem.add(index, cell);
            }
        });
    }

    /**
     * this will be insert at index
     * @param dataList
     * @param sectionKey
     * @param index
     * @param cell
     */
    public static void addAllCell(List<BaseSectionData> dataList, String sectionKey, int index, List<BaseInfoCellModel> cell){
        $.each(dataList, section -> {
            if (section.keyParam.equalsIgnoreCase(sectionKey)){
                section.listItem.addAll(index, cell);
            }
        });
    }

    /**
     * this will be add cell at last index
     * @param dataList
     * @param sectionKey
     * @param cell
     */
    public static void addCell(List<BaseSectionData> dataList, String sectionKey, BaseInfoCellModel cell){
        $.each(dataList, section -> {
            if (section.keyParam.equalsIgnoreCase(sectionKey)){
                section.listItem.add(cell);
            }
        });
    }

    /**
     * this will be return index of cell base on cell key
     * @param section
     * @param key
     * @return
     */
    public static int searchCellIndexPath(BaseSectionData section, String key){
        int indexPath = -1;
        List<BaseInfoCellModel> list = section.listItem;
        for (int i=0;i<list.size();i++){
            BaseInfoCellModel item = list.get(i);

            if (item.keyParam.equalsIgnoreCase(key)){
                indexPath = i;
            }
        }
        return indexPath;
    }

    /**
     * this will be return index of section base on section key
     * @param list
     * @param key
     * @return
     */
    public static int searchSectionIndexPath(List<BaseSectionData> list, String key){
        int indexPath = 0;
        for (int i=0;i<list.size();i++){
            BaseSectionData item = list.get(i);
            if (item.keyParam.equalsIgnoreCase(key)){
                indexPath = i;
            }
        }
        return indexPath;
    }

    /**
     * generate list data for adapter base on HashMap data
     * @param data
     * @return
     */
    public static ArrayList<BaseSectionData> generate(List<HashMap<String, Object>> data) {
        ArrayList<BaseSectionData> result = new ArrayList<>();
        $.each(data, section -> {
            if (section.get(CellConstant.Items) != null) {
                result.add(generateSection(section));
            }
        });

        return result;
    }

    /**
     * generate section base on HashMap section data
     * @param section
     * @return
     */
    public static BaseSectionData generateSection(HashMap<String, Object> section) {
        List<HashMap<String, Object>> itemData = (List<HashMap<String, Object>>) section.get(CellConstant.Items);
        List<BaseInfoCellModel> items = new ArrayList<>();
        String header =  getString(section, CellConstant.Header);
        String footer =  getString(section, CellConstant.Footer);
        String sectionKey = getString(section, CellConstant.KeyParam);
        Integer headerLayout = getInt(section, CellConstant.HeaderLayout, VIEW_TYPE_HEADER);
        Boolean headerSimple = getBool(section, CellConstant.HeaderSimple, false);
        Integer footerLayout = getInt(section, CellConstant.FooterLayout, VIEW_TYPE_FOOTER);
        HashMap<String, Object> headerInfo = (HashMap<String, Object>) section.get(CellConstant.HeaderInfo);
        HashMap<String, Object> footerInfo = (HashMap<String, Object>) section.get(CellConstant.FooterInfo);
        Boolean isCollapse = getBool(section, CellConstant.IsCollapse, false);
        Boolean isClickAble = getBool(section, CellConstant.IsClickAble, true);
        $.each(itemData, item -> items.add(generateCell(item)));
        return new BaseSectionData(items, header, headerSimple, footer, isCollapse, sectionKey, headerLayout, footerLayout, headerInfo, footerInfo, isClickAble);
    }

    /**
     * generate cell base on HashMap cell data
     * @param item
     * @return
     */
    public static BaseInfoCellModel generateCell(HashMap<String, Object> item) {
        String left = getString(item, CellConstant.Left);
        String right = getString(item, CellConstant.Right);
        int layoutID = getInt(item, CellConstant.LayoutId);
        CellConstant.CellAccessoryType cellAccessoryType = getCellAccessoryType(item, CellConstant.Type);
        String keyParam = getString(item, CellConstant.KeyParam);
        CellConstant.CellDataType dataType = getCellDataType(item, CellConstant.DataType);
        List<String> list = getListString(item, CellConstant.DataSource);
        int leftColor = getInt(item, CellConstant.LeftColor);
        int rightColor = getInt(item, CellConstant.RightColor);
        String value = getString(item, CellConstant.Value);
        List<Object> dataListObject = getListObject(item, CellConstant.DataObject);
        Object obj = item.get(CellConstant.Object);
        int keyBoard = getInt(item, CellConstant.Keyboard);
        Boolean isClickAble = getBool(item, CellConstant.IsClickAble);
        String min = getString(item, CellConstant.MIN);
        String max = getString(item, CellConstant.MAX);
        String hintValue = getString(item, CellConstant.HINT);
        Boolean isDisable = getBool(item, CellConstant.IsDisable);
        int fontSize = getInt(item, CellConstant.FontSize);
        Boolean isChangeColor = getBool(item, CellConstant.IsChangeColor);
        HashMap<String, Object> displayInfo = item.get(CellConstant.DisplayInfo) == null ? new HashMap<>() : (HashMap<String, Object>) item.get(CellConstant.DisplayInfo);

        return new BaseInfoCellModel(left, right, leftColor,
                rightColor, layoutID, keyParam, dataType, keyBoard, list,
                dataListObject, displayInfo, cellAccessoryType, isClickAble,
                min, max,hintValue, value, obj, isDisable, fontSize, isChangeColor);
    }

    /**
     * this is loop of data list
     * @param data
     * @param cell
     */
    public static void collectData(List<BaseSectionData> data, ItemCell cell){
        $.each(data, section -> {
            $.each(section.listItem, item -> {
                cell.itemCell(item);
            });
        });
    }

    public interface ItemCell {
        public void itemCell(BaseInfoCellModel cellModel);
    }

    /**
     * this will be return cell base on cell key
     * @param data
     * @param keyParam
     * @return
     */
    public static BaseInfoCellModel findCellModel(List<BaseSectionData> data, String keyParam){
        List<BaseInfoCellModel> listResult = new ArrayList<>();
        $.each(data, section -> {
            $.each(section.listItem, item -> {
                if (item.keyParam.equalsIgnoreCase(keyParam)){
                    listResult.add(item);
                }
            });
        });
        return listResult.size() > 0 ? listResult.get(0) : null;
    }


    public static List<BaseInfoCellModel> findAllCellModel(List<BaseSectionData> data, String keyParam){
        List<BaseInfoCellModel> listResult = new ArrayList<>();
        $.each(data, section -> {
            $.each(section.listItem, item -> {
                if (item.keyParam.equalsIgnoreCase(keyParam)){
                    listResult.add(item);
                }
            });
        });
        return listResult;
    }

    /**
     * this will be return section base on section key
     * @param data
     * @param keyParam
     * @return
     */
    public static BaseSectionData findSectionModel(List<BaseSectionData> data, String keyParam){
        List<BaseSectionData> listResult = new ArrayList<>();
        $.each(data, section -> {
            if (section.keyParam.equalsIgnoreCase(keyParam)){
                listResult.add(section);
            }
        });
        return listResult.size() > 0 ? listResult.get(0) : null;
    }


    //region support function
    public static String getString(HashMap<String, Object> section, String key) {
        return section.get(key) == null ? "" : (String)section.get(key);
    }
    public static Boolean getBool(HashMap<String, Object> section, String key) {
        return getBool(section, key, false);
    }
    public static Boolean getBool(HashMap<String, Object> section, String key, boolean defaultValue) {
        return section.get(key) == null ? defaultValue : (Boolean) section.get(key);
    }

    public static int getInt(HashMap<String, Object> section, String key) {
        return getInt(section, key, -1);
    }
    public static int getInt(HashMap<String, Object> section, String key, int defaultValue) {
        return section.get(key) == null ? defaultValue : (int) section.get(key);
    }

    public static Double getDouble(HashMap<String, Object> section, String key) {
        return getDouble(section, key, 0.0);
    }
    public static Double getDouble(HashMap<String, Object> section, String key, Double defaultValue) {
        if (section == null) return defaultValue;
        return section.get(key) == null ? defaultValue : (Double) section.get(key);
    }


    public static CellConstant.CellAccessoryType getCellAccessoryType(HashMap<String, Object> section, String key) {
        return section.get(key) == null ? CellConstant.CellAccessoryType.none : (CellConstant.CellAccessoryType) section.get(key);
    }
    public static CellConstant.CellDataType getCellDataType(HashMap<String, Object> section, String key) {
        return section.get(key) == null ? CellConstant.CellDataType.STRING : (CellConstant.CellDataType) section.get(key);
    }

    public static List<String> getListString(HashMap<String, Object> section, String key) {
        return section.get(key) == null ? new ArrayList<>() : (List<String>) section.get(key);
    }

    public static List<Object> getListObject(HashMap<String, Object> section, String key) {
        return section.get(key) == null ? new ArrayList<>() : (List<Object>) section.get(key);
    }
    //endregion
}
