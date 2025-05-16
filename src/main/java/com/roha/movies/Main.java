package com.roha.movies;
import com.roha.movies.fetcher.Initializer;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {
        //inject initializer here
        @Inject
        Initializer initializer;
        @Override
        public int run(String... args) throws Exception {
            System.out.println("Do startup logic here");
            initializer.init();
            Quarkus.waitForExit();
            return 0;
        }
    }
}
