package common.transfer.request.standart;

import java.util.List;

import common.models.Entity;


/**
 * Запрос с элементом.
 * @author Septyq
 */
public class EntityRequest extends StandartRequest {
    private static final long serialVersionUID = 2593749874L;

    private final Entity entity;

    public EntityRequest(String name, Entity entity) {
        super(name);
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public static boolean validate(List<?> args) {
        return (args.size() == 1 && args.get(0) instanceof Entity && args.get(0) != null);
    }
}
