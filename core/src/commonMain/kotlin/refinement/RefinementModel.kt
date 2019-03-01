package refinement

import kotlin.properties.Delegates


open class RefinementModel<T>(val mode: Mode = Mode.Disjunctive) {

    enum class Mode {
        ConjunctiveSingleChoice,
        ConjunctiveMultipleChoice,
        Disjunctive
    }

    var refinements by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        oldValue.forEach {
            if (!newValue.contains(it)) {
                selected.remove(it)
            }
        }
        if (oldValue != newValue) {
            refinementListeners.forEach { it(newValue) }
        }
    }
    val selected = mutableListOf<T>()

    val onSelectedRefinement = { refinement: T ->
        when (mode) {
            Mode.ConjunctiveSingleChoice -> {
                selected.clear()
                selected += refinement
            }
            Mode.ConjunctiveMultipleChoice, Mode.Disjunctive -> {
                if (!selected.remove(refinement)) {
                    selected += refinement
                }
            }
        }
        selectedListeners.forEach { it(selected) }
    }
    val refinementListeners = mutableListOf<RefinementListener<T>>()
    val selectedListeners = mutableListOf<RefinementListener<T>>()
}