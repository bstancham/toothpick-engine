package info.bschambers.toothpick;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TPEncoding implements Iterable<TPEncoding.Param> {

    public enum ParamType { FIELD, METHOD, LIST_METHOD }

    private List<Param> params = new ArrayList<>();

    public void addField(Class targetClass, Object val, String fieldName) {
        List values = new ArrayList();
        values.add(val);
        params.add(new TPEncoding.Param(ParamType.FIELD, targetClass, values, fieldName));
    }

    public void addMethod(Class targetClass, Object val, String setMethodName) {
        List values = new ArrayList();
        values.add(val);
        params.add(new TPEncoding.Param(ParamType.METHOD, targetClass, values, setMethodName));
    }

    public void addListMethod(Class targetClass, List values, String setMethodName) {
        params.add(new TPEncoding.Param(ParamType.LIST_METHOD, targetClass, values, setMethodName));
    }

    @Override
    public Iterator<Param> iterator() {
        return params.iterator();
    }

    public class Param {

        private ParamType t;
        private Class targetClass;
        private List values;
        private String setMethodName;

        public Param(ParamType t, Class targetClass, List values, String setMethodName) {
            this.t = t;
            this.targetClass = targetClass;
            this.values = values;
            this.setMethodName = setMethodName;
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

        public String getSetMethodName() {
            return setMethodName;
        }
    }

}
