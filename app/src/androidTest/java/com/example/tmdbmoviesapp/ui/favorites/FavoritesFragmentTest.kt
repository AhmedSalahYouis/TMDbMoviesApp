package com.example.tmdbmoviesapp.ui.favorites

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tmdbmoviesapp.MainActivity
import com.example.tmdbmoviesapp.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoritesFragmentTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testFavoritesTitleIsDisplayed() {
        onView(withText("Favorites")).check(matches(isDisplayed()))
    }

    @Test
    fun testRecyclerViewFavoritesIsDisplayed() {
        onView(withId(R.id.recyclerViewFavorites)).check(matches(isDisplayed()))
    }
}