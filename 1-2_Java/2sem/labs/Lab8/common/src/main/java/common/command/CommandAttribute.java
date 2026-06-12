package common.command;

import java.io.Serializable;

import common.transfer.request.Request;


/**
 * Класс для хранения атрибутов команды
 * @author Septyq
 */
public class CommandAttribute implements Describable, Serializable {
    private static final long serialVersionUID = 847238174L;

    private final String name;
    private final String description;
    private final PublicityMarker publicity;
    private final Class<? extends Request> argsType;

    public CommandAttribute(String name, String description, Class<? extends Request> argsType, PublicityMarker publicity) {
        this.name = name;
        this.description = description;
        this.argsType = argsType;
        this.publicity = publicity;
    }
    
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public PublicityMarker getPublicity() {
        return publicity;
    }

    public Class<? extends Request> getArgsType() {
        return argsType;
    }
}
