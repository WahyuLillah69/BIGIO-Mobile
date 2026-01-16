package com.takehomechallenge.lillah.ui.search

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
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var api: RickMortyApi
    private lateinit var dao: FavoriteDao
    private lateinit var repository: CharacterRepository
    private lateinit var viewModel: SearchViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        api = mockk()
        dao = mockk()
        repository = CharacterRepository(api, dao)
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Success state
    @Test
    fun `search success should emit Success state`() = runTest {
        val fakeCharacters = listOf(
            Character(
                id = 1,
                name = "Morty Smith",
                image = "url",
                species = "Human",
                gender = "Male",
                origin = mockk<Origin>(),
                location = mockk<Location>()
            )
        )

        coEvery { api.searchCharacter("Morty") } returns mockk {
            coEvery { results } returns fakeCharacters
        }

        viewModel.search("Morty")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is SearchState.Success)
        assertEquals(1, (state as SearchState.Success).data.size)
        assertEquals("Morty Smith", state.data[0].name)
    }

    // Empty state
    @Test
    fun `search empty result should emit Empty state`() = runTest {
        coEvery { api.searchCharacter("Unknown") } returns mockk {
            coEvery { results } returns emptyList()
        }

        viewModel.search("Unknown")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is SearchState.Empty)
    }

    // Error state
    @Test
    fun `search error should emit Error state`() = runTest {
        coEvery { api.searchCharacter("Error") } throws Exception("Character not found")

        viewModel.search("Error")
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is SearchState.Error)
        assertEquals("Character not found", (state as SearchState.Error).message)
    }
}