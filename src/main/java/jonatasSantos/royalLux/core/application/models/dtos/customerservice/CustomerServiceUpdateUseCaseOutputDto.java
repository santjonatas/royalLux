package jonatasSantos.royalLux.core.application.models.dtos.customerservice;

import java.util.ArrayList;

public record CustomerServiceUpdateUseCaseOutputDto(boolean success, ArrayList<String> warningList) {
}
