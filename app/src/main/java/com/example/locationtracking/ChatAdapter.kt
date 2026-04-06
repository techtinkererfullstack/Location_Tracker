package com.example.locationtracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_message, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.tvMessage.text = message.text
        
        // Simple alignment logic
        if (message.senderId == currentUserId) {
            holder.tvMessage.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        } else {
            holder.tvMessage.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
        }
    }

    override fun getItemCount() = messages.size
}