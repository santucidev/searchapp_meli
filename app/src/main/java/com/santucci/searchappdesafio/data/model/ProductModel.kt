package com.santucci.searchappdesafio.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val results: List<SearchResult>
)

data class SearchResult(
    val id: String,
    val title: String,
    val thumbnail: String,
    val price: Double,
    val condition: String,
    @SerializedName("currency_id") val currencyId: String,
    @SerializedName("original_price") val originalPrice: Double?,
    val shipping: ShippingInfo?,
    val installments: InstallmentInfo?,

    @SerializedName("official_store_name")
    val officialStoreName: String?,
    val seller: SellerInfo?,
    val attributes: List<Attribute>?
)

data class ShippingInfo(
    @SerializedName("free_shipping")
    val freeShipping: Boolean
)

data class InstallmentInfo(
    val quantity: Int,
    val amount: Double
)

data class SellerInfo(
    val nickname: String
)

data class ItemDetail(
    val id: String,
    val title: String,
    val price: Double,

    @SerializedName("currency_id")
    val currencyId: String,

    val pictures: List<Picture> = emptyList(),
    val attributes: List<Attribute> = emptyList(),
    var description: String = "",

    @SerializedName("path_from_root")
    var categoryPath: List<PathFromRoot> = emptyList()
)

data class Picture(
    val url: String
)

data class Attribute(
    val name: String,
    @SerializedName("value_name")
    val valueName: String?
)

data class ItemDescription(
    @SerializedName("plain_text")
    val plainText: String
)

data class CategoryInfo(
    @SerializedName("path_from_root")
    val pathFromRoot: List<PathFromRoot>
)

data class PathFromRoot(
    val name: String
)
