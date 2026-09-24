package com.example.cargrasp.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.cargrasp.BuildConfig

/**
 * Manages Gemini API Key retrieval.
 * Prioritizes keys configured securely in BuildConfig (from local.properties),
 * falling back to local SharedPreferences if set via in-app configuration.
 */
class ApiKeyStorage(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * Gets the active Gemini API key.
     * Checks BuildConfig first, then local storage.
     */
    fun getApiKey(): String {
        // 1. First check BuildConfig (injected from local.properties or environment variable)
        val buildKey = BuildConfig.GEMINI_API_KEY.trim()
        if (buildKey.isNotBlank() && !buildKey.equals("YOUR_API_KEY_HERE", ignoreCase = true)) {
            return buildKey
        }

        // 2. Fallback to runtime user configured key
        return prefs.getString(KEY_GEMINI_API, "")?.trim().orEmpty()
    }

    /**
     * Saves a user-provided API key for runtime testing without rebuilding.
     */
    fun saveCustomApiKey(key: String) {
        prefs.edit().putString(KEY_GEMINI_API, key.trim()).apply()
    }

    /**
     * Clears any locally stored custom API key.
     */
    fun clearCustomApiKey() {
        prefs.edit().remove(KEY_GEMINI_API).apply()
    }

    /**
     * Checks if a valid API key is present.
     */
    fun isApiKeyConfigured(): Boolean {
        return getApiKey().isNotBlank()
    }

    /**
     * Indicates whether the current active key came from BuildConfig or local storage.
     */
    fun isFromBuildConfig(): Boolean {
        val buildKey = BuildConfig.GEMINI_API_KEY.trim()
        return buildKey.isNotBlank() && !buildKey.equals("YOUR_API_KEY_HERE", ignoreCase = true)
    }

    companion object {
        private const val PREF_NAME = "cargrasp_api_prefs"
        private const val KEY_GEMINI_API = "custom_gemini_api_key"
    }
}
