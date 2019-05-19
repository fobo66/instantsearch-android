package com.algolia.instantsearch.demo.home

import com.algolia.search.model.ObjectID
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.search.HighlightResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class HomeHit(
    override val objectID: ObjectID,
    val name: String,
    val type: String,
    val index: String,
    @SerialName("_highlightResult")
    val highlightResults: Map<String, HighlightResult>? = null
): Indexable