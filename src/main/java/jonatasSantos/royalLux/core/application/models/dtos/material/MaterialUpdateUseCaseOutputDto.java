package jonatasSantos.royalLux.core.application.models.dtos.material;

import java.util.ArrayList;

public record MaterialUpdateUseCaseOutputDto(boolean success, ArrayList<String> warningList) {
}
