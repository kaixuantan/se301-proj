package se301.project.factory;

public interface Factory<T, K> {
    T create(K arg);
}
