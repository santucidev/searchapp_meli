package com.santucci.searchappdesafio.repository

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.santucci.searchappdesafio.data.model.CategoryInfo
import com.santucci.searchappdesafio.data.model.ItemDescription
import com.santucci.searchappdesafio.data.model.ItemDetail
import com.santucci.searchappdesafio.data.model.SearchResponse
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val context: Context,
    private val gson: Gson
) {

    fun isValidQuery(query: String): Boolean {
        return when (query.lowercase()) {
            "zapatillas", "iphone", "camisa", "cafe", "arroz" -> true
            else -> false
        }
    }

    fun searchProducts(query: String): SearchResponse {
        val fileName = when (query.lowercase()) {
            "zapatillas" -> "search-MLA-zapatillas.json"
            "iphone" -> "search-MLA-iphone.json"
            "camisa" -> "search-MLA-camisa.json"
            "cafe" -> "search-MLA-cafe.json"
            "arroz" -> "search-MLA-arroz.json"
            else -> return SearchResponse(emptyList())
        }

        return try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            gson.fromJson(jsonString, SearchResponse::class.java)
        } catch (e: Exception) {
            SearchResponse(emptyList())
        }
    }

    // LOGS adicionado para conforme necessidade para -> Gestão de casos de erros do ponto de vista do desenvolvedor.

    fun getProductDetails(itemId: String): ItemDetail? {
        Log.d(
            "DEBUG", "Tentando carregar detalhes para o item ID: $itemId"
        )
        return try {
            val itemJson =
                context.assets.open("item-$itemId.json").bufferedReader().use { it.readText() }
            val descriptionJson =
                context.assets.open("item-$itemId-description.json").bufferedReader()
                    .use { it.readText() }
            val categoryJson = context.assets.open("item-$itemId-category.json").bufferedReader()
                .use { it.readText() }

            val itemDetail = gson.fromJson(itemJson, ItemDetail::class.java)
            Log.d("DEBUG", "Detalhes carregados com sucesso para $itemId")
            val description = gson.fromJson(descriptionJson, ItemDescription::class.java)
            val categoryInfo = gson.fromJson(categoryJson, CategoryInfo::class.java)

            itemDetail.apply {
                this.description = description.plainText
                this.categoryPath = categoryInfo.pathFromRoot
            }
        } catch (e: Exception) {
            Log.e(
                "DEBUG", "FALHA ao carregar detalhes para o item $itemId: ${e.message}"
            )
            null
        }
    }
}