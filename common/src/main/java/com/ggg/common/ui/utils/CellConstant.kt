package com.ggg.common.ui.utils

/**
 * Created by tuannguyen on 10/20/17.
 */

class CellConstant private constructor() {
    companion object {
        const val Items = "items"
        const val IsCollapse = "isCollapse"
        const val Header = "header"
        const val Footer = "footer"
        const val HeaderLayout = "HeaderLayout"
        const val HeaderSimple = "HeaderSimple" // Same as item, background white
        const val FooterLayout = "FooterLayout"
        const val HeaderInfo = "HeaderInfo"
        const val FooterInfo = "FooterInfo"
        const val Left = "left"
        const val BackgroundColor = "BackgroundColor"
        const val Right = "right"
        const val LayoutId = "LayoutId"
        const val KeyParam = "keyParam"
        const val Value = "value"
        const val IsDisable = "IsDisable"
        const val Type = "type"
        const val LeftColor = "leftColor"
        const val RightColor = "rightColor"
        const val DataSource = "dataSource"
        const val DataType = "dataType"
        const val Keyboard = "keyboard"
        const val IsClickAble = "IsClickAble"
        const val Empty = "empty"
        const val DisplayInfo = "displayInfo"
        const val ItemName = "itemName"
        const val FromDate = "fromDate"
        const val ToDate = "toDate"
        const val Amount = "amount"
        const val Remark = "remark"
        const val DataObject = "DataObject"
        const val SectionHeight = "SectionHeight"
        const val Object = "Object"
        const val DATA = "DATA"
        const val MIN = "MIN"
        const val MAX = "MAX"
        const val HINT = "HINT"
        const val IS_PASSWORD = "PASSWORD"
        const val FontSize = "FontSize"
        const val IsChangeColor = "IsChangeColor"
        const val Subtitle1 = "Subtitle1"
        const val Subtitle2 = "Subtitle2"
        const val Subtitle3 = "Subtitle3"
        const val IsShowSubTitle1 = "IsShowSubTitle1"
        const val IsShowSubTitle2 = "IsShowSubTitle2"
        const val IsShowSubTitle3 = "IsShowSubTitle3"
        const val IsShowRightSubTitle1 = "IsShowRightSubTitle1"
        const val IsShowRightSubTitle2 = "IsShowRightSubTitle2"
        const val IsShowRightSubTitle3 = "IsShowRightSubTitle3"
        const val VIEW_TYPE_FOOTER = -3
        const val VIEW_TYPE_CLEAR = -999
        const val VIEW_TYPE_HEADER = -2
        const val VIEW_TYPE_ITEM = -1
    }
    enum class CellAccessoryType {
        none,
        nextIndicator, // arrow next
        downIndicator, // arrow down
        checkMark,
        detailButton // icon i
    }

    enum class CellDataType {
        STRING,
        DATE,
        TIME,
        LIST,
        MULTI
    }
}

