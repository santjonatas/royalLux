package jonatasSantos.royalLux.core.application.usecases.address;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.AddressRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressCreateUseCaseInputDto;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressCreateUseCaseOutputDto;
import jonatasSantos.royalLux.core.domain.entities.Address;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.AddressState;
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

class AddressCreateUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    private AddressCreateUseCaseImpl addressCreateUseCase;

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
        AddressCreateUseCaseInputDto input = new AddressCreateUseCaseInputDto(userLogged.getId(),
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressState.RS,
                "15910-000");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressCreateUseCase.execute(
                    userLogged,
                    input
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir usuário de endereço a ser criado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Usuário inexistente'")
    void deveLancarExcecaoQuandoUsuarioASerAtualizadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

        AddressCreateUseCaseInputDto input = new AddressCreateUseCaseInputDto(user.getId(),
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressState.RS,
                "15910-000");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressCreateUseCase.execute(
                    userLogged,
                    input
            );
        });

        assertEquals("Usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário funcionário tentar criar endereço para qualquer usuário que não seja ele, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para criar endereço de outro usuário'")
    void deveLancarExcecaoQuandoUsuarioEmployeeTentarAtualizarQualquerUsuario(){
        // Arrange
        User userLogged = new User("marcos_3", UserRole.EMPLOYEE, true);
        userLogged.setId(3);

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

        AddressCreateUseCaseInputDto input = new AddressCreateUseCaseInputDto(user.getId(),
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressState.RS,
                "15910-000");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(user));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            addressCreateUseCase.execute(
                    userLogged,
                    input
            );
        });

        assertEquals("Você não possui autorização para criar endereço de outro usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário cliente tentar criar endereço para qualquer usuário que não seja ele, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para criar endereço de outro usuário'")
    void deveLancarExcecaoQuandoUsuarioClienteTentarAtualizarQualquerUsuario(){
        // Arrange
        User userLogged = new User("vitor_4", UserRole.CLIENT, true);
        userLogged.setId(4);

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

        AddressCreateUseCaseInputDto input = new AddressCreateUseCaseInputDto(user.getId(),
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressState.RS,
                "15910-000");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(user));

        // Act + Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            addressCreateUseCase.execute(
                    userLogged,
                    input
            );
        });

        assertEquals("Você não possui autorização para criar endereço de outro usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Deve criar endereço com sucesso e retornar id de endereço cadastrado")
    void deveCriarEnderecoComSucesso(){
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(4);

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

        AddressCreateUseCaseInputDto input = new AddressCreateUseCaseInputDto(user.getId(),
                "Avenida Wenceslau Escobar",
                "197",
                " ",
                "Alegria",
                "Porto Alegre",
                AddressState.RS,
                "15910-000");

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(userRepository.findById(String.valueOf(input.userId())))
                .thenReturn(Optional.of(user));

        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> {
            Address addressCreated = invocation.getArgument(0);
            addressCreated.setId(2);
            return addressCreated;
        });

        // Act
        AddressCreateUseCaseOutputDto output = addressCreateUseCase.execute(userLogged, input);

        // Assert
        assertNotNull(output);
        assertEquals(2, output.addressId());
        verify(addressRepository).save(any(Address.class));
        verify(addressRepository, times(1)).save(any(Address.class));
    }
}