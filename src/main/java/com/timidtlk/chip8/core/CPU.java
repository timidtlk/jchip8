package com.timidtlk.chip8.core;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

public class CPU {
    private short program_counter;
    private short index_register;
    private byte stack_pointer;
    private byte[] memory;
    private byte[] registers;
    private short[] stack;

    private byte delay_timer;
    private byte sound_timer;

    private Display display;

    public CPU(InputStream romFile, Display display) {
        program_counter = 0x200;
        index_register = 0;
        stack_pointer = -1;
        memory = new byte[4096];
        registers = new byte[16];
        stack = new short[16];

        delay_timer = 0;
        sound_timer = 0;

        this.display = display;

        try {
            int data;
            while ((data = romFile.read()) != -1) {
                memory[program_counter++] = (byte) data;
                System.out.printf("%s ", Integer.toHexString(data).toUpperCase());
            }
            program_counter = 0x200;
            romFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0x50; i < Font.font.length; i++) {
            memory[i] = Font.font[i];
        }
    }

    public void tick() {
        short opcode = (short) (memory[program_counter] << 8 | memory[program_counter + 1]);

        program_counter += 2;
        
        executeOpcode(opcode);
    }

    public void executeOpcode(short opcode) {

        int x = (opcode & 0x0F00) >> 8;
        int y = (opcode & 0x00F0) >> 4;

        switch (opcode & 0xF000) {
            case 0x0000:
                switch (opcode) {
                    case 0x00E0:
                        display.clearScreen();
                        break;
                    case 0x00EE:
                        break;
                }
                break;
            case 0x1000:
                program_counter = (short) (opcode & 0xFFF);
                break;
            case 0x6000:
                registers[x] = (byte) (opcode & 0xFF);
                break;
            case 0x7000:
                registers[x] += (byte) (opcode & 0xFF);
                break;
            case 0xA000:
                index_register = (short) (opcode & 0xFFF);
                break;
            case 0xD000:
                byte width = 8;
                byte height = (byte) (opcode & 0xF);

                registers[0xF] = 0;

                for (int i = 0; i < height; i++) {
                    byte sprite = memory[index_register + i];

                    for (int j = 0; j < width; j++) {
                        if ((sprite & 0x80) > 0) {
                            if (display.togglePixel(registers[x] + j, registers[y] + i)) {
                                registers[0xF] = 1;
                            }
                        }

                        sprite <<= 1;
                    }
                }
                break;
            default:
                System.out.println("Unknown instruction");
                break;
        }
    }
}
