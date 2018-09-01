package JavaExamples;
import java.util.Objects;
public class AuthorJavaCase {
    private final String name;

    public AuthorJavaCase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AuthorJavaCase{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorJavaCase that = (AuthorJavaCase) o;
        return Objects.equals(name, that.name);
    }
}