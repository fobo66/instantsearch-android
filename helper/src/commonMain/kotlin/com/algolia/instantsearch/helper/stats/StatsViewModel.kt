package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.search.model.response.ResponseSearch


public open class StatsViewModel(response: ResponseSearch? = null) {

    val response = SubscriptionValue(response)
}