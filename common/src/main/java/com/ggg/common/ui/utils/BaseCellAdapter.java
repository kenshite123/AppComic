package com.ggg.common.ui.utils;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ggg.common.R;
import com.ggg.common.ui.utils.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

/**
 * Created by tuannguyen on 10/19/17.
 */

public class BaseCellAdapter extends SectionedRecyclerViewAdapter<BaseViewHolder> {


    //region properties
    public List<BaseSectionData> dataList;
    public ItemCellClickListener listener;
    //endregion

    //region constructor
    public void updateData(List<BaseSectionData> dataItem) {
        this.dataList = dataItem;
        notifyDataSetChanged();
    }

    public void addData(List<BaseSectionData> dataItem) {
        this.dataList.addAll(dataItem);
        notifyDataSetChanged();
    }

    public BaseCellAdapter(ItemCellClickListener listener) {
        this.listener = listener;
        dataList = new ArrayList<>();
    }

    public BaseCellAdapter(List<BaseSectionData> dataList, ItemCellClickListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    //endregion

    //region override function

    @Override
    public int getSectionCount() {
        if (dataList == null) {
            return 0;
        }

        return dataList.size();
    }

    @Override
    public int getItemCount(int section) {
        if (dataList == null) {
            return 0;
        }

        return dataList.get(section).listItem.size();
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int section, boolean expanded) {
        BaseSectionData baseSectionData = dataList.get(section);
        if (baseSectionData == null) {
            return;
        }
        holder.itemView.setBackgroundResource(R.color.colorHeader);
        View headerView = holder.itemView.findViewById(R.id.header_blank_grey);
        if(null != headerView){
            holder.itemView.findViewById(R.id.header_blank_grey).setVisibility(View.GONE);
        }

        if (holder.cell != null) {
            if (baseSectionData.headerInfo != null) {
                holder.cell.setSectionDisplay(baseSectionData.headerInfo, expanded);
            }
            if (baseSectionData.isClickAble) {
                holder.cell.setOnClickListener(holder);
            }
        } else if (holder != null && holder.title != null && holder.icon != null) {
            if (!TextUtils.isEmpty(baseSectionData.header)) {
                if (baseSectionData.headerSimple) {
                    holder.title.setText(baseSectionData.header);
                    holder.title.setTextColor(ResourceUtil.getColor(R.color.black));
                    holder.itemView.setBackgroundResource(R.color.white);
                    holder.itemView.findViewById(R.id.header_blank_grey).setVisibility(View.VISIBLE);
                } else {
                    holder.title.setText(baseSectionData.header);
                    holder.title.setTextColor(ResourceUtil.getColor(R.color.gray));
                    holder.itemView.setBackgroundResource(R.color.colorHeader);
                    holder.itemView.findViewById(R.id.header_blank_grey).setVisibility(View.GONE);
                }
                holder.icon.setImageResource(expanded ?
                        R.drawable.ic_arrow_down_grey : R.drawable.ic_arrow_next_grey);
                holder.title.setVisibility(View.VISIBLE);
                holder.icon.setVisibility(View.VISIBLE);
            } else {
                holder.title.setVisibility(View.GONE);
                holder.icon.setVisibility(View.GONE);
            }
        }else {
            holder.itemView.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int section) {
        if (holder.cell != null) {
            BaseSectionData baseSectionData = dataList.get(section);
            if (baseSectionData.footerInfo != null) {
                holder.cell.setSectionDisplay(baseSectionData.footerInfo, false);
            }
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder, int section,
                                 int relativePosition, int absolutePosition) {
        BaseInfoCellModel object = getItem(section, relativePosition);
        if (object != null && viewHolder.cell != null) {
            viewHolder.cell.setViewModel(object);
            if (!object.isClickAble) {
                viewHolder.cell.setOnClickListener(viewHolder);
                viewHolder.cell.setContentActionListener(viewHolder);
            }
            if (object.isDisable){
                viewHolder.cell.setOnClickListener(item -> Timber.d("this cell is disable"));
            }

            if (object.displayInfo != null && object.displayInfo.get(CellConstant.BackgroundColor) != null){
                int backgroundColor = (int) object.displayInfo.get(CellConstant.BackgroundColor);
                viewHolder.itemView.setBackgroundColor(ResourceUtil.getColor(backgroundColor));
                viewHolder.cell.setBackgroundColor(ResourceUtil.getColor(backgroundColor));
            }else {
                viewHolder.itemView.setBackgroundColor(ResourceUtil.getColor(R.color.white));
                viewHolder.cell.setBackgroundColor(ResourceUtil.getColor(R.color.white));
            }
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item_header, parent, false);
            return new BaseViewHolder(view, this, viewType);
        } else if (viewType == VIEW_TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item_header, parent, false);
            return new BaseViewHolder(view, this, viewType);
        } else {
            if (viewType == CellConstant.VIEW_TYPE_CLEAR) {
                viewType = R.layout.item_header_clear;
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new BaseViewHolder(view, this, viewType);
        }

    }

    @Override
    public int getFooterViewType(int section) {
        return dataList.get(section).footerLayoutId;
    }

    @Override
    public int getHeaderViewType(int section) {
        return dataList.get(section).headerLayoutId;
    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        return getItem(section, relativePosition).layoutId;
    }
    //endregion

    //region support function
    public BaseInfoCellModel getItem(int section, int relativePosition) {
        BaseSectionData sectionData = dataList.get(section);
        BaseInfoCellModel object = sectionData.listItem.get(relativePosition);
        return object;
    }

    public interface ItemCellClickListener {
        public void cellClicked(BaseInfoCellModel viewModel, int section, int position);

        public void buttonClicked(BaseInfoCell cell,int section, int position,String action);
    }

    @Override
    public boolean isEnableExpand(int section) {
        BaseSectionData sectionData = dataList.get(section);
        if (!TextUtils.isEmpty(sectionData.header) || sectionData.isClickAble) {
            return true;
        }
        return false;
    }
    //endregion
}
