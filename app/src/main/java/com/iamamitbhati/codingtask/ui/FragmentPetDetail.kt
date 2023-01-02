package com.iamamitbhati.codingtask.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.iamamitbhati.codingtask.databinding.FragmentPetDetailBinding


class FragmentPetDetail : DialogFragment() {
    companion object {
        const val CONTENT_URL = "content_url"
        const val TAG = "fragment_pet_detail"
    }

    private lateinit var binding: FragmentPetDetailBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(CONTENT_URL)?.let {
            loadPage(it)
        } ?: kotlin.run {
            Toast.makeText(requireContext(), "URL not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPage(contentUrl: String) {
        binding.webView.apply {
            loadUrl(contentUrl)
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    return false
                }
            }
        }


    }

}