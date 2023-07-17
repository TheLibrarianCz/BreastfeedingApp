package com.example.kojeniapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.kojeniapp.database.Repository
import com.example.kojeniapp.database.models.AdditionalConstants
import com.example.kojeniapp.database.models.Feeding
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class NewFeedingViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun createFeeding(
        left: Boolean,
        right: Boolean,
        probiotics: Boolean,
        vigantol: Boolean,
        espumisan: Boolean
    ) {
        repository.insertNewFeeding(
            Feeding(
                date = LocalDate.now(),
                timestamp = LocalTime.now(),
                breast = sumUpBreasts(left = left, right = right),
                additions = sumUpAdditions(probiotics, vigantol, espumisan)
            )
        )
    }

    private fun sumUpBreasts(
        left: Boolean,
        right: Boolean
    ): Int = listOf(
        if (left) AdditionalConstants.PROBIOTICS else 0,
        if (right) AdditionalConstants.VIGANTOL else 0
    ).sum()

    private fun sumUpAdditions(
        probiotics: Boolean,
        vigantol: Boolean,
        espumisan: Boolean
    ): Int = listOf(
        if (probiotics) AdditionalConstants.PROBIOTICS else 0,
        if (vigantol) AdditionalConstants.VIGANTOL else 0,
        if (espumisan) AdditionalConstants.ESPUMISAN else 0
    ).sum()
}
