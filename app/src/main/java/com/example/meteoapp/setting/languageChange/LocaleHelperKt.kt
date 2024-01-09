/*
 * Copyright (c) 09/01/2024 Toyem Tezem Ryan Parfait & Djouaka Kelefack Lionel all rights reserved
 */

package com.example.meteoapp.setting.languageChange

import android.content.Context
import android.content.SharedPreferences
import android.os.LocaleList
import androidx.preference.PreferenceManager
import java.util.Locale

/**
 * implementazione per default del [LocaleHelperKt].
 * questa pagina contenente più classe, ci permette di salvare il cambiamento di lingua
 * scelto dall'utente
 * ovviamente la cosa piu importante è il onAttach nel Mainactivity che ricorda all'avvio dell'app
 * della lingua scelta
 */
open class DefaultLocaleHelper private constructor(context: Context) : BaseLocaleHelpe(context) {
    companion object {
        @Volatile
        private var instance: LocaleHelperKt? = null
        private var LOCK: Any = Any()

        fun getInstance(context: Context): LocaleHelperKt {
            synchronized(LOCK) {
                if (instance == null) instance = DefaultLocaleHelper(context)
                return instance!!
            }
        }
    }

    override fun setCurrentLocale(language: String): Context {
        return setLocale(context, language)
    }

    override fun onAttach(defaultLanguage: String?): Context {
        val lang = getPersistedLocale(defaultLanguage ?: Locale.getDefault().language)
        return setLocale(context, lang)
    }

    override fun getCurrentLocale(): String =
        getPersistedLocale(Locale.getDefault().language)
}

abstract class BaseLocaleHelpe(internal val context: Context) :
    LocaleHelperKt {
    companion object {
        private const val SELECTED_LANGUAGE = "LocaleHelperKt_SelectedLanguage"
    }


    internal fun getPersistedLocale(defaultLocale: String): String {
        return cacheStorage.getString(SELECTED_LANGUAGE, defaultLocale) ?: defaultLocale
    }

    private val cacheStorage: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(
            context
        )
    }


    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun baseSetLocale(context: Context, newLocale: Locale): Context {
        var tmpContext = context
        val res = tmpContext.resources
        val configuration = res.configuration
        configuration.setLocale(newLocale)
        val localeList = LocaleList(newLocale)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)
        tmpContext = tmpContext.createConfigurationContext(configuration)
        return tmpContext
    }

    /**
     * setta la lingua
     */
    internal fun setLocale(context: Context, newLocale: String): Context {
        cacheStorage.edit().apply {
            putString(SELECTED_LANGUAGE, newLocale)
            apply()
        }
        val locale = Locale(newLocale)
        return baseSetLocale(context, locale)
    }
}

interface LocaleHelperKt {
    fun onAttach(defaultLanguage: String? = null): Context
    fun getCurrentLocale(): String
    fun setCurrentLocale(language: String): Context
}

