package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonGetUseCaseInputDto;
import jonatasSantos.royalLux.core.domain.entities.Person;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PersonGetUseCaseImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Person> criteriaQuery;

    @Mock
    private Root<Person> root;

    @Mock
    private TypedQuery<Person> typedQuery;

    @InjectMocks
    private PersonGetUseCaseImpl personGetUseCase;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        var entityManagerField = PersonGetUseCaseImpl.class.getDeclaredField("entityManager");
        entityManagerField.setAccessible(true);
        entityManagerField.set(personGetUseCase, entityManager);
    }

    @Test
    @DisplayName("Quando não existir usuário com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);

        PersonGetUseCaseInputDto input = new PersonGetUseCaseInputDto(null, null, null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personGetUseCase.execute(
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
    @DisplayName("Quando usuário for CLIENT, deve retornar pessoas e excluir outros CLIENTS")
    void naoDeveRetornarOutrasPessoasClientQuandoForClient() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.CLIENT, true);
        userLogged.setId(1);
        Person person4 = new Person(userLogged, "João Gomes", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "joao.gomes@gmail.com");
        person4.setId(1);

        PersonGetUseCaseInputDto input = new PersonGetUseCaseInputDto(null, null, null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User client2 = new User("maria_client", UserRole.CLIENT, true);
        client2.setId(2);
        Person person1 = new Person(client2, "Maria", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "maria.pereira@gmail.com");
        person1.setId(2);

        User employee = new User("carlos_employee", UserRole.EMPLOYEE, true);
        employee.setId(3);
        Person person2 = new Person(employee, "Carlos", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "carlos.santos@gmail.com");
        person2.setId(3);

        User admin = new User("ana_admin", UserRole.ADMIN, true);
        admin.setId(4);
        Person person3 = new Person(admin, "Ana Castela", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "ana.castela@gmail.com");
        person3.setId(4);

        var personsFromDb = List.of(person1, person2, person3, person4);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Person.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Person.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(personsFromDb);

        // Act
        var result = personGetUseCase.execute(
                    userLogged,
                    input,
                    0,
                    10,
                    true
            );

        //  Assert
        assertEquals(3, result.size());

        assertTrue(result.stream().anyMatch(person -> person.id().equals(1))); // O próprio Client
        assertTrue(result.stream().anyMatch(person -> person.id().equals(3))); // Employee
        assertTrue(result.stream().anyMatch(person -> person.id().equals(4))); // Admin

        assertFalse(result.stream().anyMatch(person -> person.id().equals(2))); // Outro Client não deve aparecer
    }

    @Test
    @DisplayName("Quando usuário for ADMIN, deve retornar todas as pessoas com sucesso")
    void deveRetornarTodasPessoasQuandoForAdmin() {
        // Arrange
        User userLogged = new User("ana_admin", UserRole.ADMIN, true);
        userLogged.setId(1);
        Person person3 = new Person(userLogged, "Ana Castela", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "ana.castela@gmail.com");
        person3.setId(1);

        PersonGetUseCaseInputDto input = new PersonGetUseCaseInputDto(null, null, null, null, null, null, null);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        User client2 = new User("maria_client", UserRole.CLIENT, true);
        client2.setId(2);
        Person person1 = new Person(client2, "Maria", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "maria.pereira@gmail.com");
        person1.setId(2);

        User employee = new User("carlos_employee", UserRole.EMPLOYEE, true);
        employee.setId(3);
        Person person2 = new Person(employee, "Carlos", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "carlos.santos@gmail.com");
        person2.setId(3);

        var personsFromDb = List.of(person1, person2, person3);

        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Person.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Person.class)).thenReturn(root);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);

        when(typedQuery.getResultList()).thenReturn(personsFromDb);

        // Act
        var result = personGetUseCase.execute(
                userLogged,
                input,
                0,
                10,
                true
        );

        //  Assert
        assertEquals(3, result.size());

        assertTrue(result.stream().anyMatch(user -> user.id().equals(1))); // Admin logado
        assertTrue(result.stream().anyMatch(user -> user.id().equals(2))); // Client
        assertTrue(result.stream().anyMatch(user -> user.id().equals(3))); // Employee
    }
}