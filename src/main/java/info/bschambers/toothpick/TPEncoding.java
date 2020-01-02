package info.bschambers.toothpick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TPEncoding implements Iterable<TPEncoding.Param> {

    public enum ParamType { FIELD, METHOD, LIST_METHOD, VOID_METHOD }

    private List<Param> params = new ArrayList<>();

    public void addField(Class targetClass, Object val, String fieldName) {
        List values = new ArrayList();
        values.add(val);
        params.add(new TPEncoding.Param(ParamType.FIELD, targetClass, values, fieldName));
    }

    public void addMethod(Class targetClass, Object val, String methodName) {
        List values = new ArrayList();
        values.add(val);
        params.add(new TPEncoding.Param(ParamType.METHOD, targetClass, values, methodName));
    }

    public void addListMethod(Class targetClass, List values, String methodName) {
        params.add(new TPEncoding.Param(ParamType.LIST_METHOD, targetClass, values, methodName));
    }

    /**
     * Add instruction to call a setup method with no arguments and void return type.
     */
    public void addVoidMethod(String methodName) {
        params.add(new TPEncoding.Param(ParamType.VOID_METHOD, null, null, methodName));
    }

    @Override
    public Iterator<Param> iterator() {
        return params.iterator();
    }

    public class Param {

        private ParamType t;
        private Class targetClass;
        private List values;
        private String methodName;

        public Param(ParamType t, Class targetClass, List values, String methodName) {
            this.t = t;
            this.targetClass = targetClass;
            this.values = values;
            this.methodName = methodName;
        }

        public ParamType getParamType() {
            return t;
        }

        public Class getTargetClass() {
            return targetClass;
        }

        public List getValueList() {
            return values;
        }

        public Object getValue() {
            return values.get(0);
        }

        public String getMethodName() {
            return methodName;
        }
    }

}
