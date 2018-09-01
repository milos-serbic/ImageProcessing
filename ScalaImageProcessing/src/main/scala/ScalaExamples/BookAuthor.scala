package ScalaExamples

abstract class Person(val name: String)
trait ScalaBook {
  val personName: String
  def getScalaBooks = "ScalaBook of " +  personName
}
class Author( name: String) extends Person(name: String) with ScalaBook {
  val personName = name
}
class Editor(name: String) extends Person(name: String) with ScalaBook {
  val personName = name
}

class Book (private var _title: String = "ScalaBook", val author: Author) {

  def title = _title
  def title_= (newTitle: String): Unit = _title = newTitle

  def printAuthor(prefix: String = "Prefix") = {
    var concat = prefix + author.name
    println(concat)
  }

  override def toString: String = s"BookTitle: $title"
}

case class AuthorCase(val name: String)
case class BookCase(val name: String, val author: AuthorCase)

object MainApp {

  def main(args: Array[String]) {
    var book = new Book(author = new Author("ScalaAuthor"))
    book printAuthor "Name of Author: "
    println("Title of Book: " + book.title)
    book.title = "NewTitle"
    println(book)

    println(checkObjects(book));
    println(checkObjects(new Author("TestAuthor")));

    var authorCase = AuthorCase("AuthorCase");
    val authorCaseSame = AuthorCase("AuthorCase");
    val authorCase2 = AuthorCase("AuthorCase2");

    println("Name from Case: " + authorCase.name);
    println("toString from Case: " + authorCase.toString);
    println("hashCode from Case: " + authorCase.hashCode);
    println("Equals for same value, from Case: " + (authorCase == authorCaseSame));
    println("Equals for different value, from Case: " + (authorCase == authorCase2));

    val bookCase = BookCase("BookCase", authorCase2)
    println("Match for AuthorCase: " + checkCase(authorCase));
    println("Match for AuthorCase2: " + checkCase(authorCase2));
    println("Match for BookCase: " + checkCase(bookCase));

    var author = new Author("AuthorName");
    var editor = new Editor("EditorName");
    println("Autohor Books: " + author.getScalaBooks);
    println("Editor Books: " + editor.getScalaBooks);

    val authorList = List(new AuthorCase("Author1"),
      new AuthorCase("Author2"),
      new AuthorCase("Author3"))

    val authorNewList =
      for (i <- authorList;
           j <- 0 to 5 if j % 2 == 0)
        yield new Author(i.name + " " + j)

    authorNewList.foreach(changedAuthorName => println(changedAuthorName.name))
  }

  val checkObjects = (obj : Any) => obj match {
    case a: Author => "Get name from match: " + a.name
    case b: Book => "Call toString from match" + b.toString
    case _ => "Default..."
  }

  val checkCase = (obj : AnyRef) => obj match {
    case AuthorCase("AuthorCase") => "AuthorCase"
    case AuthorCase("AuthorCase2") => "AuthorCase2"
    case BookCase(title, author) => AuthorCase(author.name.toString)
    case _ => "Default..."
  }
}
