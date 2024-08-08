package reflection.api;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

public class InvestigatorImp implements Investigator {

    private Class clazz;
    private Object obj;


    @Override
    public void load(Object anInstanceOfSomething)
    {
       this.clazz = anInstanceOfSomething.getClass();
       this.obj = anInstanceOfSomething;
    }

    @Override
    public int getTotalNumberOfFields() {
        return clazz.getFields().length;
    }

    @Override
    public int getTotalNumberOfMethods() {
        return clazz.getMethods().length;
    }

    @Override
    public int getTotalNumberOfConstructors() {
        return clazz.getConstructors().length;
    }

    @Override
    public int getCountOfStaticMethods() {
        Method[] methods = clazz.getMethods();
        int count = 0;
        for(Method m : methods)
        {
            if(Modifier.isStatic((m.getModifiers())))
                count++;
        }
        return count;
    }
    @Override
    public int getCountOfConstantFields() {
        Field[] fields = clazz.getFields();
        int count = 0;
        for (Field f : fields) {
            if (Modifier.isFinal(f.getModifiers()))
                count++;
        }
        return count;
    }

    @Override
    public boolean isExtending() {
        return false;
    }

    @Override
    public boolean isParentClassAbstract() {
        return false;
    }

    @Override
    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
        return 0;
    }

    @Overridek
    public Object createInstance(int numberOfArgs, Object... args) {
        return null;
    }

    @Override
    public Object elevateMethodAndInvoke(String name, Class<?>[] parametersTypes, Object... args) {
        return null;
    }

    @Override
    public Set<String> getAllImplementedInterfaces() {
        return Set.of();
    }

    @Override
    public String getInheritanceChain(String delimiter) {
        return "";
    }

    @Override
    public String getParentClassSimpleName() {
        return "";
    }

    @Override
    public Set<String> getNamesOfAllFieldsIncludingInheritanceChain() {
        return Set.of();
    }

}
