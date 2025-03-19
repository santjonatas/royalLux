package jonatasSantos.royalLux.core.application.models.dtos.person;

import java.util.ArrayList;

public record PersonUpdateUseCaseOutputDto(boolean success, ArrayList<String> warningList){}
