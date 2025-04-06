package jonatasSantos.royalLux.core.application.models.dtos.salonservice;

import java.util.ArrayList;

public record SalonServiceUpdateUseCaseOutputDto(boolean success, ArrayList<String> warningList) {
}
