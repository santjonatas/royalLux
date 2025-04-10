package jonatasSantos.royalLux.core.application.usecases.auth;

import jonatasSantos.royalLux.core.application.contracts.repositories.UserRepository;
import jonatasSantos.royalLux.core.domain.entities.User;
import jonatasSantos.royalLux.core.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RegisterUseCaseImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    @InjectMocks
    private RegisterUseCaseImpl registerUseCase;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Realizar criação de user com sucesso")
    void executeCase1() {
        User user = new User("joao_1", UserRole.CLIENT, true);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);
        when(user.validatePassword("Teste123@")).thenReturn("Teste123@");
//        when(passwordEncoder.encode("Teste123@")).thenReturn("$2a$10$vuyp4Ye.glwje0Qy9/uVc.nO3HcovvGU6PA7wFv24LHaO.Wtq6ley");


    }
}