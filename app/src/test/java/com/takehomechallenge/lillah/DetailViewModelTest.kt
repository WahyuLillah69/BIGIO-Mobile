package com.takehomechallenge.lillah.ui.detail

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.takehomechallenge.lillah.data.local.DatabaseProvider
import com.takehomechallenge.lillah.data.local.FavoriteDao
import com.takehomechallenge.lillah.data.model.Character
import com.takehomechallenge.lillah.data.model.Location
import com.takehomechallenge.lillah.data.model.Origin
import com.takehomechallenge.lillah.data.remote.ApiClient
import com.takehomechallenge.lillah.data.remote.RickMortyApi
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var application: Application
    private lateinit var api: RickMortyApi
    private lateinit var dao: FavoriteDao
    private lateinit var viewModel: DetailViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        application = mockk(relaxed = true)
        api = mockk()
        dao = mockk()

        mockkObject(DatabaseProvider)
        val fakeDb = mockk<com.takehomechallenge.lillah.data.local.AppDatabase>()
        every { fakeDb.favoriteDao() } returns dao
        every { DatabaseProvider.getDatabase(application) } returns fakeDb

        mockkObject(ApiClient)
        every { ApiClient.api } returns api

        viewModel = DetailViewModel(application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    // Fetch detail success
    @Test
    fun `fetchDetail success should emit Success state and update isFavorite`() = runTest {
        val fakeCharacter = Character(
            id = 1,
            name = "Rick Sanchez",
            image = "url",
            species = "Human",
            gender = "Male",
            origin = mockk<Origin>(),
            location = mockk<Location>()
        )

        coEvery { api.getCharacterDetail(1) } returns fakeCharacter
        coEvery { dao.isFavorite(1) } returns true

        viewModel.fetchDetail(1)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is DetailState.Success)
        assertEquals("Rick Sanchez", (state as DetailState.Success).character.name)
        assertTrue(viewModel.isFavorite.value)
    }

    // Fetch detail error
    @Test
    fun `fetchDetail error should emit Error state`() = runTest {
        coEvery { api.getCharacterDetail(1) } throws Exception("Network error")

        viewModel.fetchDetail(1)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is DetailState.Error)
        assertEquals("Failed to load detail", (state as DetailState.Error).message)
    }

    // Toggle favorite add
    @Test
    fun `toggleFavorite should add favorite when not favorite`() = runTest {
        val fakeCharacter = Character(
            id = 2,
            name = "Morty Smith",
            image = "url",
            species = "Human",
            gender = "Male",
            origin = mockk<Origin>(),
            location = mockk<Location>()
        )

        coEvery { dao.insertFavorite(any()) } just Runs

        viewModel.toggleFavorite(fakeCharacter)
        advanceUntilIdle()

        assertTrue(viewModel.isFavorite.value)
        coVerify { dao.insertFavorite(any()) }
    }

    // Toggle favorite remove
    @Test
    fun `toggleFavorite should remove favorite when already favorite`() = runTest {
        val fakeCharacter = Character(
            id = 3,
            name = "Summer Smith",
            image = "url",
            species = "Human",
            gender = "Female",
            origin = mockk<Origin>(),
            location = mockk<Location>()
        )

        coEvery { dao.insertFavorite(any()) } just Runs
        coEvery { dao.deleteFavorite(any()) } just Runs

        // Add first
        viewModel.toggleFavorite(fakeCharacter)
        advanceUntilIdle()
        assertTrue(viewModel.isFavorite.value)

        // Remove
        viewModel.toggleFavorite(fakeCharacter)
        advanceUntilIdle()
        assertFalse(viewModel.isFavorite.value)

        coVerify { dao.deleteFavorite(any()) }
    }
}