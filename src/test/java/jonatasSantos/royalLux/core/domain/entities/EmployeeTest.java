package jonatasSantos.royalLux.core.domain.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    @DisplayName("Quando atribuir valor nulo ao user, estourar exceção IllegalArgumentException com mensagem 'Usuário não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoUser(){
        // Arrange
        Employee employee = new Employee();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employee.setUser(null);
        });

        assertEquals("Usuário não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor nulo ao título, estourar exceção IllegalArgumentException com mensagem 'Título não pode ser nulo'")
    void deveLancarExcecaoQuandoAtribuirValorNuloAoTitulo(){
        // Arrange
        Employee employee = new Employee();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employee.setTitle(null);
        });

        assertEquals("Título não pode ser nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com menos de 5 caracteres ao título, estourar exceção IllegalArgumentException com mensagem 'Título deve conter pelo menos 5 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMenorDe5CaracteresAoTitulo(){
        // Arrange
        Employee employee = new Employee();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employee.setTitle("Test");
        });

        assertEquals("Título deve conter pelo menos 5 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor com mais de 50 caracteres ao título, estourar exceção IllegalArgumentException com mensagem 'Título não deve conter mais que 50 caracteres'")
    void deveLancarExcecaoQuandoAtribuirValorMaiorDe50CaracteresAoTitulo(){
        // Arrange
        Employee employee = new Employee();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employee.setTitle("Teste aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        });

        assertEquals("Título não deve conter mais que 50 caracteres", exception.getMessage());
    }

    @Test
    @DisplayName("Quando atribuir valor abaixo de zero ao salário, estourar exceção IllegalArgumentException com mensagem 'Salário não pode ser menor que zero'")
    void deveLancarExcecaoQuandoAtribuirValorMenorQueZeroAoSalario(){
        // Arrange
        Employee employee = new Employee();

        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            employee.setSalary(BigDecimal.valueOf(-10));
        });

        assertEquals("Salário não pode ser menor que zero", exception.getMessage());
    }
}