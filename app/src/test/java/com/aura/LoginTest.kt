package com.aura

import com.aura.app.data.model.Credentials
import com.aura.app.data.model.CredentialsResult
import com.aura.app.data.repository.BankRepository
import com.aura.app.data.state.LoginState
import com.aura.app.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.mockito.Mockito.`when` as whenever //utilisation d'un alias car when est reservé à KT

/**
 * Ce test unitaire à pour but de tester la fonction login du viewModel
 * Il est mis en place un mock de BankRepository
 *
 * En  @Before nous avons :
 * - Une initialisation des mocks avec MockitoAnnotations.openMocks
 * - Une création d'une instance de la classe LoginViewModel avec le bankRepository mocké en constructeur
 * - Un set de UnconfinedTestDispatcher en main afin de rediriger les coroutines sur celui-ci.
 *
 * En @After nous avons :
 * - Un reset du Dispatchers après chaque test afin d'éviter les effets de bord.
 */

@ExperimentalCoroutinesApi
class LoginTest {
    @Mock
    private lateinit var bankRepository: BankRepository

    private lateinit var viewModel: LoginViewModel


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this) //init des mocks
        viewModel = LoginViewModel(bankRepository) //création de l'instance de la classe LVM + bankRepo en constructeur
        Dispatchers.setMain(UnconfinedTestDispatcher()) //set unconfinedtestDispatcher en tant que main, afin de rediriger les coroutines dessus.
    }

    @After
    fun atTheEnd(){
        Dispatchers.resetMain() //a la fin fait un reset du dispatch(un clean en quelque sorte)
    }


    /**
     * Test unitaire qui vérifie le comportement du ViewModel lorsqu'il est appelé avec des identifiants
     * et des mots de passe valides, en s'assurant qu'il renvoie un état de connexion réussi avec
     * l'identifiant d'utilisateur correct.
     */
    @Test
    fun logWithValidCredMakeStateSuccess() = runTest {
        //declaration user pass valide
        val identifier = "userOk"
        val password = "passwordOk"
        //création instance de credential avec le user pass
        val credentials = Credentials(identifier, password)
        //création d'un Result accordé
        val loginResult = CredentialsResult(granted = true)

        //config du mock pour que la fonction de connexion fasse un retour du result //note when -> whenever via alias en "import"
        whenever (bankRepository.login(credentials)).thenReturn(loginResult)

        //call de la fonction login dans le viewmodel
        viewModel.login(identifier, password)

        //defini le state de la connexion
        val state = viewModel.loginState.value

        //verif si c'est un succès et si le userId correspond
        assertTrue(state is LoginState.Success && state.userId == identifier)
    }

    /**
     * Test unitaire qui vérifie le comportement du ViewModel lorsqu'il est appelé avec des identifiants
     * et des mots de passe invalide, en s'assurant qu'il renvoie un état de connexion en echec avec
     * un message d'erreur.
     */
    @Test
    fun logiWithInvalidCredMakeStateFailure() = runTest {
        val identifier = "badUser"
        val password = "badPassword"
        val credentials = Credentials(identifier,password)
        val loginResult = CredentialsResult(granted = false)

        whenever(bankRepository.login(credentials)).thenReturn(loginResult)

        viewModel.login(identifier, password)

        val state = viewModel.loginState.value


    }
}