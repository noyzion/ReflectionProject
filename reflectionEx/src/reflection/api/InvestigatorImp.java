package reflection.api;
import java.lang.reflect.*;
import java.util.HashSet;
import java.util.Set;

public class InvestigatorImp implements Investigator {

    private Class<?> clazz;
    private Object obj;

    @Override
    public void load(Object anInstanceOfSomething) {
        this.clazz = anInstanceOfSomething.getClass();
        this.obj = anInstanceOfSomething;
    }

    @Override
    public int getTotalNumberOfFields() {
        return clazz.getDeclaredFields().length;
    }

    @Override
    public int getTotalNumberOfMethods() {
        return clazz.getDeclaredMethods().length;
    }

    @Override
    public int getTotalNumberOfConstructors() {
        return clazz.getConstructors().length;
    }

    @Override
    public String getParentClassSimpleName() {
        return clazz.getSuperclass().getSimpleName();
    }

    @Override
    public int getCountOfStaticMethods() {
        Method[] methods = clazz.getDeclaredMethods();
        int count = 0;
        for (Method m : methods) {
            if (Modifier.isStatic((m.getModifiers())))
                count++;
        }
        return count;
    }

    @Override
    public int getCountOfConstantFields() {
        Field[] fields = clazz.getDeclaredFields();
        int count = 0;
        for (Field f : fields) {
            if (Modifier.isFinal(f.getModifiers()))
                count++;
        }
        return count;
    }

    @Override
    public boolean isExtending() {
        return (!getParentClassSimpleName().equals("Object"));
    }

    @Override
    public boolean isParentClassAbstract() {
        if (isExtending()) {
            Class<?> superClass = clazz.getSuperclass();
            return (Modifier.isAbstract(superClass.getModifiers()));

        } else
            return false;
    }

    @Override
    public Set<String> getNamesOfAllFieldsIncludingInheritanceChain() {
        Set<String> allFields = new HashSet<>();
        Class<?> currClass = this.clazz;

        while (currClass != null) {
            Field[] fields = currClass.getDeclaredFields();
            for (Field field : fields) {
                allFields.add(field.getName());
            }
            currClass = currClass.getSuperclass();
        }
        return allFields;
    }

    @Override
    public Set<String> getAllImplementedInterfaces() {
        Set<String> implementedInterfaces = new HashSet<>();

        Class<?>[] interfaces = clazz.getInterfaces();

        for (Class<?> inter : interfaces) {
            implementedInterfaces.add(inter.getSimpleName());
        }
        return implementedInterfaces;

    }

    @Override
    public String getInheritanceChain(String delimiter) {
        return getInheritanceChainHelper(clazz, delimiter);

    }

    private String getInheritanceChainHelper(Class<?> currClass, String delimiter) {
        if (currClass.getSimpleName().equals("Object")) {
            return "Object";
        } else {
            String inheritance = getInheritanceChainHelper(currClass.getSuperclass(), delimiter);
            inheritance += delimiter;
            inheritance += currClass.getSimpleName();
            return inheritance;
        }
    }

    @Override
    public int invokeMethodThatReturnsInt(String methodName, Object... args) {
        int res;
        try {
            Class<?>[] parameterTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                parameterTypes[i] = args[i].getClass();
            }
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            Object result = method.invoke(obj, args);

            if (result instanceof Integer) {
                res = (Integer) result;
            } else {
                throw new IllegalArgumentException("Method result is not of type Integer");
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Error during method invocation", e.getTargetException());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Method not found", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access to method", e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid arguments provided", e);
        }

        return res;
    }

    @Override
    public Object createInstance(int numberOfArgs, Object... args) {
        Class<?>[] parameterTypes = new Class<?>[numberOfArgs];
        for (int i = 0; i < numberOfArgs; i++) {
            parameterTypes[i] = getPrimitiveClass(args[i].getClass());
        }
        try {
            Constructor<?> constructor = clazz.getConstructor(parameterTypes);
            return constructor.newInstance(args);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Exception occurred while creating instance: " + e.getMessage(), e);
        }
    }

    @Override
    public Object elevateMethodAndInvoke(String name, Class<?>[] parametersTypes, Object... args) {
        try {
            Method method = clazz.getDeclaredMethod(name, parametersTypes);
            method.setAccessible(true);
            return method.invoke(obj, args);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking method", e);
        }
    }

    private Class<?> getPrimitiveClass(Class<?> clazz) {
        if (clazz == Integer.class) return int.class;
        if (clazz == Boolean.class) return boolean.class;
        if (clazz == Character.class) return char.class;
        if (clazz == Byte.class) return byte.class;
        if (clazz == Short.class) return short.class;
        if (clazz == Long.class) return long.class;
        if (clazz == Float.class) return float.class;
        if (clazz == Double.class) return double.class;
        return clazz;
    }
}
