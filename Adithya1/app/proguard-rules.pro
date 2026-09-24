# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /path/to/sdk/tools/proguard/proguard-android.txt

# Keep data models for Gson serialization/deserialization
-keepclassmembers class com.example.cargrasp.data.model.** { *; }
-keep class com.example.cargrasp.data.model.** { *; }

# Google Generative AI
-keep class com.google.ai.client.generativeai.** { *; }
