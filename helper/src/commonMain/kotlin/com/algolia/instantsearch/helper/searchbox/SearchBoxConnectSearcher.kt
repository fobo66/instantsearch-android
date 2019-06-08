package com.algolia.instantsearch.helper.searchbox

import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searcher.defaultDispatcher


public fun SearchBoxViewModel.connectSearcher(
    searcher: Searcher,
    searchAsYouType: Boolean = true,
    debouncer: Debouncer = Debouncer(100)
) {
    if (searchAsYouType) {
        onItemChanged += {
            searcher.setQuery(it)
            debouncer.debounce(searcher.coroutineScope, defaultDispatcher) { searcher.search() }
        }
    } else {
        onQuerySubmitted += {
            searcher.setQuery(it)
            searcher.searchAsync()
        }
    }
}