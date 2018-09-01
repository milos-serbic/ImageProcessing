package JavaExamples;

import ScalaExamples.ScalaBook;

public class Author implements JavaBook {

    public final String name;

    public Author(String name) {
        this.name = name;
    }

    public static void main(String[] args) {

        Author author = new Author("Author1");
        System.out.println("Calling default method getJavaBooks: " + author.getJavaBooks(author.name));
    }

    public String getName() {
        return name;
    }
}
