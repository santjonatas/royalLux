package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalonServiceCustomerServiceTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao atendimento, estourar exceção IllegalArgumentException com mensagem 'Atendimento não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoAtendimento(){
        // Arrange
        SalonServiceCustomerService salonServiceCustomerService = new SalonServiceCustomerService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonServiceCustomerService.setCustomerService(null);
        });

        assertEquals("Atendimento não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao serviço, estourar exceção IllegalArgumentException com mensagem 'Serviço não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoServico(){
        // Arrange
        SalonServiceCustomerService salonServiceCustomerService = new SalonServiceCustomerService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonServiceCustomerService.setSalonService(null);
        });

        assertEquals("Serviço não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao funcionário, estourar exceção IllegalArgumentException com mensagem 'Funcionário não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoFuncionario(){
        // Arrange
        SalonServiceCustomerService salonServiceCustomerService = new SalonServiceCustomerService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonServiceCustomerService.setEmployee(null);
        });

        assertEquals("Funcionário não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo a completo, estourar exceção IllegalArgumentException com mensagem 'Completo não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloACompleto(){
        // Arrange
        SalonServiceCustomerService salonServiceCustomerService = new SalonServiceCustomerService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            salonServiceCustomerService.setCompleted(null);
        });

        assertEquals("Completo não pode ser nulo", exception.getMessage());
    }
}