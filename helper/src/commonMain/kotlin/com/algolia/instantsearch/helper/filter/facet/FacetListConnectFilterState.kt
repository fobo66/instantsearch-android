package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.state.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


public fun FacetListViewModel.connectFilterState(
    attribute: Attribute,
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID(attribute, FilterOperator.Or)
) {
    filterState.filters.subscribePast { filters ->
        selections.value = filters.getFacetFilters(groupID)
            .map { it.getValue() }
            .toSet()
    }
    eventSelection.subscribe { computed ->
        val filters = computed.map { Filter.Facet(attribute, it) }.toSet()

        filterState.notify {
            when (selectionMode) {
                SelectionMode.Single -> clear(groupID)
                SelectionMode.Multiple -> {
                    val currentFilters = items.value.map { Filter.Facet(attribute, it.value) }.toSet()
                    val currentSelections = selections.value.map { Filter.Facet(attribute, it) }
                    val facetsToRemove = if (persistentSelection) currentFilters + currentSelections else currentFilters

                    remove(groupID, facetsToRemove)
                }
            }
            add(groupID, filters)
        }
    }
}