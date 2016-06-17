package de.aktey.scanndal.classmappertest;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ruben
 * Date: 04.08.13
 * Time: 19:57
 */
public class ClassMapperTestClass {
    public String foo() {
        return "bar";
    }
    public void forEachLoop() {
        List<String> list = Arrays.asList("a", "b");
        list.forEach( (l) -> {
            System.out.println(l);
        });
    }
}
