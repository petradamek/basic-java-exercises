package cz.bilysklep.trainings.basicjava.functions.gui;

import cz.bilysklep.trainings.basicjava.functions.UnaryFunction;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import javax.swing.JComponent;


public class UnaryFunctionPane extends JComponent {
    
    
    /* ---------------------------------------------------------------------- */
    /* Properties                                                             */
    /* ---------------------------------------------------------------------- */
    private UnaryFunction function;
    private Rectangle2D viewPort;
    private int segmentsCount = 100;
    
    public UnaryFunction getFunction() {
        return function;
    }
    
    public void setFunction(UnaryFunction function) {
        firePropertyChange("function",this.function,function);
        this.function = function;
        resetShapes();
        repaint();
    }
    
    public Rectangle2D getViewPort() {
        return viewPort;
    }
    
    public void setViewPort(Rectangle2D viewPort) {
        firePropertyChange("viewPort",this.viewPort,viewPort);
        this.viewPort = viewPort;
        resetShapes();
        repaint();
    }
    
    public int getSegmentsCount() {
        return segmentsCount;
    }
    
    public void setSegmentsCount(int segmentsCount) {
        firePropertyChange("segmentsCount",this.segmentsCount,segmentsCount);
        this.segmentsCount = segmentsCount;
        resetShapes();
        repaint();
    }
    /* ---------------------------------------------------------------------- */
    /* Axes                                                                   */
    /* ---------------------------------------------------------------------- */
    private static class AxisDescriptor {
        
        private final double delta;
        private final double firstLinePosition;
        private final int linesCount;
        
        public AxisDescriptor(double minValue, double maxValue) {
            delta = findSuitableDelta((maxValue - minValue) / 10);
            
            firstLinePosition = Math.ceil(minValue/delta) * delta;
            linesCount = (int) Math.round(Math.ceil(0.001 +
                    (maxValue - firstLinePosition) / delta));
        }
        
        public int getLinesCount() {
            return linesCount;
        }
        
        public double getLinePosition(int index) {
            return firstLinePosition + (delta * index);
        }
        
        public double getDelta() {
            return delta;
        }
        
        private static final double[] SUITABLE_DELTAS = {
            0.01, 0.02, 0.05,
            0.1, 0.2, 0.5,
            1, 2, 5,
            10, 20, 50
        };
        
        private double findSuitableDelta(double proposedDelta) {
            double lastDelta = SUITABLE_DELTAS[0];
            for(double d : SUITABLE_DELTAS) {
                if (d > proposedDelta) {
                    return lastDelta;
                } else {
                    lastDelta = d;
                }
            }
            return lastDelta;
        }
    }
    
    private static class AxisGridPathIterator implements PathIterator {
        
        private final AxisDescriptor axisDescriptor;
        private final double minY;
        private final double maxY;
        private final int x;
        private final int y;
        private int index = 0;
        
        private AxisGridPathIterator(boolean xAxis,
                Rectangle2D viewPort, AxisDescriptor axisDescriptor) {
            this.axisDescriptor = axisDescriptor;
            if (xAxis) {
                x = 0;
                y = 1;
                minY = viewPort.getMinY();
                maxY = viewPort.getMaxY();
            } else {
                x = 1;
                y = 0;
                minY = viewPort.getMinX();
                maxY = viewPort.getMaxX();
            }
        }
        
        @Override
        public int currentSegment(double[] coords) {
            int nextMove;
            coords[x] = axisDescriptor.getLinePosition(index / 2);
            if ((index % 2) == 0) {
                coords[y] = minY;
                nextMove = SEG_MOVETO;
            } else {
                coords[y] = maxY;
                nextMove = SEG_LINETO;
            }
            return nextMove;
        }
        
        @Override
        public int currentSegment(float[] coords) {
            double[] buffer = new double[2];
            int result = currentSegment(buffer);
            coords[0] = (float) buffer[0];
            coords[1] = (float) buffer[1];
            return result;
        }
        
        @Override
        public void next() {
            index ++;
        }
        
        @Override
        public boolean isDone() {
            return index > (axisDescriptor.getLinesCount() * 2);
        }
        
        @Override
        public int getWindingRule() {
            return WIND_NON_ZERO;
        }
    }
    
    /* ---------------------------------------------------------------------- */
    /* Shapes                                                                 */
    /* ---------------------------------------------------------------------- */
    private GeneralPath functionShape;
    private GeneralPath axesShape;
    private GeneralPath axesMarksShape;
    private AxisDescriptor xAxis;
    private AxisDescriptor yAxis;
    
    
    private void resetShapes() {
        
        if (function == null || viewPort == null) {
            return;
        }
        
        functionShape = new GeneralPath(new UnaryFunctionShape(function,
                viewPort.getMinX(),viewPort.getMaxX(),viewPort.getWidth() / segmentsCount));
        
        axesShape = new GeneralPath();
        Shape xAxisLine = new Line2D.Double(viewPort.getMinX(),0,viewPort.getMaxX(),0);
        Shape yAxisLine = new Line2D.Double(0,viewPort.getMinY(),0,viewPort.getMaxY());
        if (xAxisLine.intersects(viewPort)) {
            axesShape.append(xAxisLine,false);
        }
        if (yAxisLine.intersects(viewPort)) {
            axesShape.append(yAxisLine,false);
        }
        
        axesMarksShape = new GeneralPath();
        xAxis = new AxisDescriptor(viewPort.getMinX(),viewPort.getMaxX());
        yAxis = new AxisDescriptor(viewPort.getMinY(),viewPort.getMaxY());
        axesMarksShape.append(new AxisGridPathIterator(true,viewPort,xAxis),false);
        axesMarksShape.append(new AxisGridPathIterator(false,viewPort,yAxis),false);
        
        // Invalidate transformed shapes
        oldSize = null;
    }
    
