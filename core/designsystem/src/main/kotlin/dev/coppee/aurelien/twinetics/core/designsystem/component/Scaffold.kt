/*
 * Copyright (C) 2025 Aurélien Coppée
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.coppee.aurelien.twinetics.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.EaseInOutQuint
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.window.core.layout.WindowHeightSizeClass.Companion.COMPACT
import dev.coppee.aurelien.twinetics.core.config.LocalConfigHelper
import dev.coppee.aurelien.twinetics.core.designsystem.BottomBarItem
import dev.coppee.aurelien.twinetics.core.designsystem.LocalNavAnimatedVisibilityScope
import dev.coppee.aurelien.twinetics.core.designsystem.LocalSharedTransitionScope
import dev.coppee.aurelien.twinetics.core.designsystem.R.color
import dev.coppee.aurelien.twinetics.core.designsystem.TopAppBarAction
import dev.coppee.aurelien.twinetics.core.designsystem.component.TopAppBarStyle.HOME
import dev.coppee.aurelien.twinetics.core.designsystem.component.TopAppBarStyle.LARGE
import dev.coppee.aurelien.twinetics.core.designsystem.component.TopAppBarStyle.SMALL
import dev.coppee.aurelien.twinetics.core.designsystem.component.TopAppBarStyle.TRANSPARENT
import dev.coppee.aurelien.twinetics.core.designsystem.component.topAppBar.Rn3LargeTopAppBar
import dev.coppee.aurelien.twinetics.core.designsystem.component.topAppBar.Rn3SmallTopAppBar
import dev.coppee.aurelien.twinetics.core.designsystem.icons.Logo
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.Rn3PaddingValues
import dev.coppee.aurelien.twinetics.core.designsystem.paddingValues.toRn3PaddingValues

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class,
    ExperimentalAnimationSpecApi::class,
)
@Composable
fun Rn3Scaffold(
    modifier: Modifier = Modifier,
    topAppBarTitle: String,
    topAppBarTitleAlignment: Alignment.Horizontal = Start,
    onBackIconButtonClicked: (() -> Unit)?,
    topAppBarActions: List<TopAppBarAction> = emptyList(),
    topAppBarStyle: TopAppBarStyle = LARGE,
    bottomBarItems: List<BottomBarItem> = emptyList(),
    floatingActionButton: @Composable (Modifier) -> Unit = {},
    content: @Composable (Rn3PaddingValues) -> Unit,
) {
    val windowHeightClass = currentWindowAdaptiveInfo().windowSizeClass.windowHeightSizeClass
    val config = LocalConfigHelper.current

    when {
        // LARGE
        topAppBarStyle == LARGE && windowHeightClass != COMPACT -> Rn3ScaffoldImpl(
            modifier = modifier,
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            content = content,
            floatingActionButton = floatingActionButton,
            bottomBarItems = bottomBarItems,
        ) { scrollBehavior ->
            Rn3LargeTopAppBar(
                modifier = modifier,
                title = topAppBarTitle,
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
                actions = topAppBarActions,
            )
        }

        // SMALL or HOME or TRANSPARENT
        else -> Rn3ScaffoldImpl(
            modifier = modifier,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            content = content,
            floatingActionButton = floatingActionButton,
            bottomBarItems = bottomBarItems,
        ) { scrollBehavior ->
            Rn3SmallTopAppBar(
                modifier = modifier,
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
                actions = topAppBarActions,
                transparent = (topAppBarStyle == TRANSPARENT),
            ) {
                val sharedTransitionScope = LocalSharedTransitionScope.current
                    ?: throw IllegalStateException("RahNeil_N3:4F6o9kodw29Oaj8zoDlAWesB1Merqam9")
                val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (topAppBarStyle == HOME) {
                        val alternateIcon =
                            (1..1000).random() == 1 || config.getBoolean("alternateIcon_force")
                        val context = LocalContext.current

                        with(sharedTransitionScope) {
                            Surface(
                                modifier = Modifier
                                    .size(36.dp)
                                    .then(
                                        if (animatedVisibilityScope != null) {
                                            Modifier.sharedElement(
                                                rememberSharedContentState(key = "Logo_background"),
                                                animatedVisibilityScope = animatedVisibilityScope,
                                                boundsTransform = { initialBounds, targetBounds ->
                                                    keyframes {
                                                        initialBounds at 0 using ArcMode.ArcBelow using EaseInOutQuint
                                                        targetBounds at durationMillis
                                                    }
                                                },
                                            )
                                        } else {
                                            Modifier
                                        },
                                    ),
                                color = Color(
                                    ContextCompat.getColor(
                                        context,
                                        color.core_designsystem_color,
                                    ),
                                ),
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Modifier
                                    .fillMaxSize()
                                    .skipToLookaheadSize()
                                    .then(
                                        if (animatedVisibilityScope != null) {
                                            with(animatedVisibilityScope) {
                                                Modifier
                                                    .animateEnterExit(
                                                        enter = slideInVertically(
                                                            animationSpec = tween(
                                                                delayMillis = 250,
                                                                easing = EaseOutBack,
                                                            ),
                                                        ) { it } + fadeIn(
                                                            animationSpec = tween(
                                                                durationMillis = 1,
                                                                delayMillis = 250,
                                                            ),
                                                        ),
                                                    )
                                                    .sharedElement(
                                                        state = rememberSharedContentState(key = "Logo_icon"),
                                                        animatedVisibilityScope = animatedVisibilityScope,
                                                        boundsTransform = { initialBounds, targetBounds ->
                                                            keyframes {
                                                                initialBounds at 0 using ArcMode.ArcBelow using EaseInOutQuint
                                                                targetBounds at durationMillis
                                                            }
                                                        },
                                                    )
                                            }
                                        } else {
                                            Modifier
                                        },
                                    ).let { modifier ->
                                        when {
                                            alternateIcon -> Icon(
                                                imageVector = Icons.Outlined.RemoveRedEye,
                                                contentDescription = null,
                                                modifier = modifier.scale(.75f),
                                                tint = Color.White,
                                            )

                                            else -> Icon(
                                                imageVector = Icons.Outlined.Logo,
                                                contentDescription = null,
                                                modifier = modifier.scale(.75f),
                                                tint = Color.White,
                                            )
                                        }
                                    }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(if (topAppBarStyle == HOME) 0.dp else 32.dp))

                    Text(
                        text = topAppBarTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = topAppBarTitleAlignment),
                    )

                    Spacer(modifier = Modifier.width(if (topAppBarStyle == HOME) 4.dp else 0.dp))
                }
            }
        }
    }
}

@SuppressLint("DesignSystem")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Rn3ScaffoldImpl(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable (Rn3PaddingValues) -> Unit,
    floatingActionButton: @Composable (Modifier) -> Unit,
    bottomBarItems: List<BottomBarItem> = emptyList(),
    topBarComponent: @Composable (scrollBehavior: TopAppBarScrollBehavior) -> Unit,
) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            if (bottomBarItems.isNotEmpty()) {
                NavigationBar {
                    bottomBarItems.forEachIndexed { _, item ->
                        NavigationBarItem(
                            icon = { item.Icon() },
                            label = { Text(item.label) },
                            selected = item.selected,
                            alwaysShowLabel = !item.fullSize,
                            onClick = item.onClick,
                            colors = item.colors(),
                            enabled = item.enabled,
                        )
                    }
                }
            }
        },
        topBar = { topBarComponent(scrollBehavior) },
        contentWindowInsets = WindowInsets.statusBars.add(WindowInsets.displayCutout),
        floatingActionButton = { floatingActionButton(Modifier.navigationBarsPadding()) },
    ) {
        content(
            it.toRn3PaddingValues() + WindowInsets.navigationBars.toRn3PaddingValues(),
        )
    }
}

/**
 * Styles to be applied to the TopAppBars ([Large][Rn3LargeTopAppBar] or [Small][Rn3SmallTopAppBar])
 *
 * • [LARGE] represents a classic [LargeTopAppBar][Rn3LargeTopAppBar] if the screen is high enough else falls back to a classic [SmallTopAppBar][Rn3SmallTopAppBar], this is the default behavior.
 *
 * • [SMALL] represents a classic [SmallTopAppBar][Rn3SmallTopAppBar]
 *
 * • [HOME] represents a classic [SmallTopAppBar][Rn3SmallTopAppBar] with the Logo before the title.
 *
 * • [TRANSPARENT] is [SMALL] but with no background color on the appbar.
 */
enum class TopAppBarStyle {
    LARGE,
    SMALL,
    HOME,
    TRANSPARENT,
}
