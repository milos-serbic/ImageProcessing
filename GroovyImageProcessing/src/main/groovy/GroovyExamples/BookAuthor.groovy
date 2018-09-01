package GroovyExamples

import groovy.json.JsonBuilder
import groovy.xml.MarkupBuilder

abstract class Person {
    String name
}
trait GroovyBook {
    def getGroovyBooks() {
        return "GroovyBook of ${getName()}"
    }
}
class Author extends Person implements GroovyBook {
}
class Editor extends Person implements GroovyBook {
}

class BookMetaClass {
    String title
    Integer numberOfPages

    def invokeMethod(String name, Object args) {
        return "Method invocaton with name: $name and parametar: $args"
    }

    def getProperty(String name) {
        if (name != 'naslov')
            return metaClass.getProperty(this, name)
        else
            return title
    }
    void setProperty(String name, Object value) {
        this.@"$name" = "Value $value is overridden"
    }
}

class Book {
    private String title
    Author author
    Integer numberOfPages

    def printAuthor(authorPrefix = 'Author Info: ') {
        System.out.println(authorPrefix + author.name)
    }
    void printTitle(params) {
        System.out.println(params.titlePrefix + " " + title)
    }

    String toString() {
        title
    }

    static void main(String[] args) {
        def book = "BookDynamic"
        System.out.println("Dynamic typing for Book: " + book)

        book = new Book()
        book.title = "Book1"
        book.author = new Author(name: "Author1")
        System.out.println("Book title: " + book.title + ", author: " +  book.author.name)

        book.setAuthor(new Author(name: "Author1 with Setter"))
        System.out.println("Book title: " + book.title + ", author: " +  book.getAuthor().name)

        book = new Book(title: "Book2", author: new Author(name: "Author2"))
        System.out.println("Book title: ${book.title}, author: ${book.author.name}")

        book = new Book(title: "Book3")
        System.out.println("Book title: ${book.title}, author: ${book.author?.name}")

        book = new Book(author: new Author(name: "Author4"))
        System.out.println("Book title: ${book.title?:"NoTitle"}, author: ${book.author?.name}")

        book.printAuthor()
        book.printAuthor "Info about Author: "
        book.title = "Title2"
        book.printTitle titlePrefix:"Book Title: "

        def author = new Author(name: "GroovyAuthor")
        def editor = new Editor(name: "GroovyEditor")
        System.out.println(author.getGroovyBooks())
        System.out.println(editor.getGroovyBooks())

        def bookList = [new Book(title: "Book1", numberOfPages: 100),
                        new Book(title: "Book2", numberOfPages: 230),
                        new Book(title: "Book3", numberOfPages: 340)]

        bookList.each { println("Number of Pages: ${it.numberOfPages} for Book: ${it.title}")}
        bookList.eachWithIndex  { it, i -> println("Number of Pages: $it.numberOfPages for Book: ${it.title} with Index $i")}
        println(bookList.collect { "Double number of Pages: ${it.numberOfPages * 2}" })
        println("find: " + bookList.find { it.numberOfPages > 200 })
        println("findAll: " + bookList.findAll { it.numberOfPages > 200 })
        println("every: " + bookList.every { it.numberOfPages > 200 })
        println("any: " + bookList.any { it.numberOfPages > 200 })
        println("sum: " + bookList.sum { it.numberOfPages })
        println("join: " + bookList.join(','))
        println("inject: " + bookList.inject('Number of Pages ') { str, item -> str + " " + item.numberOfPages })
        println("min: " + bookList.min { it.numberOfPages })
        println("max: " + bookList.max { it.numberOfPages })

        def bookMap = [
                Book1:100,
                Book2:230,
                Book3:340]
        bookMap.each { entry ->
            println "Book title: $entry.key , number of pages: $entry.value"
        }


        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)
        xml.bookList() {
            bookObj(isbn:'123') {
                title('Book1')
                authorObj() {
                    name 'Author1'
                }
            }
            bookObj(isbn:'456') {
                title('Book2')
                authorObj() {
                    name 'Author1'
                }
            }
        }
        println writer.toString()
        def bookListXml = """
                        <bookList>
                          <book isbn='123'>
                            <title>Book1</title>
                            <authorObj>
                              <name>Author1</name>
                            </authorObj>
                          </book>
                          <book isbn='456'>
                            <title>Book2</title>
                            <authorObj>
                              <name>Author1</name>
                            </authorObj>
                          </book>
                        </bookList>
                    """
        def response = new XmlParser().parseText(bookListXml)
        println("Title of the Book: " + response.book[0].title.text())

        def bookMetaObject = new BookMetaClass(title: "Book1", numberOfPages: 100)
        println(bookMetaObject.someMethod("Test", 5))
        println("Getter of title on Serbian: $bookMetaObject.naslov")
        println("Regular getter: $bookMetaObject.numberOfPages")
        bookMetaObject.title = "NewBook"
        println(bookMetaObject.title)
        println("Getting title without getters: "
                + bookMetaObject.metaClass.getAttribute(bookMetaObject, 'title'))
        bookMetaObject.metaClass.setAttribute(bookMetaObject, 'title', 'Book2')
        println("Setting title without setters, new title is: $bookMetaObject.title")
    }
}
