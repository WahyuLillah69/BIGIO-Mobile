package com.takehomechallenge.lillah.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.takehomechallenge.lillah.data.model.Character
import com.takehomechallenge.lillah.data.model.Location
import com.takehomechallenge.lillah.data.model.Origin
import com.takehomechallenge.lillah.data.remote.RickMortyApi
import com.takehomechallenge.lillah.data.local.FavoriteDao
import com.takehomechallenge.lillah.data.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var api: RickMortyApi
    private lateinit var dao: FavoriteDao
    private lateinit var repository: CharacterRepository
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        api = mockk()
        dao = mockk()
        repository = CharacterRepository(api, dao)
        viewModel = HomeViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Success state
    @Test
    fun `fetchCharacters success should emit Success state`() = runTest {
        val fakeCharacters = listOf(
            Character(
                id = 1,
                name = "Rick Sanchez",
                image = "url",
                species = "Human",
                gender = "Male",
                origin = mockk<Origin>(),
                location = mockk<Location>()
            )
        )

        coEvery { api.getCharacters() } returns mockk {
            coEvery { results } returns fakeCharacters
        }

        viewModel.fetchCharacters()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is HomeState.Success)
        assertEquals(1, (state as HomeState.Success).data.size)
    }

    // Empty state
    @Test
    fun `fetchCharacters empty should emit Empty state`() = runTest {
        coEvery { api.getCharacters() } returns mockk {
            coEvery { results } returns emptyList()
        }

        viewModel.fetchCharacters()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is HomeState.Empty)
    }

    // Error state
    @Test
    fun `fetchCharacters error should emit Error state`() = runTest {
        coEvery { api.getCharacters() } throws Exception("Network error")

        viewModel.fetchCharacters()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is HomeState.Error)
        assertEquals("Network error", (state as HomeState.Error).message)
    }
}