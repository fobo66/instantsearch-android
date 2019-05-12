package com.algolia.instantsearch.demo.selectable.facet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.algolia.instantsearch.demo.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.FilterState
import kotlinx.android.synthetic.main.selectable_facets_demo.*
import kotlinx.android.synthetic.main.selectable_header.*
import searcher.SearcherSingleIndex
import selectable.facet.*
import selectable.facet.FacetSortCriterion.*
import selectable.list.SelectionMode


class SelectableFacetsDemo : AppCompatActivity() {

    private val color = Attribute("color")
    private val promotions = Attribute("promotions")
    private val category = Attribute("category")
    private val colors
        get() = mapOf(
            color.raw to ContextCompat.getColor(this, android.R.color.holo_red_dark),
            promotions.raw to ContextCompat.getColor(this, android.R.color.holo_blue_dark),
            category.raw to ContextCompat.getColor(this, android.R.color.holo_green_dark)
        )

    private val groupIDColor = FilterGroupID.And(color)
    private val groupIDPromotions = FilterGroupID.And(promotions)
    private val groupIDCategory = FilterGroupID.Or(category)

    private val index = client.initIndex(IndexName("mobile_demo_selectable_facets"))
    private val filterState = FilterState(
        facetGroups = mutableMapOf(
            FilterGroupID.And(color.raw) to setOf(Filter.Facet(color, "green"))
        )
    )
    private val searcher = SearcherSingleIndex(index, filterState = filterState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selectable_facets_demo)

        val colorAViewModel = SelectableFacetsViewModel()
        val colorAPresenter = SelectableFacetsPresenter(listOf(IsRefined, AlphabeticalAscending), 5)
        val colorAAdapter = SelectableFacetsAdapter()

        colorAViewModel.connectSearcher(color, searcher, groupIDColor)
        colorAViewModel.connectView(colorAAdapter, colorAPresenter)

        val colorBViewModel = SelectableFacetsViewModel(selectionMode = SelectionMode.Single)
        val colorBPresenter = SelectableFacetsPresenter(listOf(AlphabeticalDescending), 3)
        val colorBAdapter = SelectableFacetsAdapter()

        colorBViewModel.connectSearcher(color, searcher, groupIDColor)
        colorBViewModel.connectView(colorBAdapter, colorBPresenter)

        val promotionViewModel = SelectableFacetsViewModel(selectionMode = SelectionMode.Multiple)
        val promotionPresenter = SelectableFacetsPresenter(listOf(CountDescending), 5)
        val promotionAdapter = SelectableFacetsAdapter()

        promotionViewModel.connectSearcher(promotions, searcher, groupIDPromotions)
        promotionViewModel.connectView(promotionAdapter, promotionPresenter)

        val categoryViewModel = SelectableFacetsViewModel(selectionMode = SelectionMode.Multiple)
        val categoryPresenter = SelectableFacetsPresenter(listOf(CountDescending, AlphabeticalAscending), 5)
        val categoryAdapter = SelectableFacetsAdapter()

        categoryViewModel.connectSearcher(category, searcher, groupIDCategory)
        categoryViewModel.connectView(categoryAdapter, categoryPresenter)

        configureRecyclerView(listTopLeft, colorAAdapter)
        configureRecyclerView(listTopRight, colorBAdapter)
        configureRecyclerView(listBottomLeft, promotionAdapter)
        configureRecyclerView(listBottomRight, categoryAdapter)

        titleTopLeft.text = formatTitle(colorAPresenter, groupIDColor)
        titleTopRight.text = formatTitle(colorBPresenter, groupIDColor)
        titleBottomLeft.text = formatTitle(promotionPresenter, groupIDPromotions)
        titleBottomRight.text = formatTitle(categoryPresenter, groupIDCategory)

        onChangeThenUpdateFiltersText(searcher, colors, filtersTextView)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onClearAllThenClearFilters(searcher, filtersClearAll)
        onResponseChangedThenUpdateNbHits(searcher)

        searcher.search()
    }

    override fun onDestroy() {
        super.onDestroy()
        searcher.cancel()
    }

    private fun FacetSortCriterion.format(): String {
        return when (this) {
            IsRefined -> name
            CountAscending -> "CountAsc"
            CountDescending -> "CountDesc"
            AlphabeticalAscending -> "AlphaAsc"
            AlphabeticalDescending -> "AlphaDesc"
        }
    }

    private fun formatTitle(presenter: SelectableFacetsPresenter, filterGroupID: FilterGroupID): String {

        val criteria = presenter.sortBy.joinToString("-") { it.format() }
        val operator = when (filterGroupID) {
            is FilterGroupID.And -> "And"
            is FilterGroupID.Or -> "Or"
        }

        return "$operator, $criteria, l=${presenter.limit}"
    }
}