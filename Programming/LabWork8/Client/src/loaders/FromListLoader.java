package loaders;

import common.descriptions.LoadDescription;
import managers.AbstractLoader;
import parsers.Parser;

import java.util.ArrayList;

public class FromListLoader extends AbstractLoader {
    private final ArrayList<String> fields;
    private int element;
    private boolean insideCall;

    public FromListLoader(ArrayList<String> fields){
        super(null);
        this.fields = fields;
        element = 0;
        insideCall = false;
        parser = new Parser();
    }
    @Override
    public <D extends LoadDescription<?>> D enter(D description) {
        if (insideCall) {
            insideCall = false;
        } else {
            element = 0;
        }

        return super.enter(description);
    }

    @Override
    public <T extends LoadDescription<?>> T enterDate(T t) {
        String s = fields.get(element++);
        T res = (T)t.setValue(parser.parse(s, t.getType()));
        if (t.getFieldOfDescriptionSetter() != null)
            t.setField(t.getValue());
        return res;
    }

    @Override
    public <T extends LoadDescription<Enum>> T enterEnum(T t) {
        String s = fields.get(element++);
        T res = (T) t.setValue(parse(s, (Class<Enum>) t.getType()));
        if (t.getFieldOfDescriptionSetter() != null)
            t.setField(t.getValue());
        return res;
    }

    @Override
    public <T extends LoadDescription<?>> T enterWrapper(T t) {
        try {
            String s = fields.get(element++);
            T res = (T) t.setValue(parse(s, t.getType()));
            if (t.getFieldOfDescriptionSetter() != null)
                t.setField(t.getValue());
            return res;
        } catch (Exception e){
            throw new IllegalArgumentException(t.getDescription().toString());
        }
    }

    @Override
    public LoadDescription<String> enterString(LoadDescription<String> loadDescription) {
        String s = fields.get(element++);
        LoadDescription<String> res = loadDescription.setValue(s);
        if (loadDescription.getFieldOfDescriptionSetter() != null)
            loadDescription.setField(loadDescription.getValue());
        return res;
    }

    @Override
    public  <D extends LoadDescription<?>> D enterComposite(D description) {
        description.getFields().forEach((LoadDescription<?> ld) -> {insideCall = true; this.enter(ld);});
        description.build();
        if (description.getFieldOfDescriptionSetter() != null)
            description.setField(description.getValue());
        return description;
    }
}
