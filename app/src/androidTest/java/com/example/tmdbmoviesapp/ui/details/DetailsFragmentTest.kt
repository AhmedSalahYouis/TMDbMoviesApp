package com.example.tmdbmoviesapp.ui.details

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.tmdbmoviesapp.R
import org.junit.Test

class DetailsFragmentTest {

    @Test
    fun testDetailsFragmentDisplaysData() {
        val bundle = Bundle().apply {
            putInt("movieId", 550) // Example movieId
        }

        launchFragmentInContainer<DetailsFragment>(bundle, R.style.Theme_TMDBMoviesApp)

        onView(withId(R.id.textViewTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.textViewOverview)).check(matches(isDisplayed()))
    }
}