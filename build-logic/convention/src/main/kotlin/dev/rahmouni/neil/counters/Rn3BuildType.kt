package dev.coppee.aurelien.twinetics

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class Rn3BuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
