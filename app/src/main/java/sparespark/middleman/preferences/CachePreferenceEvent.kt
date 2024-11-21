package sparespark.middleman.preferences

sealed class CachePreferenceEvent {
    data object OnClearCacheClick : CachePreferenceEvent()
}