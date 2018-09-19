package com.sparsity.routeplanner.visualization;

import com.sparsity.routeplanner.vrpdomain.VehicleRoutingSolution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class SolutionPanel extends JPanel {

    private SolutionPainter solutionPainter = new SolutionPainter();
    private VehicleRoutingSolution solution;

    public SolutionPanel() {
        this.solutionPainter = new SolutionPainter();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (solution != null) {
                    resetPanel();
                }
            }
        });
    }


    public VehicleRoutingSolution getSolution() {
        return solution;
    }

    public void setSolution(VehicleRoutingSolution solution) {
        this.solution = solution;
    }

    public void resetPanel() {
        solutionPainter.reset(solution, getSize(), this);
        repaint();
    }

    public void updatePanel(VehicleRoutingSolution solution) {
        this.solution = solution;
        resetPanel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage canvas = solutionPainter.getCanvas();
        if (canvas != null) {
            g.drawImage(canvas, 0, 0, this);
        }
    }

}
