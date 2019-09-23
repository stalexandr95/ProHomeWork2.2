import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

    @Retention(RetentionPolicy.RUNTIME)
    @interface SaveTo {
        String path();
    }
    @Retention(RetentionPolicy.RUNTIME)
    @interface Saver {}

    @SaveTo(path = "D:\\JavaEE\\1.txt") //choose way on your folder
    class Container {
        String s = "Sample text";

        @Saver
        public void save(String path) throws IOException {
            FileWriter w = new FileWriter(path);
            try {
                w.write(s);
            } finally {
                w.close();
            }
        }
    }

public class Main {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {
        Container c = new Container();
        Class<?> cls = c.getClass();

        if ( ! cls.isAnnotationPresent(SaveTo.class)) {
            System.out.println("Error!");
            return;
        }

        SaveTo saveTo = cls.getAnnotation(SaveTo.class);
        String path = saveTo.path();
        Method[] methods = cls.getDeclaredMethods();

        for (Method m : methods) {
            if (m.isAnnotationPresent(Saver.class)) {
                m.invoke(c, path);
                break;
            }
        }

        System.out.println("Check file in choosen directory");

    }
}
