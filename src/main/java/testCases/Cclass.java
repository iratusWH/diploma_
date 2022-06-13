package testCases;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Cclass extends Bclass {
    private final String someVar;

    Cclass() {
        this("asd");
    }

    Cclass(String someVar) {
        super();
        this.someVar = someVar;
    }

    void someMethod() {
        for (int i = 10, j = 0; j < 10; j++) {
            log.info("{}", i * j);
            i = i + (i + j);
        }
    }
}
