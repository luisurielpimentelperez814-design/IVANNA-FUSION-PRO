# IVANNA-FUSION-PRO ProGuard / R8 rules
# FIX #7: mantener funciones JNI native para que R8 no las elimine

# Mantener el companion object de DSPBridge (llamado por reflexión en tests)
-keep class com.ivanna.fusion.pro.DSPBridge { *; }

# Mantener todas las funciones declaradas como 'external' (JNI bridge)
# R8 las eliminaría por no detectar llamadas desde Kotlin/Java
-keepclasseswithmembernames class * {
    native <methods>;
}

# Compose — las clases de UI generadas por el compilador no deben eliminarse
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# ViewModel — inyectado por viewModel(), no referenciado directamente
-keep class * extends androidx.lifecycle.ViewModel { *; }

# Navigation Compose
-keep class androidx.navigation.** { *; }
