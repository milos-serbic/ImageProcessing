package JavaExamples;

import java.util.ArrayList;
import java.util.List;

public class Book {

    private String title;
    public final Author author;

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    public void printAuthor(String prefix) {
        String concat = prefix + this.author.name;
        System.out.println(concat);
    }

    public String toString() {
        return "BookTitle: " + this.title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static void main(String[] args) {

        Book book = new Book("JavaBook", new Author("JavaAuthor"));
        book.printAuthor("Name of Author: ");
        System.out.println("Title of Book: " + book.getTitle());
        book.setTitle("NewTitle");
        System.out.println(book);

        AuthorJavaCase authorCase = new AuthorJavaCase("AuthorCase");
        AuthorJavaCase authorCaseSame = new AuthorJavaCase("AuthorCase");
        AuthorJavaCase authorCase2 = new AuthorJavaCase("AuthorCase2");

        System.out.println("Name from Case: " + authorCase.getName());
        System.out.println("toString from Case: " + authorCase.toString());
        System.out.println("hashCode from Case: " + authorCase.hashCode());
        System.out. println("Equals for same value, from Case: " + (authorCase.equals(authorCaseSame)));
        System.out.println("Equals for different value, from Case: " + (authorCase.equals(authorCase2)));

        Author author1 = new Author("AuthorForFiltering1");
        Author author2 = new Author("AuthorForFiltering2");
        Author author3 = new Author("Author3");
        List<Author> authorList = new ArrayList<Author>();
        authorList.add(author1);
        authorList.add(author2);
        authorList.add(author3);

        authorList.stream()
                .map(author -> new Book("Title for Author: " + author.getName(), author))
                .filter(bookLambda -> bookLambda.getTitle().contains("AuthorForFiltering"))
                .distinct()
                .map(Book::getTitle)
                .map(String::toLowerCase)
                .forEach(System.out::println);
    }
}
