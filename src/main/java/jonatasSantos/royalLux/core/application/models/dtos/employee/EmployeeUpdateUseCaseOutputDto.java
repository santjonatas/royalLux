package jonatasSantos.royalLux.core.application.models.dtos.employee;

import java.util.ArrayList;

public record EmployeeUpdateUseCaseOutputDto(boolean success, ArrayList<String> warningList) {
}
