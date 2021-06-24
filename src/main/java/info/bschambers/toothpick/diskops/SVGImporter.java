package info.bschambers.toothpick.diskops;

import info.bschambers.toothpick.TPProgram;
import info.bschambers.toothpick.actor.TPActor;
import info.bschambers.toothpick.actor.TPForm;
import info.bschambers.toothpick.actor.TPLink;
import info.bschambers.toothpick.geom.Node;
import java.io.*;
import java.util.List;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * <p>Imports SVG (Scalable Vector Graphics) files and converts them into toothpicks.</p>
 *
 * <p>Each group becomes an actor.</p>
 *
 * <p>SVG elements supported (all other elements are ignored):</p>
 * <ul>
 * <li>{@code line}</li>
 * <li>{@code rect}</li>
 * <li>{@code polyline}</li>
 * <li>{@code polygon}</li>
 * <li>{@code path} (partly supported)</li>
 * </ul>
 *
 * <p>TODO: attributes supported:</p>
 * <ul>
 * <li>{@code stroke} i.e. colour</li>
 * <li>{@code stroke-width}</li>
 * </ul>
 */
public class SVGImporter {

    public TPProgram importSVG(File f) {
        TPProgram prog = new TPProgram("imported from SVG file");
        try {
            Document doc = getDocument(f);
            Element docElem = doc.getDocumentElement();
            decodeSVG(docElem, prog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prog;
    }

    private static Document getDocument(File f)
	throws ParserConfigurationException, SAXException, IOException {
	DocumentBuilder builder =
	    DocumentBuilderFactory.newInstance().newDocumentBuilder();
	Document doc = builder.parse(f);
	return doc;
    }

    private void decodeSVG(Element svgElem, TPProgram prog) {
	if (svgElem.getNodeName().equals("svg")) {
            int groupNum = 0;
            int singleNum = 0;
            for (Element childElem : TPXml.getDirectChildElements(svgElem)) {

                if (childElem.getNodeName().equals("g")) {

                    // TOP-LEVEL GROUP

                    groupNum++;
                    for (Element groupChild : TPXml.getDirectChildElements(childElem)) {
                        String type = groupChild.getNodeName();
                        if (type.equals("g")) {
                            groupNum++;

                            // each group becomes an actor and gets added to the program
                            TPForm form = new TPForm();
                            decodeGroup(groupChild, form);
                            TPActor actor = new TPActor(form);
                            actor.updateForm();
                            actor.autosetCenter();
                            actor.name = "group " + groupNum;
                            prog.addActor(actor);
                            System.out.println("Added TPActor: " + actor.name);

                        } else {
                            singleNum++;

                            // in top-level group, each element also becomes an actor
                            TPForm form = new TPForm();

                            if (type.equals("line")) {
                                decodeLine(groupChild, form);
                            } else if (type.equals("rect")) {
                                decodeRect(groupChild, form);
                            } else if (type.equals("polyline")) {
                                decodePolyline(groupChild, form);
                            } else if (type.equals("polygon")) {
                                decodePolygon(groupChild, form);
                            } else if (type.equals("path")) {
                                decodePath(groupChild, form);
                            }

                            TPActor actor = new TPActor(form);
                            actor.updateForm();
                            actor.autosetCenter();
                            actor.name = "element " + singleNum;
                            prog.addActor(actor);
                            System.out.println("Added TPActor: " + actor.name);
                        }
                    }
                }
            }
        } else {
            System.out.println("ERROR! SVGImporter.decodeSVG() - element is '"
                               + svgElem.getNodeName() + "' - should be 'svg'.");
        }
    }

    private void decodeGroup(Element elem, TPForm form) {
        for (Element child : TPXml.getDirectChildElements(elem)) {
            String type = child.getNodeName();
            if (type.equals("g")) {
                decodeGroup(child, form);
            } else if (type.equals("line")) {
                decodeLine(child, form);
            } else if (type.equals("rect")) {
                decodeRect(child, form);
            } else if (type.equals("polyline")) {
                decodePolyline(child, form);
            } else if (type.equals("polygon")) {
                decodePolygon(child, form);
            } else if (type.equals("path")) {
                decodePath(child, form);
            }
        }
    }

    private void decodeLine(Element elem, TPForm form) {
	try {
	    String x1Str = TPXml.getStringAttr(elem, "x1", TPXml.DEFAULT_STR);
	    String y1Str = TPXml.getStringAttr(elem, "y1", TPXml.DEFAULT_STR);
	    String x2Str = TPXml.getStringAttr(elem, "x2", TPXml.DEFAULT_STR);
	    String y2Str = TPXml.getStringAttr(elem, "y2", TPXml.DEFAULT_STR);
            double x1 = Double.parseDouble(x1Str);
            double y1 = Double.parseDouble(y1Str);
            double x2 = Double.parseDouble(x2Str);
            double y2 = Double.parseDouble(y2Str);
            System.out.println("LINE:...");
            form.addLinkReuseNodes(x1, y1, x2, y2);
            form.housekeeping();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void decodeRect(Element elem, TPForm form) {
	try {
	    String xStr = TPXml.getStringAttr(elem, "x", TPXml.DEFAULT_STR);
	    String yStr = TPXml.getStringAttr(elem, "y", TPXml.DEFAULT_STR);
	    String wStr = TPXml.getStringAttr(elem, "width", TPXml.DEFAULT_STR);
	    String hStr = TPXml.getStringAttr(elem, "height", TPXml.DEFAULT_STR);
            double x = Double.parseDouble(xStr);
            double y = Double.parseDouble(yStr);
            double w = Double.parseDouble(wStr);
            double h = Double.parseDouble(hStr);
            System.out.println("RECT...");
            form.addLinkReuseNodes(x, y, x + w, y);
            form.addLinkReuseNodes(x + w, y, x + w, y + h);
            form.addLinkReuseNodes(x + w, y + h, x, y + h);
            form.addLinkReuseNodes(x, y + h, x, y);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void decodePolyline(Element elem, TPForm form) {
        System.out.println("POLYLINE: start");
        decodeLinePoints(elem, form, false);
    }

    private void decodePolygon(Element elem, TPForm form) {
        System.out.println("POLYGON: start");
        decodeLinePoints(elem, form, true);
    }

    private void decodeLinePoints(Element elem, TPForm form, boolean closedShape) {
        int numPoints = 0;
	try {
	    String pointsStr = TPXml.getStringAttr(elem, "points", TPXml.DEFAULT_STR);
            System.out.println(pointsStr);
            pointsStr = pointsStr.replaceAll(",", " ");
            System.out.println(pointsStr);

            int i = 1;
            double firstX = 0;
            double firstY = 0;
            double x1 = 0;
            double y1 = 0;
            double x2 = 0;
            double y2 = 0;
            for (String str : pointsStr.split("\\s+")) {
                numPoints++;
                if (i == 1) {
                    firstX = Double.parseDouble(str);
                    x1 = firstX;
                    i++;
                } else if (i == 2) {
                    firstY = Double.parseDouble(str);
                    y1 = firstY;
                    i++;
                } else if (i == 3) {
                    x2 = Double.parseDouble(str);
                    i++;
                } else if (i == 4) {
                    y2 = Double.parseDouble(str);
                    form.addLinkReuseNodes(x1, y1, x2, y2);
                    x1 = x2;
                    y1 = y2;
                    i = 3;
                }
            }

            if (closedShape) {
                System.out.println("... closing shape...");
                form.addLinkReuseNodes(x1, y1, firstX, firstY);
            }
	} catch (Exception e) {
	    e.printStackTrace();
	}
        System.out.println("... finished (" + numPoints + " points)");
    }

    private void decodePath(Element elem, TPForm form) {
        System.out.println("PATH: start");
        PathParser pp = new PathParser(elem, form);
        if (pp.ok()) {
            pp.decode();
        } else {
            System.out.println("... PATH: error in init!");
        }
    }

    private static class PathParser {

        private enum Mode {
            NULL, MOVE, LINE, HORIZONTAL, VERTICAL, CLOSE,
            CUBIC, QUADRATIC
        }

        private Mode m = Mode.NULL;
        private boolean absolute = false;
        private double lastDouble = 0;
        private boolean dataInput = false;
        private String[] tokens;
        private String currentToken = "";
        private int i = 0;
        private double a = 0;
        private double b = 0;
        private double x = 0;
        private double y = 0;
        private double firstX = 0;
        private double firstY = 0;
        private boolean initX = false;
        private boolean initY = false;
        private int inputCount = 0;
        private Element elem;
        private TPForm form;
        private boolean initOK;

        public PathParser(Element elem, TPForm form) {
            this.elem = elem;
            this.form = form;

            String dataStr = TPXml.getStringAttr(elem, "d", TPXml.DEFAULT_STR);
            System.out.println(dataStr);
            dataStr = dataStr.replaceAll(",", " ");
            System.out.println(dataStr);
            tokens = dataStr.split("\\s+");

            initOK = true;
        }

        public boolean ok() { return initOK; }

        public void decode() {

            while(i < tokens.length) {

                if (m == Mode.NULL) {
                    parseToken();
                } else if (m == Mode.MOVE) {
                    if (inputCount == 0)
                        fetchInputA();
                    if (inputCount == 1)
                        fetchInputB();
                    if (inputCount == 2) {
                        x = a;
                        y = b;
                        System.out.println("... MOVE to " + x + ", " + y);
                        inputCount = 0;
                    }
                } else if (m == Mode.LINE) {
                    if (inputCount == 0)
                        fetchInputA();
                    if (inputCount == 1)
                        fetchInputB();
                    if (inputCount == 2) {
                        System.out.println("... LINE from " + x + ", " + y + " to " + a + ", " + b);
                        // TPLine line = new TPLine(x, y, a, b);
                        // form.addPart(line);
                        form.addLinkReuseNodes(x, y, a, b);
                        x = a;
                        y = b;
                        inputCount = 0;
                    }
                } else if (m == Mode.HORIZONTAL) {
                    if (inputCount == 0)
                        fetchInputA();
                    if (inputCount == 1) {
                        System.out.println("... HORIZONTAL LINE from "
                                           + x + ", " + y + " to " + a + ", " + y);
                        // TPLine line = new TPLine(x, y, a, y);
                        // form.addPart(line);
                        form.addLinkReuseNodes(x, y, a, y);
                        x = a;
                        inputCount = 0;
                    }
                } else if (m == Mode.VERTICAL) {
                    if (inputCount == 0)
                        fetchInputB();
                    if (inputCount == 1) {
                        System.out.println("... VERTICAL LINE from "
                                           + x + ", " + y + " to " + x + ", " + b);
                        // TPLine line = new TPLine(x, y, x, b);
                        // form.addPart(line);
                        form.addLinkReuseNodes(x, y, x, b);
                        y = b;
                        inputCount = 0;
                    }
                } else {
                    System.out.println("... MODE NOT HANDLED: " + m);
                    parseToken();
                }
            }
        }

        private void closePath() {
            System.out.println("... CLOSE PATH (line from "
                               + x + ", " + y + " to "
                               + firstX + ", " + firstY + ")");
            // TPLine line = new TPLine(x, y, firstX, firstY);
            // form.addPart(line);
            form.addLinkReuseNodes(x, y, firstX, firstY);
        }

        private void fetchInputA() {
            dataInput = true;
            parseToken();
            // if number was not obtained, we don't want to update value of 'b'
            if (dataInput) {
                if (absolute) {
                    a = lastDouble;
                } else {
                    a = x + lastDouble;
                }
                if (!initX) {
                    firstX = a;
                    initX = true;
                }
                dataInput = false;
            }
        }

        private void fetchInputB() {
            dataInput = true;
            parseToken();
            // if number was not obtained, we don't want to update value of 'a'
            if (dataInput) {
                if (absolute) {
                    b = lastDouble;
                } else {
                    b = y + lastDouble;
                }
                if (!initY) {
                    firstY = b;
                    initY = true;
                }
                dataInput = false;
            }
        }

        private void parseToken() {
            currentToken = tokens[i++];

            if (dataInput) {
                try {
                    lastDouble = Double.parseDouble(currentToken);
                    inputCount++;
                    return;
                } catch (NumberFormatException e) {
                    System.out.println("... token '" + currentToken + "' is not a number!");
                    dataInput = false;
                }
            }

            if (!dataInput) {

                inputCount = 0;

                if (currentToken.equals("M")) {
                    m = Mode.MOVE;
                    absolute = true;
                } else if (currentToken.equals("m")) {
                    m = Mode.MOVE;
                    absolute = false;
                } else if (currentToken.equals("L")) {
                    m = Mode.LINE;
                    absolute = true;
                } else if (currentToken.equals("l")) {
                    m = Mode.LINE;
                    absolute = false;
                } else if (currentToken.equals("H")) {
                    m = Mode.HORIZONTAL;
                    absolute = true;
                } else if (currentToken.equals("h")) {
                    m = Mode.HORIZONTAL;
                    absolute = false;
                } else if (currentToken.equals("V")) {
                    m = Mode.VERTICAL;
                    absolute = true;
                } else if (currentToken.equals("v")) {
                    m = Mode.VERTICAL;
                    absolute = false;
                } else if (currentToken.equalsIgnoreCase("z")) {
                    closePath();
                } else {
                    m = Mode.NULL;
                }
            }
        }
    }

}
