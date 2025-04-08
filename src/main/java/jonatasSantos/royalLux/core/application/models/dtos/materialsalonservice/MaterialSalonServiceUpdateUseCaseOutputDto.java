package jonatasSantos.royalLux.core.application.models.dtos.materialsalonservice;

import java.util.ArrayList;

public record MaterialSalonServiceUpdateUseCaseOutputDto(boolean success, ArrayList<String> warningList) {
}
