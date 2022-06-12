package testCases;

public class Cclass extends Bclass{
    private final String someVar;
    Cclass(){
        this("asd");
    }

    Cclass(String someVar){
        super();
        this.someVar = someVar;
    }
}
