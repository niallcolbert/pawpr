package com.neddoesdev.pawpr.activities.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.neddoesdev.pawpr.R

class MessageFragment : Fragment() {

    private lateinit var messageViewModel: MessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        messageViewModel =
            ViewModelProvider(this).get(MessageViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_message, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        messageViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}