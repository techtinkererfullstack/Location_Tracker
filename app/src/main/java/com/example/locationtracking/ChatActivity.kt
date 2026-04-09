package com.example.locationtracking

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.locationtracking.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var chatPartnerId: String? = null
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        chatPartnerId = intent.getStringExtra("partnerId")
        val partnerName = intent.getStringExtra("partnerName") ?: "Chat"

        binding.toolbar.title = partnerName
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        listenForMessages()

        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun setupRecyclerView() {
        adapter = ChatAdapter(messages)
        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChat.adapter = adapter
    }

    private fun listenForMessages() {
        val currentUserId = auth.currentUser?.uid ?: return
        val partnerId = chatPartnerId ?: return

        // Query messages where the current user is either sender or receiver
        // Note: For a real app, you'd likely use a 'chatRoomId' or composite queries
        // This simple query fetches all messages in the collection and filters locally or via multiple listeners
        // For simplicity, we'll listen to all messages and filter for this specific conversation
        db.collection("chats")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                messages.clear()
                for (doc in snapshots!!) {
                    val message = doc.toObject(ChatMessage::class.java)
                    if ((message.senderId == currentUserId && message.receiverId == partnerId) ||
                        (message.senderId == partnerId && message.receiverId == currentUserId)) {
                        messages.add(message)
                    }
                }
                adapter.notifyDataSetChanged()
                binding.recyclerViewChat.scrollToPosition(messages.size - 1)
            }
    }

    private fun sendMessage() {
        val messageText = binding.etMessage.text.toString()
        val currentUserId = auth.currentUser?.uid
        if (messageText.isNotEmpty() && currentUserId != null && chatPartnerId != null) {
            val message = ChatMessage(
                senderId = currentUserId,
                receiverId = chatPartnerId!!,
                text = messageText,
                timestamp = System.currentTimeMillis()
            )

            db.collection("chats").add(message)
                .addOnSuccessListener {
                    binding.etMessage.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to send message", Toast.LENGTH_SHORT).show()
                }
        }
    }
}