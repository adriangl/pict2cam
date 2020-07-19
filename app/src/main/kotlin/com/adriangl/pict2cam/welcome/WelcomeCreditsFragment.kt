package com.adriangl.pict2cam.welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.adriangl.pict2cam.databinding.WelcomeCreditsFragmentBinding

/**
 * [Fragment] that displays the open-source libraries and resources used in the app.
 *
 * It's the last of the steps added in [WelcomeActivity].
 */
class WelcomeCreditsFragment : Fragment() {

    companion object {
        fun newInstance(): WelcomeCreditsFragment = WelcomeCreditsFragment()
    }

    private lateinit var binding: WelcomeCreditsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        WelcomeCreditsFragmentBinding.inflate(inflater).also { binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            appintroCard.setOnClickListener {
                openWebsite("https://github.com/AppIntro/AppIntro")
            }
            androidImageCropperCard.setOnClickListener {
                openWebsite("https://github.com/ArthurHub/Android-Image-Cropper")
            }
            freeVectorCard.setOnClickListener {
                openWebsite("https://freevector.com")
            }
        }
    }

    private fun openWebsite(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}