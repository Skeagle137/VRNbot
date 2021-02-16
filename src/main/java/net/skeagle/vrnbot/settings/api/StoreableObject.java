package net.skeagle.vrnbot.settings.api;

public abstract class StoreableObject<T> {

    protected T deserialize() {
        return null;
    }

    protected Object serialize() {
        return null;
    }
}
