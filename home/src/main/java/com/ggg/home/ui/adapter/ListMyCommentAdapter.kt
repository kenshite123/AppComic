package com.ggg.home.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.home.R
import com.ggg.home.data.model.CommentModel
import com.ggg.home.utils.Constant
import com.rey.material.widget.RelativeLayout
import java.lang.ref.WeakReference

class ListMyCommentAdapter : RecyclerView.Adapter<ListMyCommentAdapter.ViewHolder> {
    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listComments: List<CommentModel>

    constructor(context: Context, listener: OnEventControlListener, listComments: List<CommentModel>) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listComments = listComments
    }

    fun notifyData(listComments: List<CommentModel>) {
        this.listComments = listComments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_my_comment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listComments.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentModel = listComments[position]
        holder.tvComment.text = commentModel.content

        if ("comment" == commentModel.type) {
            holder.tvCommentParent.visibility = View.GONE
        } else {
            holder.tvCommentParent.visibility = View.VISIBLE
            holder.tvCommentParent.text = commentModel.replies[0].content
        }

        Glide.with(weakContext.get())
                .load(commentModel.comicModel.imageUrl)
                .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivComic)

        holder.tvComicTitle.text = commentModel.comicModel.title
        holder.tvCreateAt.text = commentModel.createdAt

        holder.btnDeleteComment.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_BUTTON_DELETE_COMMENT, it, position)
        }

        holder.rltMyComment.setOnClickListener {
            if ("comment" == commentModel.type) {
                listener.onEvent(Constant.ACTION_CLICK_ON_ITEM_MY_COMMENT, it, commentModel.commentId)
            } else {
                listener.onEvent(Constant.ACTION_CLICK_ON_ITEM_MY_COMMENT, it, commentModel.replies[0].commentId)
            }
        }

        holder.cltComicInfo.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_COMIC, it, commentModel.comicModel.id)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rltMyComment: RelativeLayout = itemView.findViewById(R.id.rltMyComment)
        var tvComment: TextView = itemView.findViewById(R.id.tvComment)
        var tvCommentParent: TextView = itemView.findViewById(R.id.tvCommentParent)
        var cltComicInfo: ConstraintLayout = itemView.findViewById(R.id.cltComicInfo)
        var ivComic: ImageView = itemView.findViewById(R.id.ivComic)
        var tvComicTitle: TextView = itemView.findViewById(R.id.tvComicTitle)
        var tvCreateAt: TextView = itemView.findViewById(R.id.tvCreateAt)
        var btnDeleteComment: RelativeLayout = itemView.findViewById(R.id.btnDeleteComment)
    }
}