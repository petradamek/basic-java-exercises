package cz.bilysklep.trainings.basicjava.functions.gui;

import cz.bilysklep.trainings.basicjava.functions.PolynomialFunction;
import cz.bilysklep.trainings.basicjava.functions.SquareRootFunction;
import cz.bilysklep.trainings.basicjava.functions.UnaryFunction;
import cz.bilysklep.trainings.basicjava.functions.SinusFunction;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

/**
 * This window allows to visualize any unary function.
 *
 * @author Petr Adamek
 */
public class UnaryFunctionFrame extends JFrame {
    
    private final UnaryFunctionPane functionPane = new UnaryFunctionPane();
    
    private final List<UnaryFunction> functions = new ArrayList<>();
    private final List<Rectangle2D> viewPorts = new ArrayList<>();
    
    private void loadData() {
        functions.add(new SinusFunction(1,1));
        functions.add(new SinusFunction(0.5,2));
        functions.add(new SinusFunction(2,0.5));
        functions.add(new SquareRootFunction());
        functions.add(new PolynomialFunction(0));
        functions.add(new PolynomialFunction(Math.E));
        functions.add(new PolynomialFunction(1,2));
        functions.add(new PolynomialFunction(1,2,3));
        functions.add(new PolynomialFunction(1,2,3,4));
        functions.add(new PolynomialFunction(1,0,0,4));
        functions.add(new PolynomialFunction(1,0,2,4));
        viewPorts.add(new Rectangle2D.Double(-1,-1,2,2));
        viewPorts.add(new Rectangle2D.Double(-Math.PI,-1,2*Math.PI,2));
        viewPorts.add(new Rectangle2D.Double(-10,-2,20,4));
        viewPorts.add(new Rectangle2D.Double(-5,-5,10,10));
    }
        
    /**
     * Creates a new instance of UnaryFunctionFrame
     */
    public UnaryFunctionFrame() {
        loadData();
        initComponents();
    }
    
    private void initComponents() {
        ResourceBundle bundle = ResourceBundle.getBundle(
                UnaryFunctionFrame.class.getPackage().getName() + ".Bundle");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(functionPane,BorderLayout.CENTER);
        
        final JComboBox functionSelectionBox = new JComboBox(functions.toArray());
        final JComboBox viewPortSelectionBox = new JComboBox(viewPorts.toArray());
        final JSpinner segmentsCountSpinner = new JSpinner(
                new SpinnerNumberModel(50,1,400,10));
        
        ActionListener comboBoxesActionListener = (ActionEvent e) -> {
            if (e.getSource() == functionSelectionBox) {
                functionPane.setFunction(
                        (UnaryFunction) functionSelectionBox.getSelectedItem());
            } else if (e.getSource() == viewPortSelectionBox) {
                functionPane.setViewPort(
                        (Rectangle2D) viewPortSelectionBox.getSelectedItem());
            }
        };

        functionSelectionBox.addActionListener(comboBoxesActionListener);
        viewPortSelectionBox.addActionListener(comboBoxesActionListener);
        segmentsCountSpinner.addChangeListener((ChangeEvent e) -> {
            functionPane.setSegmentsCount(
                    ((Number) segmentsCountSpinner.getValue()).intValue());
        });
        viewPortSelectionBox.setRenderer(new viewPortCellRenderer());
        
        JPanel toolBar = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);        
        toolBar.add(new JLabel(bundle.getString("JLabel_Function")),gridBagConstraints);
        toolBar.add(new JLabel(bundle.getString("JLabel_ViewPort")),gridBagConstraints);
        toolBar.add(new JLabel(bundle.getString("JLabel_SegmentsCount")),gridBagConstraints);
//        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.4;
        gridBagConstraints.gridy = 1;
        toolBar.add(functionSelectionBox,gridBagConstraints);
        toolBar.add(viewPortSelectionBox,gridBagConstraints);
        gridBagConstraints.weightx = 0.2;
        toolBar.add(segmentsCountSpinner,gridBagConstraints);
        
        add(toolBar,BorderLayout.NORTH);
        functionSelectionBox.setSelectedIndex(0);
        viewPortSelectionBox.setSelectedIndex(0);
        segmentsCountSpinner.setValue(100);
//        functionPane.setMinimumSize(new Dimension(100,100));
//        pack();
        setSize(320,200);
    }
    
    /**
     * Main method. It shows this window.
     *
     * @param args arguments from command line
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new UnaryFunctionFrame().setVisible(true);
        });
    }
    
    private static class viewPortCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            
            super.getListCellRendererComponent(list,value,index,isSelected,cellHasFocus);
            Rectangle2D rect = (Rectangle2D) value;
            setText("[minX=" + rect.getMinX() + ", maxX=" + rect.getMaxX() +
                    ", minY=" + rect.getMinY() + ", maxY=" + rect.getMaxY());
            return this;
        }
        
    }
    
}
