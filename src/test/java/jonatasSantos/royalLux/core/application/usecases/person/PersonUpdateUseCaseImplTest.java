package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseOutputDto;
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
    @DisplayName("Quando não existir pessoa a ser atualizada, estourar exceção EntityNotFoundException com mensagem 'Dados pessoais inexistentes'")
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

        assertEquals("Dados pessoais inexistentes", exception.getMessage());
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

        when(personRepository.existsByCpfAndIdNot(input.cpf(), personToBeUpdated.getId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            personUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("CPF já vinculado a um usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir pessoa com telefone passado no input, estourar exceção ConflictException com mensagem 'Telefone já vinculado a um usuário'")
    void deveLancarExcecaoQuandoTelefoneJaEstiverVinculadoAOutraPessoa() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        PersonUpdateUseCaseInputDto input = new PersonUpdateUseCaseInputDto("Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");
        User user = new User("mateus_2", UserRole.CLIENT, true);
        Person personToBeUpdated = new Person(user, "Mateus Souza", LocalDate.of(2003, 8, 16), "46543661054", "11985633657", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(personRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(personToBeUpdated));

        when(personRepository.existsByCpfAndIdNot(input.cpf(), personToBeUpdated.getId()))
                .thenReturn(false);

        when(personRepository.existsByPhoneAndIdNot(input.phone(), personToBeUpdated.getId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            personUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Telefone já vinculado a um usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir pessoa com email passado no input, estourar exceção ConflictException com mensagem 'Email já vinculado a um usuário'")
    void deveLancarExcecaoQuandoEmailJaEstiverVinculadoAOutraPessoa() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        PersonUpdateUseCaseInputDto input = new PersonUpdateUseCaseInputDto("Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");
        User user = new User("mateus_2", UserRole.CLIENT, true);
        Person personToBeUpdated = new Person(user, "Mateus Souza", LocalDate.of(2003, 8, 16), "46543661054", "11985633657", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(personRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(personToBeUpdated));

        when(personRepository.existsByCpfAndIdNot(input.cpf(), personToBeUpdated.getId()))
                .thenReturn(false);

        when(personRepository.existsByPhoneAndIdNot(input.phone(), personToBeUpdated.getId()))
                .thenReturn(false);

        when(personRepository.existsByEmailAndIdNot(input.email(), personToBeUpdated.getId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            personUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Email já vinculado a um usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for funcionário e tentar atualizar outra pessoa, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para atualizar dados pessoais de outro usuário'")
    void deveLancarExcecaoQuandoUsuarioLogadoForFuncionarioETentarAtualizarOutraPessoa() {
        // Arrange
        User userLogged = new User("marcos_3", UserRole.EMPLOYEE, true);
        userLogged.setId(3);
        PersonUpdateUseCaseInputDto input = new PersonUpdateUseCaseInputDto("Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");
        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);
        Person personToBeUpdated = new Person(user, "Mateus Souza", LocalDate.of(2003, 8, 16), "46543661054", "11985633657", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(personRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(personToBeUpdated));

        when(personRepository.existsByCpfAndIdNot(input.cpf(), personToBeUpdated.getId()))
                .thenReturn(false);

        when(personRepository.existsByPhoneAndIdNot(input.phone(), personToBeUpdated.getId()))
                .thenReturn(false);

        when(personRepository.existsByEmailAndIdNot(input.email(), personToBeUpdated.getId()))
                .thenReturn(false);

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            personUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Você não possui autorização para atualizar dados pessoais de outro usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for cliente e tentar atualizar outra pessoa, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para atualizar dados pessoais de outro usuário'")
    void deveLancarExcecaoQuandoUsuarioLogadoForClienteETentarAtualizarOutraPessoa() {
        // Arrange
        User userLogged = new User("vitor_4", UserRole.CLIENT, true);
        userLogged.setId(3);
        PersonUpdateUseCaseInputDto input = new PersonUpdateUseCaseInputDto("Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");
        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);
        Person personToBeUpdated = new Person(user, "Mateus Souza", LocalDate.of(2003, 8, 16), "46543661054", "11985633657", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(personRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(personToBeUpdated));

        when(personRepository.existsByCpfAndIdNot(input.cpf(), personToBeUpdated.getId()))
                .thenReturn(false);

        when(personRepository.existsByPhoneAndIdNot(input.phone(), personToBeUpdated.getId()))
                .thenReturn(false);

        when(personRepository.existsByEmailAndIdNot(input.email(), personToBeUpdated.getId()))
                .thenReturn(false);

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            personUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Você não possui autorização para atualizar dados pessoais de outro usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar pessoa com sucesso e retornar true em propriedade success do output")
    void deveAtualizarPessoaComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(3);
        PersonUpdateUseCaseInputDto input = new PersonUpdateUseCaseInputDto("Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");
        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);
        Person personToBeUpdated = new Person(user, "Mateus Souza", LocalDate.of(2003, 8, 16), "46543661054", "11985633657", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(personRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(personToBeUpdated));

        when(personRepository.existsByCpfAndIdNot(input.cpf(), personToBeUpdated.getId()))
                .thenReturn(false);

        when(personRepository.existsByPhoneAndIdNot(input.phone(), personToBeUpdated.getId()))
                .thenReturn(false);

        when(personRepository.existsByEmailAndIdNot(input.email(), personToBeUpdated.getId()))
                .thenReturn(false);

        // Act
        PersonUpdateUseCaseOutputDto output = personUpdateUseCase.execute(userLogged, 2, input);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(personRepository).save(any(Person.class));
        verify(userRepository, times(1)).findById(String.valueOf(3));
        verify(personRepository, times(1)).findById(String.valueOf(2));
        verify(personRepository, times(1)).existsByCpfAndIdNot(input.cpf(), personToBeUpdated.getId());
        verify(personRepository, times(1)).existsByPhoneAndIdNot(input.phone(), personToBeUpdated.getId());
        verify(personRepository, times(1)).existsByEmailAndIdNot(input.email(), personToBeUpdated.getId());
    }
}