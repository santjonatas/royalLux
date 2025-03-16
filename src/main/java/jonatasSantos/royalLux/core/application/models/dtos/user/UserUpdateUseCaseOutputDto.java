package jonatasSantos.royalLux.core.application.models.dtos.user;

import java.util.ArrayList;

public record UserUpdateUseCaseOutputDto (boolean success, ArrayList<String> warningList){}