package com.aura

import com.aura.app.data.model.*
import com.aura.app.data.network.ServiceInterface
import com.aura.app.data.repository.BankRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


/**
 * Tests unitaires pour BankRepository.
 *
 * Cette classe de test vise à valider le comportement de BankRepository en simulant ses dépendances exterieur,
 * en particulier ServiceInterface, pour s'assurer qu'il interagit correctement avec ces services sans effectuer
 * de véritables appels réseau.
 *
 * Les principales fonctionnalités testées incluent :
 * - Connexion des utilisateurs avec vérification des identifiants. (fun testLogin)
 * - Récupération des comptes par ID utilisateur. (fun testGetAccountByUserId)
 * - Exécution de transactions financières. (fun testTransfer)
 *
 * les tests utilisent un mock de ServiceInterface pour contrôler les réponses attendues et vérifier
 * que BankRepository fait les bons appels à cette interface.
 *
 * Chaque test est contrruit de la façon suivante :
 * - Mise en place des Data pour le test
 * - Configuration du comportement attendu par le mock
 * - Execution de la fonction correspondante
 * - Verification que la méthode a bien été appelé sur le mock de serviceInterface
 * - Assertion pour vérifier le bon retour.
 */

@ExperimentalCoroutinesApi
class BankRepositoryTest {

    //mock pour simuler le ServiceInterface qui interagit avec le réseau
    @Mock
    private lateinit var serviceInterface: ServiceInterface

    //instance du BankRepository qui sera testée
    private lateinit var bankRepository: BankRepository

    //config initiale avant chaque test
    @Before
    fun setUp() {
        //init des mocks créés avec les annotations @Mock
        MockitoAnnotations.initMocks(this)
        //creation de li'nstance de BankRepository avec le serviceInterface mocké
        bankRepository = BankRepository(serviceInterface)
    }

    //test la fonction de login
    @Test
    fun testLogin() = runBlockingTest {
        //data pour le test
        val credentials = Credentials("username", "password")
        val expectedResult = CredentialsResult(true)
        //config du comportement attendu du mock
        `when`(serviceInterface.login(credentials)).thenReturn(expectedResult)

        //exec de la fonction de login
        val result = bankRepository.login(credentials)

        //vérif que la méthode login a été appelée sur le mock de serviceInterface.
        verify(serviceInterface).login(credentials)
        //assert pour vérifier que le return est le bon
        assert(result == expectedResult)
    }

    //test la récup des comptes d'un utilisateur
    @Test
    fun testGetAccountsByUserId() = runBlockingTest {
        val userId = "1234"
        val expectedAccounts = listOf(Account("1", true, 250.0))

        `when`(serviceInterface.getAccountsByUserId(userId)).thenReturn(expectedAccounts)

        val result = bankRepository.getAccountsByUserId(userId)

        verify(serviceInterface).getAccountsByUserId(userId)
        assert(result == expectedAccounts)
    }

    //test la fonction de transfert pour s'assurer qu'elle envoie les bonnes informations de transfert et gère correctement le résultat
    @Test
    fun testTransfer() = runBlockingTest {
        val transfer = Transfer("senderId", "recipientId", 223.0)
        val expectedResult = TransferResult(true)

        `when`(serviceInterface.transfer(transfer)).thenReturn(expectedResult)

        val result = bankRepository.transfer(transfer)
        verify(serviceInterface).transfer(transfer)
        assert(result == expectedResult)
    }
}
