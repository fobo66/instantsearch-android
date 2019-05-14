package filter.toggle

import com.algolia.instantsearch.core.selectable.SelectableView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.toggle.FilterToggleViewModel
import com.algolia.instantsearch.helper.filter.toggle.connectView
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import shouldEqual
import shouldNotBeNull
import kotlin.test.Test


class TestFilterToggleConnectView {

    private val color = Attribute("color")
    private val red = Filter.Facet(color, "red")

    private class MockSelectableView : SelectableView {

        var boolean: Boolean? = null
        var string: String? = null

        override var onClick: ((Boolean) -> Unit)? = null

        override fun setIsSelected(isSelected: Boolean) {
            boolean = isSelected
        }

        override fun setText(text: String) {
            string = text
        }
    }

    @Test
    fun connectShouldCallSetIsSelectedAndSetText() {
        val view = MockSelectableView()
        val viewModel = FilterToggleViewModel(red)

        viewModel.isSelected = true
        viewModel.connectView(view)
        view.boolean shouldEqual true
        view.string shouldEqual FilterPresenter(red)
    }

    @Test
    fun onClickShouldCallOnIsSelectedComputed() {
        val view = MockSelectableView()
        val viewModel = FilterToggleViewModel(red)

        viewModel.onIsSelectedComputed += { viewModel.isSelected = it }
        viewModel.connectView(view)
        view.onClick.shouldNotBeNull()
        view.onClick!!(true)
        view.boolean shouldEqual true
    }

    @Test
    fun onIsSelectedChangedShouldCallSetIsSelected() {
        val view = MockSelectableView()
        val viewModel = FilterToggleViewModel(red)

        viewModel.connectView(view)
        viewModel.isSelected = true
        view.boolean shouldEqual true
    }
}