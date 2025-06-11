package jonatasSantos.royalLux.core.application.usecases.address;

import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.contracts.repositories.AddressRepository;
import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.application.exceptions.UnauthorizedException;
import jonatasSantos.royalLux.core.application.models.dtos.address.AddressDeleteUseCaseOutputDto;
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

class AddressDeleteUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    AddressRepository addressRepository;

    @InjectMocks
    private AddressDeleteUseCaseImpl addressDeleteUseCase;

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

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressDeleteUseCase.execute(
                    userLogged,
                    2
            );
        });

        assertEquals("Seu usuário é inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando não existir endereço a ser deletado com o mesmo id, estourar exceção EntityNotFoundException com mensagem 'Endereço inexistente'")
    void deveLancarExcecaoQuandoEnderecoASerDeletadoNaoExistir() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        when(userRepository.findById(String.valueOf(userLogged.getId())))
                .thenReturn(Optional.of(userLogged));

        when(addressRepository.findById(String.valueOf(2)))
                .thenReturn(Optional.empty());

        // Act + Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressDeleteUseCase.execute(
                    userLogged,
                    2
            );
        });

        assertEquals("Endereço inexistente", exception.getMessage());
    }

    @Test
    @DisplayName("Quando usuário logado for cliente e tentar deletar endereço de outro usuário, estourar exceção UnauthorizedException com mensagem 'Você não possui autorização para deletar endereço de outro usuário'")
    void deveLancarExcecaoQuandoUsuarioClienteTentarDeletarEnderecoDeOutroUsuario() {
        // Arrange
        User userLogged = new User("vinicius_5", UserRole.CLIENT, true);
        userLogged.setId(5);

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

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
            addressDeleteUseCase.execute(
                    userLogged,
                    2
            );
        });

        assertEquals("Você não possui autorização para deletar endereço de outro usuário", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar endereço com sucesso e retornar true em propriedade success do output")
    void deveDeletarEnderecoComSucesso() {
        // Arrange
        User userLogged = new User("joao_1", UserRole.ADMIN, true);
        userLogged.setId(1);

        User user = new User("mateus_2", UserRole.CLIENT, true);
        user.setId(2);

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

        // Act
        AddressDeleteUseCaseOutputDto output = addressDeleteUseCase.execute(userLogged, 2);

        // Assert
        assertNotNull(output);
        assertEquals(true, output.success());
        verify(userRepository, times(1)).findById(String.valueOf(1));
        verify(addressRepository, times(1)).findById(String.valueOf(2));
        verify(addressRepository).delete(any(Address.class));
        verify(addressRepository, times(1)).delete(address);
    }
}