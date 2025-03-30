package jonatasSantos.royalLux.core.application.models.dtos.address;

import java.util.ArrayList;

public record AddressUpdateUseCaseOutputDto(boolean success, ArrayList<String> warningList) {}
