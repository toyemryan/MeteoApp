package com.example.meteoapp.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.meteoapp.R


class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       return  inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ss = SpannableString("Toyem Ryan")
        val clickableSpan: ClickableSpan = object : ClickableSpan() {

            override fun onClick(textView: View) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://github.com/toyemryan/MeteoApp")
                activity!!.startActivity(i)            }
        }
        ss.setSpan(clickableSpan, 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val sss = SpannableString("Djouaka Kelefack Lionel")
        val clickableSpann: ClickableSpan = object : ClickableSpan() {

            override fun onClick(textView: View) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("https://github.com/Djouaka-kelefack-lionel")
                activity!!.startActivity(i)            }
        }
        sss.setSpan(clickableSpann, 0, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        val textView1 = getView()?.findViewById(R.id.name1) as TextView
        val textView2 = getView()?.findViewById(R.id.name2) as TextView
        textView1.text = ss
        textView2.text = sss
        textView1.movementMethod = LinkMovementMethod.getInstance()
        textView2.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onResume() {
        super.onResume()
        val toolbarTitle = (activity as AppCompatActivity).findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = getString (R.string.about)
    }

}