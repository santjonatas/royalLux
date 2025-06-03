package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseInputDto;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PersonUpdateUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonUpdateUseCaseImpl personUpdateUseCase;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        PersonUpdateUseCaseInputDto input = new PersonUpdateUseCaseInputDto("Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir pessoa a ser atualizada, estourar exceção EntityNotFoundException com mensagem 'Pessoa é inexistente'")
    void deveLancarExcecaoQuandoPessoaASerAtualizadaNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        PersonUpdateUseCaseInputDto input = new PersonUpdateUseCaseInputDto("Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(personRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Pessoa é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir pessoa com cpf passado no input, estourar exceção ConflictException com mensagem 'CPF já vinculado a um usuário'")
    void deveLancarExcecaoQuandoCpfJaEstiverVinculadoAOutraPessoa() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        PersonUpdateUseCaseInputDto input = new PersonUpdateUseCaseInputDto("Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");
        User user = new User("mateus_2", UserRole.CLIENT, true);
        Person personToBeUpdated = new Person(user, "Mateus Souza", LocalDate.of(2003, 8, 16), "46543661054", "11985633657", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(personRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(personToBeUpdated));

        when(personRepository.existsByCpf(String.valueOf(personToBeUpdated.getId())))
                .thenReturn(true);

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("CPF já vinculado a um usuário", exception.getMessage());
    }
}