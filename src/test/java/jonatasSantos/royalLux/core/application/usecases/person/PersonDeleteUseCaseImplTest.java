package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonDeleteUseCaseOutputDto;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonDeleteUseCaseImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonDeleteUseCaseImpl personDeleteUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir pessoa a ser deletada com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Pessoa inexistente'")
    void deveLancarExcecaoQuandoNaoExistirPessoaASerDeletada() {
        // Arrange
        when(personRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personDeleteUseCase.execute(3);
        });

        assertEquals("Pessoa inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando pessoa a ser deletada for ADMIN, estourar exceção UnauthorizedException com mensagem 'Admin não pode ser deletado'")
    void deveLancarExcecaoQuandoPessoaASerDeletadaForAdmin() {
        // Arrange
        User user = new User("joao_1", UserRole.ADMIN, true);
        Person person = new Person(user, "João Gomes", LocalDate.of(2003, 8, 16), "46543661054", "11985633657", "joao.gomes@gmail.com");

        when(personRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(person));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            personDeleteUseCase.execute(3);
        });

        assertEquals("Admin não pode ser deletado", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar pessoa com sucesso e retornar true em propriedade success do output")
    void deveDeletarPessoaComSucesso() {
        // Arrange
        User user = new User("mateus_2", UserRole.CLIENT, true);
        Person person = new Person(user, "Mateus Souza", LocalDate.of(2003, 8, 16), "46543661054", "11985633657", "mateus.souza@gmail.com");

        when(personRepository.findById(String.valueOf(3)))
                .thenReturn(Optional.of(person));

        // Act
        PersonDeleteUseCaseOutputDto output = personDeleteUseCase.execute(3);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(personRepository).delete(any(Person.class));
        verify(personRepository, times(1)).delete(person);
    }
}