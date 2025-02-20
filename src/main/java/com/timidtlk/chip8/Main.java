package com.timidtlk.chip8;

import java.io.InputStream;
import javax.swing.JFrame;

import com.timidtlk.chip8.core.CPU;
import com.timidtlk.chip8.core.Display;
import com.timidtlk.chip8.core.InputHandler;
import com.timidtlk.chip8.core.Panel;

public class Main {

    private void initializeWindow(Panel panel) {
        JFrame frame = new JFrame("CHIP8");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    Main() {
        InputStream romFile = getClass().getClassLoader().getResourceAsStream("pong.ch8");

        InputHandler input = new InputHandler();
        Display display = new Display();
        CPU cpu = new CPU(romFile, display, input);
        Panel panel = new Panel(display);
        panel.addKeyListener(input);
        panel.setFocusable(true);

        initializeWindow(panel);

        try {
            while (true) {
                Thread.sleep(5);
                cpu.tick();
                panel.revalidate();
                panel.repaint();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }
    public static void main(String[] args) {
        new Main();
    }
}
