package jonatasSantos.royalLux.core.domain.entities;

import jonatasSantos.royalLux.core.domain.enums.PaymentMethod;
import jonatasSantos.royalLux.core.domain.enums.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    @DisplayName("Quando atribuir atendimento nulo, deve lançar IllegalArgumentException com mensagem 'Atendimento não pode ser nulo'")
    void deveLancarExcecaoQuandoAtendimentoForNulo() {
        // Arrange
        Payment pagamento = new Payment();

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            pagamento.setCustomerService(null);
        });

        assertEquals("Atendimento não pode ser nulo", ex.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir usuário criador nulo, deve lançar IllegalArgumentException com mensagem 'Usuário criador de pagamento não pode ser nulo'")
    void deveLancarExcecaoQuandoUsuarioCriadorForNulo() {
        // Arrange
        Payment pagamento = new Payment();

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            pagamento.setCreatedByUser(null);
        });

        assertEquals("Usuário criador de pagamento não pode ser nulo", ex.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir descrição nula, deve lançar IllegalArgumentException com mensagem 'Descrição não pode ser nula'")
    void deveLancarExcecaoQuandoDescricaoForNula() {
        // Arrange
        Payment pagamento = new Payment();

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            pagamento.setDescription(null);
        });

        assertEquals("Descrição não pode ser nula", ex.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir nome do pagador nulo, deve lançar IllegalArgumentException com mensagem 'Nome do pagador não pode ser nulo'")
    void deveLancarExcecaoQuandoPayerNameForNulo() {
        // Arrange
        Payment pagamento = new Payment();

        // Act + Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            pagamento.setPayerName(null);
        });

        assertEquals("Nome do pagador não pode ser nulo", ex.getMessage());
    }

    @Test
    @DisplayName("Deve setar corretamente valores válidos e não estourar exceção")
    void deveSetarValoresValidosComSucesso() {
        // Arrange
        CustomerService customerService = new CustomerService();
        User user = new User("usuario_valido", jonatasSantos.royalLux.core.domain.enums.UserRole.EMPLOYEE, true);
        Payment pagamento = new Payment();

        // Act
        pagamento.setCustomerService(customerService);
        pagamento.setCreatedByUser(user);
        pagamento.setStatus(PaymentStatus.PAGO);
        pagamento.setMethod(PaymentMethod.DINHEIRO);
        pagamento.setDescription("Pagamento concluído");
        pagamento.setTransactionId("TXN123");
        pagamento.setPaymentToken("TOKEN456");
        pagamento.setPaymentUrl("https://pagamento.com");
        pagamento.setPayerName("Maria");
        pagamento.setCreatedAt(LocalDateTime.now());
        pagamento.setUpdatedAt(LocalDateTime.now());

        // Assert
        assertEquals("Maria", pagamento.getPayerName());
        assertEquals("Pagamento concluído", pagamento.getDescription());
        assertEquals(PaymentStatus.PAGO, pagamento.getStatus());
        assertEquals(PaymentMethod.DINHEIRO, pagamento.getMethod());
        assertEquals("TXN123", pagamento.getTransactionId());
        assertEquals("TOKEN456", pagamento.getPaymentToken());
        assertEquals("https://pagamento.com", pagamento.getPaymentUrl());
        assertEquals(customerService, pagamento.getCustomerService());
        assertEquals(user, pagamento.getCreatedByUser());
    }
}