package com.maxrave.data.mediaservice

internal fun artworkImageKey(url: String): String {
    val trimmed = url.substringBefore('?')
    val sizeSpec = trimmed.lastIndexOf('=')
    return if (sizeSpec > 0) trimmed.substring(0, sizeSpec) else trimmed
}

internal fun shouldReplaceStoredArtwork(
    stored: String?,
    candidate: String,
): Boolean {
    if (stored.isNullOrBlank()) return true
    if (stored == candidate) return false
    return artworkImageKey(stored) == artworkImageKey(candidate)
}
