package com.timidtlk.chip8.core;

public class Display {
    private boolean[][] pixels;
    public final int WIDTH = 64;
    public final int HEIGHT = 32;

    public Display() {
        pixels = new boolean[HEIGHT][WIDTH];
    }

    public boolean getPixel(int x, int y) {
        return pixels[y][x];
    }

    public void setPixel(int x, int y, boolean newPixel) {
        pixels[y][x] = newPixel;
    }

    public boolean togglePixel(int x, int y) {
        pixels[y][x] = !pixels[y][x];

        return !pixels[y][x];
    }
    
    public void clearScreen() {
        pixels = new boolean[HEIGHT][WIDTH];
    }
}
