package info.bschambers.toothpick.diskops;

import info.bschambers.toothpick.TPEncoding;
import info.bschambers.toothpick.TPEncodingHelper;
import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.geom.Line;
import info.bschambers.toothpick.geom.Pt;
import info.bschambers.toothpick.geom.Rect;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * <p>Load and save objects as xml files.</p>
 */
public class TPXml {

    public static final String DTD_PATH = "toothpick-program.dtd";
    private static final String DEFAULT_STR = "!";

    private class UnsupportedException extends RuntimeException {
	public UnsupportedException(Object obj) {
	    super("Type '" + obj.getClass().getName() +
		  "' is not supported in this context!");
	}
    }

    private List<String> errors = new ArrayList<>();

    public void addErrorMsg(String msg) {
        errors.add(msg);
    }

    public List<String> getErrorMessages() {
        return errors;
    }

    /**
     * @return The loaded program, or null if the operation failed.
     */
    public TPProgram load(File f) {
        try {
            Document doc = getDocument(f);
            Element progElem = doc.getDocumentElement();
            Object obj = decodeObject(progElem);
            if (obj instanceof TPProgram) {
                return (TPProgram) obj;
            } else {
                addErrorMsg("loaded object is not TPProgram type: " + obj.getClass());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>Attempts to save program as xml in the specified file.</p>
     *
     * @return True, if operation was successful, false otherwise.
     */
    public boolean save(TPProgram prog, File f) {
        boolean success = false;
        try {
     	    Document doc = newDocument();
            Element progElement = encodeObject(doc, prog);
            if (progElement == null) {
                addErrorMsg("progElement is null");
            } else {
                doc.appendChild(progElement);
                writeFile(doc, f, DTD_PATH);
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    private void writeFile(Document doc, File f, String dtd) {
	System.out.println("TPXml.writeFile() ...");
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(doc);
            // Prepare the output file
            Result result = new StreamResult(f);
	    Transformer trans = TransformerFactory.newInstance().newTransformer();
	    // adding a doctype
	    trans.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtd);
	    // make the output file more human-readable
	    trans.setOutputProperty(OutputKeys.INDENT, "yes");
            // Write the DOM to the file
            trans.transform(source, result);
        } catch (TransformerConfigurationException e) {
	    e.printStackTrace();
        } catch (TransformerException e) {
	    e.printStackTrace();
        }
	System.out.println("... done");
    }

    /*-------------------------- XML Encoding --------------------------*/

    /** Creates a new XML Document. */
    private Document newDocument() throws ParserConfigurationException {
	return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    /**
     * <p>Encodes an object as an xml element. Handles all Java primitive-wrapper classes
     * automatically. Any other class needs to implement {@link TPEncodingHelper} in order
     * to be encoded.</p>
     */
    private Element encodeObject(Document doc, Object obj) {
        Class cls = obj.getClass();
        // primitives
        if (cls == Boolean.class)
            return makePrimElement(doc, "boolean", obj.toString());
        if (cls == Byte.class)
            return makePrimElement(doc, "byte", obj.toString());
        if (cls == Character.class)
            return makePrimElement(doc, "char", obj.toString());
        if (cls == Double.class)
            return makePrimElement(doc, "double", obj.toString());
        if (cls == Float.class)
            return makePrimElement(doc, "float", obj.toString());
        if (cls == Integer.class)
            return makePrimElement(doc, "int", obj.toString());
        if (cls == Long.class)
            return makePrimElement(doc, "long", obj.toString());
        if (cls == Short.class)
            return makePrimElement(doc, "short", obj.toString());
        if (cls == String.class)
            return makePrimElement(doc, "string", obj.toString());
        // pseudo-primitives
        if (cls == Color.class)
            return makeColorElement(doc, (Color) obj);
        if (cls == Pt.class)
            return makePointElement(doc, (Pt) obj);
        if (cls == Line.class)
            return makeLineElement(doc, (Line) obj);
        if (cls == Rect.class)
            return makeRectElement(doc, (Rect) obj);
        // object instance
        Element elem = doc.createElement("instance");
        elem.setAttribute("class", obj.getClass().getName());
        if (obj instanceof TPEncodingHelper) {
            for (TPEncoding.Param param : ((TPEncodingHelper) obj).getEncoding()) {
                addParam(doc, elem, param);
            }
        } else {
            addErrorMsg("Object is not a TPEncodingHelper: " + obj.getClass());
        }
        return elem;
    }

    /**
     * Transforms {@code param} into an XML element and adds to {@code parent} as a child
     * element.
     */
    private void addParam(Document doc, Element parent, TPEncoding.Param param) {
        if (param.getParamType() == TPEncoding.ParamType.FIELD)
            addFieldParam(doc, parent, param.getTargetClass(), param.getValue(), param.getMethodName());
        else if (param.getParamType() == TPEncoding.ParamType.METHOD)
            addMethodParam(doc, parent, param.getTargetClass(), param.getValue(), param.getMethodName());
        else if (param.getParamType() == TPEncoding.ParamType.LIST_METHOD)
            addListMethodParam(doc, parent, param.getTargetClass(), param.getValueList(), param.getMethodName());
        else if (param.getParamType() == TPEncoding.ParamType.VOID_METHOD)
            addVoidMethodParam(doc, parent, param.getMethodName());
        else
            addErrorMsg("param-type not recognised: " + param.getParamType());
    }

    private void addFieldParam(Document doc, Element parent, Class targetClass,
                               Object val, String fieldName) {
        Element fieldElem = makeFieldElement(doc, fieldName, targetClass);
        fieldElem.appendChild(encodeObject(doc, val));
        parent.appendChild(fieldElem);
    }

    private void addMethodParam(Document doc, Element parent, Class targetClass,
                                Object val, String methodName) {
        Element methodElem = makeMethodElement(doc, methodName, targetClass);
        methodElem.appendChild(encodeObject(doc, val));
        parent.appendChild(methodElem);
    }

    private void addListMethodParam(Document doc, Element parent, Class targetClass,
                                    List<Object> values, String methodName) {
        Element methodElem = makeListMethodElement(doc, methodName, targetClass);
        for (Object obj : values)
            methodElem.appendChild(encodeObject(doc, obj));
        parent.appendChild(methodElem);
    }

    private void addVoidMethodParam(Document doc, Element parent, String methodName) {
        Element elem = doc.createElement("void-method");
        elem.setAttribute("name", methodName);
        parent.appendChild(elem);
    }

    public Element makeFieldElement(Document doc, String fieldName, Class argClass) {
        Element elem = doc.createElement("field");
        elem.setAttribute("name", fieldName);
        elem.setAttribute("class", argClass.getName());
        return elem;
    }

    public Element makeMethodElement(Document doc, String methodName, Class argClass) {
        Element elem = doc.createElement("method");
        elem.setAttribute("name", methodName);
        elem.setAttribute("class", argClass.getName());
        return elem;
    }

    public static Element makeListMethodElement(Document doc, String methodName, Class argClass) {
        Element elem = doc.createElement("list-method");
        elem.setAttribute("name", methodName);
        elem.setAttribute("class", argClass.getName());
        return elem;
    }

    private Element makePrimElement(Document doc, String type, String val) {
        Element elem = doc.createElement("prim");
        elem.setAttribute("type", type);
        elem.setAttribute("value", val);
        return elem;
    }

    private Element makeColorElement(Document doc, Color val) {
        Element elem = doc.createElement("color");
        elem.setAttribute("r", "" + val.getRed());
        elem.setAttribute("g", "" + val.getGreen());
        elem.setAttribute("b", "" + val.getBlue());
        return elem;
    }

    private Element makePointElement(Document doc, Pt val) {
        Element elem = doc.createElement("point");
        elem.setAttribute("x", "" + val.x);
        elem.setAttribute("y", "" + val.y);
        return elem;
    }

    private Element makeLineElement(Document doc, Line val) {
        Element elem = doc.createElement("line");
        elem.setAttribute("x1", "" + val.start.x);
        elem.setAttribute("y1", "" + val.start.y);
        elem.setAttribute("x2", "" + val.end.x);
        elem.setAttribute("y2", "" + val.end.y);
        return elem;
    }

    private Element makeRectElement(Document doc, Rect val) {
        Element elem = doc.createElement("rect");
        elem.setAttribute("x1", "" + val.x1);
        elem.setAttribute("y1", "" + val.y1);
        elem.setAttribute("x2", "" + val.x2);
        elem.setAttribute("y2", "" + val.y2);
        return elem;
    }

    /*-------------------------- XML Decoding --------------------------*/

    private static Document getDocument(File f)
	throws ParserConfigurationException, SAXException, IOException {
	DocumentBuilder builder =
	    DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = builder.parse(f);
	return doc;
    }

    private Object decodeObject(Element elem) {
	String type = elem.getNodeName();
	if (type.equals("instance")) {
	    return decodeInstance(elem);
	} else if (type.equals("prim")) {
	    return decodePrim(elem);
	} else if (type.equals("color")) {
	    return decodeColor(elem);
	} else if (type.equals("point")) {
	    return decodePoint(elem);
	} else if (type.equals("line")) {
	    return decodeLine(elem);
	} else if (type.equals("rect")) {
	    return decodeRect(elem);
	} else {
	    throw new UnsupportedException(type);
	}
    }

    /**
     * <p>Attempts to decode {@code elem} and create an object instance from it.</p>

     * <p>NOTE: the element must specify a class which has a no-args constructor</p>
     *
     * @param elem Must be an 'instance' element as defined in
     * {@code toothpick-program.dtd} which specifies a class with a no-args constructor.
     * @return A new object instance, or {@code null} if the process failed.
     */
    public Object decodeInstance(Element elem) {
	if (!isElemType(elem, "instance")) {
            addErrorMsg("decodeInstance(): wanted element type 'instance', not " +
                        elem.getNodeName());
            return null;
        }

	String classStr = DEFAULT_STR;
	try {
	    // instantiate object
	    classStr = elem.getAttribute("class");
	    Class elemClass = Class.forName(classStr);
	    Object obj = elemClass.newInstance();
            // setup object
            List<Element> children = getDirectChildElements(elem);
            for (Element childElem : children) {
                String type = childElem.getNodeName();
                if (type.equals("field")) {
                    applyField(obj, childElem);
                } else if (type.equals("method")) {
                    applyMethod(obj, childElem);
                } else if (type.equals("list-method")) {
                    applyMethod(obj, childElem);
                } else if (type.equals("void-method")) {
                    evalVoidMethod(obj, childElem);
                } else {
                    throw new UnsupportedException(type);
                }
            }
	    return obj;
	} catch (Exception e) {
	    e.printStackTrace();
            addErrorMsg("decodeInstance(): caught error - " + e.getClass()
                        + " (classStr=" + classStr + ")");
	}
        return null;
    }

    private Object decodePrim(Element elem) {
	try {
	    String type = getStringAttr(elem, "type", DEFAULT_STR);
	    String val = getStringAttr(elem, "value", DEFAULT_STR);
	    Class primClass = stringToClass(type);
	    return parsePrim(primClass, val);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	throw new UnsupportedException(elem.getNodeName());
    }

    private Object parsePrim(Class cls, String val) {
	if (cls == Byte.TYPE || cls == Byte.class) {
	    return Byte.parseByte(val);
	} else if (cls == Boolean.TYPE || cls == Boolean.class) {
	    return Boolean.parseBoolean(val);
	} else if (cls == Character.TYPE || cls == Character.class) {
	    return val.charAt(0); // first char of the string
	} else if (cls == Double.TYPE || cls == Double.class) {
	    return Double.parseDouble(val);
	} else if (cls == Float.TYPE || cls == Float.class) {
	    return Float.parseFloat(val);
	} else if (cls == Integer.TYPE || cls == Integer.class) {
	    return Integer.parseInt(val);
	} else if (cls == Long.TYPE || cls == Long.class) {
	    return Long.parseLong(val);
	} else if (cls == Short.TYPE || cls == Short.class) {
	    return Short.parseShort(val);
	} else if (cls == String.class) {
	    return val;
	}
	return null;
    }

    private Color decodeColor(Element elem) {
	try {
	    String rStr = getStringAttr(elem, "r", DEFAULT_STR);
	    String gStr = getStringAttr(elem, "g", DEFAULT_STR);
	    String bStr = getStringAttr(elem, "b", DEFAULT_STR);
            int r = Integer.parseInt(rStr);
            int g = Integer.parseInt(gStr);
            int b = Integer.parseInt(bStr);
            return new Color(r, g, b);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	throw new UnsupportedException(elem.getNodeName());
    }

    private Pt decodePoint(Element elem) {
	try {
	    String xStr = getStringAttr(elem, "x", DEFAULT_STR);
	    String yStr = getStringAttr(elem, "y", DEFAULT_STR);
            double x = Double.parseDouble(xStr);
            double y = Double.parseDouble(yStr);
            return new Pt(x, y);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	throw new UnsupportedException(elem.getNodeName());
    }

    private Line decodeLine(Element elem) {
	try {
	    String x1Str = getStringAttr(elem, "x1", DEFAULT_STR);
	    String y1Str = getStringAttr(elem, "y1", DEFAULT_STR);
	    String x2Str = getStringAttr(elem, "x2", DEFAULT_STR);
	    String y2Str = getStringAttr(elem, "y2", DEFAULT_STR);
            double x1 = Double.parseDouble(x1Str);
            double y1 = Double.parseDouble(y1Str);
            double x2 = Double.parseDouble(x2Str);
            double y2 = Double.parseDouble(y2Str);
            return new Line(new Pt(x1, y1), new Pt(x2, y2));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	throw new UnsupportedException(elem.getNodeName());
    }

    private Rect decodeRect(Element elem) {
	try {
	    String x1Str = getStringAttr(elem, "x1", DEFAULT_STR);
	    String y1Str = getStringAttr(elem, "y1", DEFAULT_STR);
	    String x2Str = getStringAttr(elem, "x2", DEFAULT_STR);
	    String y2Str = getStringAttr(elem, "y2", DEFAULT_STR);
            int x1 = Integer.parseInt(x1Str);
            int y1 = Integer.parseInt(y1Str);
            int x2 = Integer.parseInt(x2Str);
            int y2 = Integer.parseInt(y2Str);
            return new Rect(x1, y1, x2, y2);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	throw new UnsupportedException(elem.getNodeName());
    }

    private void applyField(Object obj, Element fElem) {
	String fieldName = getStringAttr(fElem, "name", DEFAULT_STR);
	List<Element> arguments = getDirectChildElements(fElem);
	for (Element argElem : arguments) {
	    String argType = argElem.getNodeName();
	    if (argType.equals("instance")) {
		String argClass = getStringAttr(fElem, "class", "!");
		applyInstField(obj, fieldName, argElem, argClass);
	    // } else if (argType.equals("array")) {
	    //     applyArrayMethod(obj, methodName, argElem);
	    } else if (argType.equals("prim")) {
		applyPrimField(obj, fieldName, argElem);
	    // } else if (argType.equals("pt2")) {
	    //     applyPt2Method(obj, methodName, argElem);
	    // } else if (argType.equals("ln2")) {
	    //     applyLn2Method(obj, methodName, argElem);
	    // } else if (argType.equals("rgb")) {
	    //     applyRGBMethod(obj, methodName, argElem);
	    } else {
		throw new UnsupportedException(argType);
	    }
	}
    }

    private void applyInstField(Object obj, String fieldName, Element instElem, String argClassName) {
	try {
	    Object arg = decodeInstance(instElem);
	    Field f = obj.getClass().getField(fieldName);
	    f.set(obj, arg);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void applyPrimField(Object obj, String fieldName, Element elem) {
	Object prim = decodePrim(elem);
	String type = getStringAttr(elem, "type", "!");
	applyField(obj, fieldName, prim);
    }

    private void applyField(Object obj, String fieldName, Object arg) {
	try {
	    Field f = obj.getClass().getField(fieldName);
	    f.set(obj, arg);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void applyMethod(Object obj, Element mElem) {
	String methodName = getStringAttr(mElem, "name", DEFAULT_STR);
	if (methodName.equals(DEFAULT_STR))
	    throw new RuntimeException("!!! 'name' ATTRIBUTE NOT FOUND !!!");
	// get argument element(s)
	List<Element> arguments = getDirectChildElements(mElem);
	for (Element argElem : arguments) {
	    String argType = argElem.getNodeName();
	    if (argType.equals("instance")) {
		String argClass = getStringAttr(mElem, "class", "!");
		applyInstMethod(obj, methodName, argElem, argClass);
	    } else if (argType.equals("prim")) {
		applyPrimMethod(obj, methodName, argElem);
	    } else if (argType.equals("color")) {
		applyColorMethod(obj, methodName, argElem);
	    } else if (argType.equals("point")) {
		applyPointMethod(obj, methodName, argElem);
	    } else if (argType.equals("line")) {
		applyLineMethod(obj, methodName, argElem);
	    } else if (argType.equals("rect")) {
		applyRectMethod(obj, methodName, argElem);
	    } else {
		throw new UnsupportedException(argType);
	    }
	}
    }

    private void applyInstMethod(Object obj, String methodName,
                                 Element instElem, String argClassName) {
	try {
	    Class argClass = stringToClass(argClassName);
	    Object arg = decodeInstance(instElem);
	    Method m = obj.getClass().getMethod(methodName, argClass);
	    m.invoke(obj, arg);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void applyPrimMethod(Object obj, String methodName, Element elem) {
	Object prim = decodePrim(elem);
	String type = getStringAttr(elem, "type", "!");
	applyMethod(obj, methodName, stringToClass(type), prim);
    }

    private void applyColorMethod(Object obj, String methodName, Element elem) {
        Color c = decodeColor(elem);
	applyMethod(obj, methodName, Color.class, c);
    }

    private void applyPointMethod(Object obj, String methodName, Element elem) {
        Pt p = decodePoint(elem);
	applyMethod(obj, methodName, Pt.class, p);
    }

    private void applyLineMethod(Object obj, String methodName, Element elem) {
        Line ln = decodeLine(elem);
	applyMethod(obj, methodName, Line.class, ln);
    }

    private void applyRectMethod(Object obj, String methodName, Element elem) {
        Rect r = decodeRect(elem);
	applyMethod(obj, methodName, Rect.class, r);
    }

    private void evalVoidMethod(Object obj, Element mElem) {
	String methodName = getStringAttr(mElem, "name", DEFAULT_STR);
	if (methodName.equals(DEFAULT_STR))
	    throw new RuntimeException("!!! 'name' ATTRIBUTE NOT FOUND !!!");

	try {
	    Method m = obj.getClass().getMethod(methodName);
	    m.invoke(obj);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public String getStringAttr(Element elmt, String attr, String defaultVal) {
	if (elmt.hasAttribute(attr)) {
	    return elmt.getAttribute(attr);
	}
	System.out.println("!!! couldn't retrieve attribute '" + attr +
			   "' from element! ---> Returning default value of '" +
			   defaultVal + "' !!!");
	return defaultVal;
    }

    private Class stringToClass(String name) {
	// primitive types...
	if (name.equals("byte")) {
	    return Byte.TYPE;
	} else if (name.equals("boolean")) {
	    return Boolean.TYPE;
	} else if (name.equals("char")) {
	    return Character.TYPE;
	} else if (name.equals("double")) {
	    return Double.TYPE;
	} else if (name.equals("float")) {
	    return Float.TYPE;
	} else if (name.equals("int")) {
	    return Integer.TYPE;
	} else if (name.equals("long")) {
	    return Long.TYPE;
	} else if (name.equals("short")) {
	    return Short.TYPE;
	} else if (name.equals("string")) {
	    return String.class;
	}
	// else
	try {
	    return Class.forName(name);
	} catch (Exception e) {
	    e.printStackTrace();
	}
	throw new UnsupportedException(name);
    }

    public boolean isElemType(Element elem, String type) {
	return elem.getNodeName().equals(type);
    }

    private List<Element> getDirectChildElements(Element parent) {
	List<Element> directChildren = new ArrayList<Element>();
	NodeList nl = parent.getChildNodes();
	for (int n = 0; n < nl.getLength(); ++n)
	    if (nl.item(n).getNodeType() == Node.ELEMENT_NODE &&
                isDirectChild((Element) nl.item(n), parent))
		directChildren.add((Element) nl.item(n));
	return directChildren;
    }

    /**
     * @return True, if 'child' is a direct child node of 'parent'.
     */
    public boolean isDirectChild(Node child, Node parent) {
	Node trueParent = child.getParentNode();
	if (parent == trueParent) return true;
	return false;
    }

    public void applyMethod(Object obj, String methodName,
                            Class argClass, Object arg) {
	try {
	    Method m = obj.getClass().getMethod(methodName, argClass);
	    m.invoke(obj, arg);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
