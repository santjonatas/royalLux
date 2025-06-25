package jonatasSantos.royalLux.core.application.contracts.services;

public interface SerializerService {

    public String toJson(Object obj);

    public <T> T fromJson(String json, Class<T> clazz);
}
