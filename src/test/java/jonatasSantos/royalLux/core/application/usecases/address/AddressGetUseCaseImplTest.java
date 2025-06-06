package jonatasSantos.royalLux.core.application.usecases.address;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.Address;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.AddressStates;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AddressGetUseCaseImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Address> criteriaQuery;

    @Mock
    private Root<Address> root;

    @Mock
    private TypedQuery<Address> typedQuery;

    @InjectMocks
    private AddressGetUseCaseImpl addressGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = AddressGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(addressGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        AddressGetUseCaseInputDto input = new AddressGetUseCaseInputDto(null, null, null, null, null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário for funcionário, deve retornar apenas o próprio endereço e de clientes")
    void naoDeveRetornarEnderecoDeOutrosFuncionariosEAdminQuandoForFuncionario() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.EMPLOYEE, true);
        userLogged.setId(1);
        Address address1 = new Address(
                userLogged,
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");
        address1.setId(1);

        AddressGetUseCaseInputDto input = new AddressGetUseCaseInputDto(null, null, null, null, null, null, null, null, null);

        User client = new User("mateus_2", UserRole.CLIENT, true);
        client.setId(2);
        Address address2 = new Address(
                client,
                "Rua das Acácias",
                "85",
                "Apartamento 302",
                "Jardim Primavera",
                "Campinas",
                AddressStates.SP,
                "13076-100");
        address2.setId(2);

        User employee = new User("marcos_3", UserRole.EMPLOYEE, true);
        employee.setId(3);
        Address address3 = new Address(
                employee,
                "Travessa do Sol",
                "12B",
                "Casa",
                "Centro",
                "Salvador",
                AddressStates.BA,
                "40020-030");
        address3.setId(3);

        User admin = new User("paulo_4", UserRole.ADMIN, true);
        admin.setId(4);
        Address address4 = new Address(
                admin,
                "Alameda das Palmeiras",
                "500",
                "Bloco C, Sala 45",
                "Parque Industrial",
                "Curitiba",
                AddressStates.PR,
                "81200-200");
        address4.setId(4);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        var usersFromDb = List.of(address1, address2, address3, address4);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Address.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Address.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(usersFromDb);

        // Act
        var result = addressGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
        );

        // Assert
        assertEquals(2, result.size());

        assertTrue(result.stream().anyMatch(address -> address.id().equals(1))); // O próprio Employee
        assertTrue(result.stream().anyMatch(address -> address.id().equals(2))); // Client

        assertFalse(result.stream().anyMatch(address -> address.id().equals(3))); // Admin não deve aparecer
        assertFalse(result.stream().anyMatch(address -> address.id().equals(4))); // Outro Employee não deve aparecer
    }

    @Test
    @DisplayName("Quando usuário for cliente, deve retornar apenas o próprio endereço")
    void naoDeveRetornarEnderecoDeOutrosUsuariosQuandoForCliente() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);
        Address address1 = new Address(
                userLogged,
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");
        address1.setId(1);

        AddressGetUseCaseInputDto input = new AddressGetUseCaseInputDto(null, null, null, null, null, null, null, null, null);

        User admin = new User("paulo_2", UserRole.ADMIN, true);
        admin.setId(2);
        Address address2 = new Address(
                admin,
                "Alameda das Palmeiras",
                "500",
                "Bloco C, Sala 45",
                "Parque Industrial",
                "Curitiba",
                AddressStates.PR,
                "81200-200");
        address2.setId(2);

        User employee = new User("marcos_3", UserRole.EMPLOYEE, true);
        employee.setId(3);
        Address address3 = new Address(
                employee,
                "Travessa do Sol",
                "12B",
                "Casa",
                "Centro",
                "Salvador",
                AddressStates.BA,
                "40020-030");
        address3.setId(3);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        var usersFromDb = List.of(address1, address2, address3);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Address.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Address.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(usersFromDb);

        // Act
        var result = addressGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(1, result.size());

        assertTrue(result.stream().anyMatch(address -> address.id().equals(1))); // O próprio Client

        assertFalse(result.stream().anyMatch(address -> address.id().equals(2))); // Admin não deve aparecer
        assertFalse(result.stream().anyMatch(address -> address.id().equals(3))); // Employee não deve aparecer
    }

    @Test
    @DisplayName("Quando usuário for ADMIN, deve retornar todos os endereços com sucesso")
    void deveRetornarTodosEnderecosQuandoForAdmin() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);
        Address address1 = new Address(
                userLogged,
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");
        address1.setId(1);

        AddressGetUseCaseInputDto input = new AddressGetUseCaseInputDto(null, null, null, null, null, null, null, null, null);

        User client = new User("paulo_2", UserRole.CLIENT, true);
        client.setId(2);
        Address address2 = new Address(
                client,
                "Alameda das Palmeiras",
                "500",
                "Bloco C, Sala 45",
                "Parque Industrial",
                "Curitiba",
                AddressStates.PR,
                "81200-200");
        address2.setId(2);

        User employee = new User("marcos_3", UserRole.EMPLOYEE, true);
        employee.setId(3);
        Address address3 = new Address(
                employee,
                "Travessa do Sol",
                "12B",
                "Casa",
                "Centro",
                "Salvador",
                AddressStates.BA,
                "40020-030");
        address3.setId(3);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        var usersFromDb = List.of(address1, address2, address3);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Address.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Address.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(usersFromDb);

        // Act
        var result = addressGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        // Assert
        assertEquals(3, result.size());

        assertTrue(result.stream().anyMatch(address -> address.id().equals(1))); // O próprio Admin
        assertTrue(result.stream().anyMatch(address -> address.id().equals(2))); // Client
        assertTrue(result.stream().anyMatch(address -> address.id().equals(3))); // Employee
    }
}