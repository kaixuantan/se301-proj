package se301.project.factory;

/**
 * Factory interface for creating objects.
 *
 * @param <T> the type of object to create
 * @param <U> the type of argument to pass to the factory
 */
public interface Factory<T, U> {
    T create(U param);
}
