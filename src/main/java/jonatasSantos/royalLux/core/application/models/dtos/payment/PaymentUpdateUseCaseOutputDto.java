package jonatasSantos.royalLux.core.application.models.dtos.payment;

import java.util.ArrayList;

public record PaymentUpdateUseCaseOutputDto(boolean success, ArrayList<String> warningList) {
}
