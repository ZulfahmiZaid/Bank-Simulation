package ui;

public class Singleton {

    private static final Singleton instance = new Singleton();
    private String name;
    private Singleton(){}

    public static Singleton getInstance(){
        return instance;
    }
    public String getName(){
        return name;
    }
    public void setName(String input_name){
        this.name = input_name;
    }

}
