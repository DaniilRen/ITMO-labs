package util.transfer.request.standart;

import java.util.List;

import models.Entity;


/**
 * Комбинированный запрос с элементом и id.
 * @author Septyq
 */
public class CombinedRequest extends IdRequest {
    private final Entity entity;

    public CombinedRequest(String name, Entity entity, int id) {
        super(name, id);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public static boolean validate(List<?> args) {
        return (args.size() == 2 
            && args.get(0) instanceof Entity && args.get(0) != null 
            && isNumeric(args.get(1))) && args.get(1) != null;
    }

}
