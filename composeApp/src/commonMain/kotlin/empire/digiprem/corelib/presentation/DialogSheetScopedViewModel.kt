package empire.digiprem.corelib.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid


/**
 * DialogSheetScopedViewModel est un composable permettant de créer
 * un scope de ViewModel indépendant pour un composant affiché
 * conditionnellement (comme un Dialog ou un BottomSheet).
 *
 * Il associe un ViewModelStore spécifique à une instance visible
 * et garantit que les ViewModels sont correctement créés et détruits
 * en fonction de la visibilité.
 *
 * Paramètres :
 *
 * - visible :
 *   Indique si le contenu (dialog/sheet) est affiché.
 *
 * - scopeId :
 *   Identifiant unique du scope. Par défaut, un UUID est généré
 *   et sauvegardé via rememberSaveable pour survivre aux recompositions.
 *
 * - content :
 *   Contenu composable qui utilisera le scope de ViewModel dédié.
 *
 * Fonctionnement :
 *
 * - Récupère le ViewModelStoreOwner parent via LocalViewModelStoreOwner
 * - Utilise ScopedStoreRegistryViewModel pour gérer les ViewModelStore
 * - Crée dynamiquement un ViewModelStoreOwner lorsque visible = true
 * - Supprime et nettoie le store lorsque visible = false
 * - Fournit le nouveau scope via CompositionLocalProvider
 *
 * Cycle de vie :
 *
 * - À l’ouverture (visible = true) :
 *   → Création d’un ViewModelStore dédié
 *
 * - À la fermeture (visible = false) :
 *   → Suppression du store associé
 *   → Nettoyage des ViewModels liés
 *
 * Cas d’utilisation :
 *
 * - Dialog avec ViewModel propre
 * - BottomSheet avec état isolé
 * - Flows UI temporaires avec cycle de vie indépendant
 *
 * Exemple :
 * ```
 * DialogSheetScopedViewModel(visible = showDialog) {
 *     val viewModel = viewModel<MyViewModel>()
 *     MyDialogContent(viewModel)
 * }
 * ```
 *
 * ⚠️ Attention :
 * - Nécessite un ViewModelStoreOwner parent valide
 * - Chaque scopeId doit être unique pour éviter les conflits
 */
@OptIn(ExperimentalUuidApi::class)
@Composable
fun  DialogSheetScopedViewModel(
    visible: Boolean,
    scopeId:String= rememberSaveable{
        Uuid.random().toString()},
    content:@Composable ()-> Unit
) {

    val parentStore= LocalViewModelStoreOwner.current
        ?:throw IllegalStateException("No parent owner found")

    val registy = viewModel<ScopedStoreRegistryViewModel>(viewModelStoreOwner = parentStore)
    var owner by remember{ mutableStateOf<ViewModelStoreOwner?>(null) }


    LaunchedEffect(visible ,scopeId){
        if (visible && owner==null){
            owner=object : ViewModelStoreOwner{
                override val viewModelStore: ViewModelStore
                    get() = registy.getOrCreate(scopeId)

            }
        }else if (!visible && owner!=null){
            registy.clear(id =  scopeId)
            owner=null
        }
    }

    owner?.let { dialogOwner->
        CompositionLocalProvider(LocalViewModelStoreOwner provides dialogOwner){
            content()
        }
    }
}