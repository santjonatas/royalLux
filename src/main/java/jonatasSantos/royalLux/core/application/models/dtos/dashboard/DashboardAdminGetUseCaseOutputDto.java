package jonatasSantos.royalLux.core.application.models.dtos.dashboard;

import jonatasSantos.royalLux.core.domain.enums.DashboardPeriod;

import java.math.BigDecimal;


public record DashboardAdminGetUseCaseOutputDto(Long totalPayments, BigDecimal profit, Long totalServices, String mostCommonSalonServiceName, Long totalMostCommonSalonService) {
}
