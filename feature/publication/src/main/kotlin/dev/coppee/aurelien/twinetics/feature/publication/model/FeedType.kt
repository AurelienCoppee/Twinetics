package dev.coppee.aurelien.twinetics.feature.publication.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.coppee.aurelien.twinetics.core.designsystem.R.string
import java.util.Locale

enum class FeedType(val resourceId: Int) {
    PUBLIC(string.core_designsystem_feedType_public),
    FRIENDS(string.core_designsystem_feedType_friends),
    EVENTS(string.core_designsystem_feedType_events);

    @Composable
    fun text(): String {
        return stringResource(id = resourceId)
    }

    companion object {
        fun fromString(value: String?): FeedType {
            return when (value?.uppercase(Locale.ROOT)) {
                "PUBLIC" -> PUBLIC
                "FRIENDS" -> FRIENDS
                "EVENTS" -> EVENTS
                else -> FRIENDS
            }
        }
    }
}