    /* ---------------------------------------------------------------------- */
    /* Transformations and painting                                           */
    /* ---------------------------------------------------------------------- */
    private AffineTransform transform;
    private Dimension oldSize;
    private Shape transformedFunctionShape;
    private Shape transformedAxesShape;
    private Shape transformedAxesMarksShape;
    private Rectangle mainCanvas;
    
    private void transformShapes() {
        
        transform = AffineTransform.getScaleInstance(
                mainCanvas.getWidth() / viewPort.getWidth(),
                - (mainCanvas.getHeight() / viewPort.getHeight()));
        transform.translate(
                (mainCanvas.getX()/transform.getScaleX())-viewPort.getX(),
                viewPort.getY()+(mainCanvas.getY()/transform.getScaleY()));
        transformedFunctionShape = functionShape.createTransformedShape(transform);
        transformedAxesShape = axesShape.createTransformedShape(transform);
        transformedAxesMarksShape = axesMarksShape.createTransformedShape(transform);
    };
    
    private static int maxAxisCaptionWidth(AxisDescriptor axis,
            FontMetrics fm, NumberFormat captionFormat) {
        int maxCaptionWidth = 0;
        for (int i = 0; i < axis.getLinesCount(); i++) {
            maxCaptionWidth = Math.max(maxCaptionWidth, fm.stringWidth(
                    captionFormat.format(axis.getLinePosition(i))));
        }
        return maxCaptionWidth;
    }
    
    private void computeMainCanvas(final Dimension size) {
        
        FontMetrics fm = getGraphics().getFontMetrics();
        int leftSpace = maxAxisCaptionWidth(yAxis,fm,CAPTION_FORMAT);
        
        mainCanvas = new Rectangle(
                leftSpace + 2, fm.getHeight() + 2,
                size.width - leftSpace - fm.getHeight() - 2,
                size.height - (2*fm.getHeight()+4));
    }
    
    private static final Stroke FUNCTION_STROKE = new BasicStroke(3);
    private static final Stroke AXES_STROKE = new BasicStroke(2);
    private static final Stroke AXES_MARKS_STROKE = new BasicStroke(1,
            BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10,
            new float[] { 3f }, 1f);
    private static final NumberFormat CAPTION_FORMAT = NumberFormat.getNumberInstance();
    
    private void paintAxisCaptions(Graphics2D g) {
        Point2D src = new Point2D.Double();
        Point dst = new Point();
        FontMetrics fm = g.getFontMetrics();
        int fontSize = fm.getHeight();
        
        // if there is no space for y captions, don't draw them        
        if (fontSize <= (yAxis.getDelta() * - transform.getScaleY())) {
            // Y axis captions
            for (int i = 0; i < yAxis.getLinesCount(); i++) {
                src.setLocation(0, yAxis.getLinePosition(i));
                String caption = CAPTION_FORMAT.format(src.getY());
                transform.transform(src,dst);
                g.drawString(caption,
                        mainCanvas.x - fm.stringWidth(caption),
                        dst.y);
            }
        }
        
        // if there is no space for x captions, don't draw them
        if (maxAxisCaptionWidth(xAxis, fm, CAPTION_FORMAT) <=
                (xAxis.getDelta() * transform.getScaleX())) {
            
            // X axis captions
            for (int i = 0; i < xAxis.getLinesCount(); i++) {
                src.setLocation(xAxis.getLinePosition(i),0);
                String caption = CAPTION_FORMAT.format(src.getX());
                transform.transform(src,dst);
                g.drawString(caption,
                        dst.x - (fm.stringWidth(caption)/2),
                        mainCanvas.y + mainCanvas.height + fontSize);
            }
            
        }        
    }
    
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponents(graphics);
        if (function == null || viewPort == null) {
            return;
        }
        Dimension size = getSize();
        if (!size.equals(oldSize)) {
            computeMainCanvas(size);
            transformShapes();
            oldSize = size;
        }
        Graphics2D g = (Graphics2D) graphics;
        paintAxisCaptions(g);
        g.setColor(Color.WHITE);
        g.fill(mainCanvas);
        g.setColor(getForeground());
        g.draw(mainCanvas);
        g.setClip(mainCanvas);
        g.setStroke(AXES_MARKS_STROKE);
        g.draw(transformedAxesMarksShape);
        g.setStroke(AXES_STROKE);
        g.draw(transformedAxesShape);
        g.setStroke(FUNCTION_STROKE);
        g.draw(transformedFunctionShape);
    }
    
}