package JavaExamples;

public interface JavaBook {

    default String getJavaBooks(String personName) {
        return "JavaBook of " + personName;
    }
}
