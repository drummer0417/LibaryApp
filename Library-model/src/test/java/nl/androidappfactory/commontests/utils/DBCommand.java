package nl.androidappfactory.commontests.utils;

import org.junit.Ignore;

@FunctionalInterface
@Ignore
public interface DBCommand<T> {

    T execute();

}
