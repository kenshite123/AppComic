package com.ggg.common.ui.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ggg.common.R;
import com.ggg.common.ui.utils.sectionedrecyclerview.SectionedViewHolder;


/**
 * Created by tuannguyen on 10/19/17.
 */

public class BaseViewHolder extends SectionedViewHolder implements View.OnClickListener,BaseInfoCell.ContentActionListenter {

    //region properties
    View itemView;
    TextView title;
    ImageView icon;
    BaseInfoCell cell;
    BaseCellAdapter adapter;
    //endregion

    //region constructor
    BaseViewHolder(View itemView, BaseCellAdapter adapter, int viewType) {
        super(itemView);
        this.itemView = itemView;
        if (viewType == CellConstant.VIEW_TYPE_HEADER) {
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            icon = itemView.findViewById(R.id.caret);
        } else if (viewType == CellConstant.VIEW_TYPE_FOOTER) {
            itemView.setOnClickListener(this);
        } else {
            this.cell = itemView.findViewById(R.id.item);
        }
        this.adapter = adapter;
    }

    //endregion
    @Override
    public void onClick(View view) {
        if (isFooter()) {
            // ignore footer clicks
            return;
        }
        if (isHeader()) {
            adapter.toggleSectionExpanded(getRelativePosition().section());
        } else {
            adapter.listener.cellClicked(adapter.dataList.get(getRelativePosition().section()).listItem.get(getRelativePosition().relativePos()), getRelativePosition().section(), getRelativePosition().relativePos());
        }
    }

    @Override
    public void onActionListener(String action) {
        adapter.listener.buttonClicked(cell, getRelativePosition().section(), getRelativePosition().relativePos(),action);
    }
}
