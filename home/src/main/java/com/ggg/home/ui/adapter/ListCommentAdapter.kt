package com.ggg.home.ui.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ggg.common.GGGAppInterface
import com.ggg.common.utils.OnEventControlListener
import com.ggg.common.utils.SpannableObject
import com.ggg.home.R
import com.ggg.home.data.model.CommentModel
import com.ggg.home.utils.Constant
import com.rey.material.widget.RelativeLayout
import com.rey.material.widget.TextView
import java.lang.ref.WeakReference

class ListCommentAdapter : RecyclerView.Adapter<ListCommentAdapter.ViewHolder> {

    lateinit var weakContext: WeakReference<Context>
    lateinit var listener: OnEventControlListener
    lateinit var listComments: List<CommentModel>
    var isMoveToReply = false

    constructor(context: Context, listener: OnEventControlListener, listComments: List<CommentModel>, isMoveToReply : Boolean) {
        this.weakContext = WeakReference(context)
        this.listener = listener
        this.listComments = listComments
        this.isMoveToReply = isMoveToReply
    }

    fun notifyData(listComments: List<CommentModel>) {
        this.listComments = listComments
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(weakContext.get()).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listComments.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val commentModel = listComments[position]
        if (!commentModel.userComment.imageUrl.isEmpty()) {
            Glide.with(weakContext.get()!!)
                    .load(commentModel.userComment.imageUrl)
                    .placeholder(GGGAppInterface.gggApp.circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.i_avatar)
                    .into(holder.ivAvatar)
        } else {
            holder.ivAvatar.setImageResource(R.drawable.i_avatar)
        }

        holder.tvUsername.text = commentModel.userComment.nickname
        holder.tvCreateAt.text = commentModel.createdAt
        holder.tvContent.text = commentModel.content
        if (commentModel.replies.isNullOrEmpty()) {
            holder.tvReplies.visibility = View.GONE
        } else {
            holder.tvReplies.visibility = View.VISIBLE
            val spannableObject = SpannableObject()
            spannableObject.addSpan("${commentModel.replies[0].userComment.nickname}: ", Color.parseColor("#ababab"), Typeface.NORMAL)
            spannableObject.addSpan(commentModel.replies[0].content, Color.parseColor("#6f6f6f"), Typeface.NORMAL)
            spannableObject.addSpan("\n\n")

            if (commentModel.replies.size > 1) {
                spannableObject.addSpan("${commentModel.replies[1].userComment.nickname}: ", Color.parseColor("#ababab"), Typeface.NORMAL)
                spannableObject.addSpan(commentModel.replies[1].content, Color.parseColor("#6f6f6f"), Typeface.NORMAL)
                spannableObject.addSpan("\n\n")
            }

            spannableObject.addSpan("${commentModel.replies.size} Replies >", Color.parseColor("#d56070"), Typeface.NORMAL)
            holder.tvReplies.text = spannableObject.span
        }

        holder.tvReplies.setOnClickListener {
            listener.onEvent(Constant.ACTION_CLICK_ON_LIST_REPLIES_COMMENT, it, commentModel)
        }

        holder.rltComment.setOnClickListener {
            if (isMoveToReply) {
                listener.onEvent(Constant.ACTION_CLICK_ON_LIST_REPLIES_COMMENT, it, commentModel)
            }
        }
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var rltComment: RelativeLayout = itemView.findViewById(R.id.rltComment)
        var ivAvatar: ImageView = itemView.findViewById(R.id.ivAvatar)
        var tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        var tvCreateAt: TextView = itemView.findViewById(R.id.tvCreateAt)
        var tvContent: TextView = itemView.findViewById(R.id.tvContent)
        var tvReplies: TextView = itemView.findViewById(R.id.tvReplies)
    }
}