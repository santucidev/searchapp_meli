package com.santucci.searchappdesafio.repository

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream

class ProductRepositoryTest {

    private lateinit var mockContext: Context
    private lateinit var mockAssetManager: AssetManager
    private lateinit var gson: Gson

    private lateinit var productRepository: ProductRepository

    @Before
    fun setUp() {
        mockContext = mockk()
        mockAssetManager = mockk()
        gson = Gson()

        every { mockContext.assets } returns mockAssetManager

        productRepository = ProductRepository(mockContext, gson)
    }

    @Test
    fun `searchProducts MUST return SearchResponse WHEN searching for valid`() {
        val query = "iphone"
        val fakeJson = """
            {
              "results": [
                { "id": "MLA123", "title": "iPhone Test", "price": 1000.0, "thumbnail": "", "currency_id": "ARS", "condition": "new" }
              ]
            }
        """.trimIndent()

        // GIVEN
        every { mockAssetManager.open("search-MLA-iphone.json") } returns ByteArrayInputStream(
            fakeJson.toByteArray()
        )

        // ACT
        val response = productRepository.searchProducts(query)

        // ASSERT
        assertNotNull(response)
        assertEquals(1, response.results.size)
        assertEquals("iPhone Test", response.results[0].title)
    }
}
