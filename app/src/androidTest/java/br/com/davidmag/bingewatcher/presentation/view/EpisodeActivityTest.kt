package br.com.davidmag.bingewatcher.presentation.view

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class EpisodeActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(EpisodeActivity::class.java)
}