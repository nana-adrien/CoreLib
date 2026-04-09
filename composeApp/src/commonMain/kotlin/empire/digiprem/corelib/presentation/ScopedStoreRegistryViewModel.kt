package empire.digiprem.corelib.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore

/**
 * ScopedStoreRegistryViewModel is a ViewModel responsible for managing
 * multiple ViewModelStore instances identified by unique keys.
 *
 * It allows creating and retrieving scoped ViewModelStore objects,
 * which is useful for handling independent ViewModel lifecycles
 * within different parts of the UI (e.g., screens, tabs, flows).
 *
 * Features:
 *
 * - getOrCreate(id):
 *   Returns an existing ViewModelStore associated with the given id,
 *   or creates a new one if it does not exist.
 *
 * - clear(id):
 *   Removes the ViewModelStore associated with the given id.
 *   Note: This does not automatically clear the ViewModels inside
 *   unless explicitly handled.
 *
 * Lifecycle:
 *
 * - onCleared():
 *   Called when this ViewModel is destroyed.
 *   Clears all stored ViewModelStore instances and releases resources
 *   to avoid memory leaks.
 *
 * Use Cases:
 *
 * - Managing scoped ViewModels for dynamic navigation
 * - Isolating ViewModel lifecycles per feature/module
 * - Handling multiple independent UI states within a single screen
 *
 * Example:
 * ```
 * val store = registry.getOrCreate("screenA")
 * val viewModel = ViewModelProvider(store, factory)
 *     .get(MyViewModel::class.java)
 * ```
 */
class ScopedStoreRegistryViewModel: ViewModel() {
    private val stores=mutableMapOf<String, ViewModelStore>()

    fun getOrCreate(id: String): ViewModelStore=stores.getOrPut(id){ ViewModelStore() }
    fun clear(id: String){
        stores.remove(id)
    }
    override fun onCleared() {
        super.onCleared()
        stores.values.forEach { it.clear() }
        stores.clear()
    }
}