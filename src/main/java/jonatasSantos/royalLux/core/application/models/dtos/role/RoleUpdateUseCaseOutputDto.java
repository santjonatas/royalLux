package jonatasSantos.royalLux.core.application.models.dtos.role;

import java.util.ArrayList;

public record RoleUpdateUseCaseOutputDto(boolean success, ArrayList<String> warningList) {
}
