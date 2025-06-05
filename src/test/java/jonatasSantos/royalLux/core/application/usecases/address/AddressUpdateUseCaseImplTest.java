package jonatasSantos.royalLux.core.application.usecases.address;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.AddressRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressUpdateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressUpdateUseCaseOutputDto;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddressUpdateUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    private AddressUpdateUseCaseImpl addressUpdateUseCase;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Quando não existir usuário logado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Seu usuário é inexistente'")
    void deveLancarExcecaoQuandoUsuarioLogadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        AddressUpdateUseCaseInputDto input = new AddressUpdateUseCaseInputDto(
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir endereço a ser atualizado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Endereço é inexistente'")
    void deveLancarExcecaoQuandoEnderecoASerAtualizadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        AddressUpdateUseCaseInputDto input = new AddressUpdateUseCaseInputDto(
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(addressRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Endereço é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for funcionário tentar atualizar endereço de outro usuário, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para atualizar endereço de outro usuário'")
    void deveLancarExcecaoQuandoFuncionarioTentarAtualizarEnderecoDeOutroUsuario() {
        // Arrange
        User userLogged = new User("marcos_3", UserRole.EMPLOYEE, true);
        userLogged.setId(1);

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

        AddressUpdateUseCaseInputDto input = new AddressUpdateUseCaseInputDto(
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");

        Address address = new Address(
                user,
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(addressRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(address));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            addressUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Você não possui autorização para atualizar endereço de outro usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for cliente tentar atualizar endereço de outro usuário, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para atualizar endereço de outro usuário'")
    void deveLancarExcecaoQuandoClienteTentarAtualizarEnderecoDeOutroUsuario() {
        // Arrange
        User userLogged = new User("vinicius_4", UserRole.CLIENT, true);
        userLogged.setId(4);

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

        AddressUpdateUseCaseInputDto input = new AddressUpdateUseCaseInputDto(
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");

        Address address = new Address(
                user,
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(addressRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(address));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            addressUpdateUseCase.execute(
                    userLogged,
                    2,
                    input
            );
        });

        assertEquals("Você não possui autorização para atualizar endereço de outro usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar endereço com sucesso e retornar true em propriedade success do output")
    void deveAtualizarEnderecoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

        AddressUpdateUseCaseInputDto input = new AddressUpdateUseCaseInputDto(
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");

        Address address = new Address(
                user,
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressStates.RS,
                "15910-000");

        address.setId(2);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(addressRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.of(address));

        // Act
        AddressUpdateUseCaseOutputDto output = addressUpdateUseCase.execute(userLogged, 2, input);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(addressRepository).save(any(Address.class));
        verify(userRepository, times(1)).findById(String.valueOf(1));
        verify(addressRepository, times(1)).findById(String.valueOf(2));
        verify(addressRepository, times(1)).save(any(Address.class));
    }
}