package com.algolia.instantsearch.demo.directory

import com.algolia.instantsearch.demo.selectable.facet.SelectableFacetsDemo
import com.algolia.instantsearch.demo.selectable.filter.SelectableFiltersDemo
import com.algolia.instantsearch.demo.selectable.numeric.SelectableNumericsDemo
import kotlin.reflect.KClass


enum class DirectoryType(val clazz: KClass<*>) {
    SelectableFacets(SelectableFacetsDemo::class),
    SelectableFilter(SelectableFiltersDemo::class),
    SelectableNumerics(SelectableNumericsDemo::class)
}