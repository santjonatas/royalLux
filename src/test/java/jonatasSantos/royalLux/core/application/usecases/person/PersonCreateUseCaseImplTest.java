package jonatasSantos.royalLux.core.application.usecases.person;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.PersonRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.ConflictException;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.person.PersonUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.user.UserUpdateUseCaseInputDto;
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

class PersonCreateUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonCreateUseCaseImpl personCreateUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        PersonCreateUseCaseInputDto input = new PersonCreateUseCaseInputDto(2, "Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir usuário de pessoa a ser criada com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioDePessoaASerCriadaNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        PersonCreateUseCaseInputDto input = new PersonCreateUseCaseInputDto(2, "Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            personCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for cliente e role de pessoa a ser criada não for cliente, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para criar pessoa com outra permissão'")
    void deveLancarExcecaoQuandoUsuarioLogadoForClienteERoleDePessoaASerCriadaNaoForCliente() {
        // Arrange
        User userLogged = new User("mateus_2", UserRole.CLIENT, true);
        PersonCreateUseCaseInputDto input = new PersonCreateUseCaseInputDto(2, "Marcos Pereira", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "marcos.pereira@gmail.com");
        User existingUser = new User("marcos_3", UserRole.EMPLOYEE, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(existingUser));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            personCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Você não possui autorização para criar pessoa com outra permissão", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for cliente e id de usuário de pessoa a ser criada for diferente de usuário logado, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para criar outra pessoa'")
    void deveLancarExcecaoQuandoUsuarioLogadoForClienteEIdDeUsuarioLogadoForDiferenteDeIdDeUsuarioDePessoaASerCriada() {
        // Arrange
        User userLogged = new User("mateus_2", UserRole.CLIENT, true);
        userLogged.setId(2);
        PersonCreateUseCaseInputDto input = new PersonCreateUseCaseInputDto(4, "Felipe Castro", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "felipe.castro@gmail.com");
        User existingUser = new User("felipe_4", UserRole.CLIENT, true);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(existingUser));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            personCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Você não possui autorização para criar outra pessoa", exception.getMessage());
    }

    @Test
    @DisplayName("Quando já existir pessoa vinculada a usuário, estourar exceção ConflictException com mensagem 'Pessoa já vinculada a um usuário'")
    void deveLancarExcecaoQuandoUsuarioJaEstiverVinculadoAOutraPessoa() {
        // Arrange
        User userLogged = new User("mateus_2", UserRole.CLIENT, true);
        userLogged.setId(2);
        PersonCreateUseCaseInputDto input = new PersonCreateUseCaseInputDto(2, "Mateus Souza", LocalDate.of(2000, 7, 3), "07966562077", "11950264148", "mateus.souza@gmail.com");
        User existingUser = userLogged;

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(existingUser));

        when(personRepository.existsByUserId(existingUser.getId()))
                .thenReturn(true);

        // Act + Assert
        ConflictException exception = assertThrows(ConflictException.class, () -> {
            personCreateUseCase.execute(userLogged, input);
        });

        assertEquals("Pessoa já vinculada a um usuário", exception.getMessage());
    }
}