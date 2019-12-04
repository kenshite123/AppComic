package com.ggg.home.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.SpannableObject
import com.ggg.home.R
import com.ggg.home.data.model.CategoryOfComicModel
import com.ggg.home.data.model.ComicRankWithCategoryModel
import com.ggg.home.utils.Constant
import com.ggg.home.utils.Utils
import kotlinx.android.synthetic.main.fragment_comic_detail.*
import java.lang.ref.WeakReference

class ListComicRankingAdapter : RecyclerView.Adapter<ListComicRankingAdapter.ViewHolder> {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listComic: List<ComicRankWithCategoryModel>

    constructor(context: Context, listener: OnEventControlListener, listComic: List<ComicRankWithCategoryModel>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listComic = listComic
    }

    fun notifyData(listComic: List<ComicRankWithCategoryModel>) {
        this.listComic = listComic
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_comic_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listComic.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comicRankWithCategoryModel = listComic[position]
        val comicRankModel = comicRankWithCategoryModel.comicRankModel

        Glide.with(weakContext.get())
                .load(comicRankModel?.imageUrl)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.ivComic)

        when (position) {
            0 -> holder.tvRank.setBackgroundResource(R.drawable.bg_rank_top1)
            1 -> holder.tvRank.setBackgroundResource(R.drawable.bg_rank_top2)
            2 -> holder.tvRank.setBackgroundResource(R.drawable.bg_rank_top3)
            else -> holder.tvRank.setBackgroundResource(R.drawable.bg_rank_another)
        }

        holder.tvRank.text = "${position + 1}"
        holder.tvTitle.text = comicRankModel?.title

        val spanLastChap = SpannableObject()
        spanLastChap.addSpan("Up to ")
        spanLastChap.addSpan("${comicRankModel?.latestChapter}", Color.BLACK, Typeface.BOLD)
        holder.tvLatestChapter.text = spanLastChap.span


        holder.tvRate.text = "Rate: ${comicRankModel?.rate}"
        holder.tvView.text = "Views: ${comicRankModel?.viewed?.let { Utils.formatNumber(it) }}"

        if (comicRankWithCategoryModel.categories.isNullOrEmpty()) {
            holder.rvListCategory.visibility = View.GONE
        } else {
            holder.rvListCategory.visibility = View.VISIBLE
            val categories: ArrayList<CategoryOfComicModel> = arrayListOf()
            if (comicRankWithCategoryModel.categories!!.count() > 2) {
                categories.add(comicRankWithCategoryModel.categories!![0])
                categories.add(comicRankWithCategoryModel.categories!![1])
            } else {
                categories.addAll(comicRankWithCategoryModel.categories!!)
            }

            val listCategoryComicDetailAdapter = ListCategoryComicDetailAdapter(weakContext.get()!!, listener, categories)
            holder.rvListCategory.setHasFixedSize(true)
            holder.rvListCategory.layoutManager = GridLayoutManager(weakContext.get(), 2)
            holder.rvListCategory.adapter = listCategoryComicDetailAdapter
        }

        holder.ctlComic.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_COMIC, it, comicRankWithCategoryModel)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ctlComic: ConstraintLayout = itemView.findViewById(R.id.ctlComic)
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
        var tvRank: TextView = itemView.findViewById(R.id.tvRank)
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        var rvListCategory: RecyclerView = itemView.findViewById(R.id.rvListCategory)
        var tvLatestChapter: TextView = itemView.findViewById(R.id.tvLatestChapter)
        var tvRate: TextView = itemView.findViewById(R.id.tvRate)
        var tvView: TextView = itemView.findViewById(R.id.tvView)
    }
}