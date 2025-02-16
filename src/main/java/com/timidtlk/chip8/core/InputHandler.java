package com.timidtlk.chip8.core;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {

    private boolean[] pressedKeys = new boolean[0x10];
    private int pressedKey = 0x0;

    @Override
    public void keyPressed(KeyEvent e) {
        int src = e.getKeyCode();

        switch (src) {
            case KeyEvent.VK_1:
                pressedKeys[0x1] = true;
                pressedKey = 0x1;
                break;
            case KeyEvent.VK_2:
                pressedKeys[0x2] = true;
                pressedKey = 0x2;
                break;
            case KeyEvent.VK_3:
                pressedKeys[0x3] = true;
                pressedKey = 0x3;
                break;
            case KeyEvent.VK_4:
                pressedKeys[0xC] = true;
                pressedKey = 0xC;
                break;
            case KeyEvent.VK_Q:
                pressedKeys[0x4] = true;
                pressedKey = 0x4;
                break;
            case KeyEvent.VK_W:
                pressedKeys[0x5] = true;
                pressedKey = 0x5;
                break;
            case KeyEvent.VK_E:
                pressedKeys[0x6] = true;
                pressedKey = 0x6;
                break;
            case KeyEvent.VK_R:
                pressedKeys[0xD] = true;
                pressedKey = 0xD;
                break;
            case KeyEvent.VK_A:
                pressedKeys[0x7] = true;
                pressedKey = 0x7;
                break;
            case KeyEvent.VK_S:
                pressedKeys[0x8] = true;
                pressedKey = 0x8;
                break;
            case KeyEvent.VK_D:
                pressedKeys[0x9] = true;
                pressedKey = 0x9;
                break;
            case KeyEvent.VK_F:
                pressedKeys[0xE] = true;
                pressedKey = 0xE;
                break;
            case KeyEvent.VK_Z:
                pressedKeys[0xA] = true;
                pressedKey = 0xA;
                break;
            case KeyEvent.VK_X:
                pressedKeys[0x0] = true;
                pressedKey = 0x0;
                break;
            case KeyEvent.VK_C:
                pressedKeys[0xB] = true;
                pressedKey = 0xB;
                break;
            case KeyEvent.VK_V:
                pressedKeys[0xF] = true;
                pressedKey = 0xF;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int src = e.getKeyCode();

        switch (src) {
            case KeyEvent.VK_1:
                pressedKeys[0x1] = false;
                break;
            case KeyEvent.VK_2:
                pressedKeys[0x2] = false;
                break;
            case KeyEvent.VK_3:
                pressedKeys[0x3] = false;
                break;
            case KeyEvent.VK_4:
                pressedKeys[0xC] = false;
                break;
            case KeyEvent.VK_Q:
                pressedKeys[0x4] = false;
                break;
            case KeyEvent.VK_W:
                pressedKeys[0x5] = false;
                break;
            case KeyEvent.VK_E:
                pressedKeys[0x6] = false;
                break;
            case KeyEvent.VK_R:
                pressedKeys[0xD] = false;
                break;
            case KeyEvent.VK_A:
                pressedKeys[0x7] = false;
                break;
            case KeyEvent.VK_S:
                pressedKeys[0x8] = false;
                break;
            case KeyEvent.VK_D:
                pressedKeys[0x9] = false;
                break;
            case KeyEvent.VK_F:
                pressedKeys[0xE] = false;
                break;
            case KeyEvent.VK_Z:
                pressedKeys[0xA] = false;
                break;
            case KeyEvent.VK_X:
                pressedKeys[0x0] = false;
                break;
            case KeyEvent.VK_C:
                pressedKeys[0xB] = false;
                break;
            case KeyEvent.VK_V:
                pressedKeys[0xF] = false;
                break;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    public boolean[] getPressedKeys() {
        return pressedKeys;
    }

    public int getPressedKey() {
        return pressedKey;
    }
}
