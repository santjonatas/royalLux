package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao user, estourar exceção IllegalArgumentException com mensagem 'Usuário criador de atendimento não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoUser(){
        // Arrange
        CustomerService customerService = new CustomerService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.setCreatedByUser(null);
        });

        assertEquals("Usuário criador de atendimento não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao cliente, estourar exceção IllegalArgumentException com mensagem 'Cliente não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoCliente(){
        // Arrange
        CustomerService customerService = new CustomerService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.setClient(null);
        });

        assertEquals("Cliente não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao valor total, estourar exceção IllegalArgumentException com mensagem 'Valor total não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoValorTotal(){
        // Arrange
        CustomerService customerService = new CustomerService();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.setTotalValue(null);
        });

        assertEquals("Valor total não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 750 caracteres a detalhes, estourar exceção IllegalArgumentException com mensagem 'Detalhes do atendimento não deve conter mais que 750 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe750CaracteresADetalhes(){
        // Arrange
        CustomerService customerService = new CustomerService();

        StringBuilder detailBuilder = new StringBuilder();
        for (int i = 0; i < 755; i++) {
            detailBuilder.append(i);
        }
        String detail = detailBuilder.toString();

        // Act + Assert
        String finalDetail = detail;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            customerService.setDetails(finalDetail);
        });

        assertEquals("Detalhes do atendimento não deve conter mais que 750 caracteres", exception.getMessage());
    }
}