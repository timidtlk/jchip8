package com.timidtlk.chip8.core;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.LineUnavailableException;

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
    private InputHandler input;

    public CPU(InputStream romFile, Display display, InputHandler input) {
        program_counter = 0x200;
        index_register = 0;
        stack_pointer = 0;
        memory = new byte[4096];
        registers = new byte[16];
        stack = new short[16];

        delay_timer = 0;
        sound_timer = 0;

        if (sound_timer > 0) {
            try {
                Sound.beep(440, sound_timer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }

        this.display = display;
        this.input = input;

        try {
            int data;
            while ((data = romFile.read()) != -1) {
                memory[program_counter++] = (byte) data;
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
        short opcode = (short) ((memory[program_counter] & 0xFF) << 8 | (memory[program_counter + 1] & 0xFF));
        opcode &= 0xFFFF;

        program_counter += 2;
        
        executeOpcode(opcode);

        delay_timer -= (delay_timer > 0) ? 1 : 0;
        sound_timer -= (sound_timer > 0) ? 1 : 0;
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
                        program_counter = stack[--stack_pointer];
                        break;
                }
                break;
            case 0x1000:
                program_counter = (short) (opcode & 0xFFF);
                break;
            case 0x2000:
                stack[stack_pointer] = program_counter;
                stack_pointer++;
                program_counter = (short) (opcode & 0xFFF);
                break;
            case 0x3000:
                if (registers[x] == (opcode & 0xFF)) program_counter += 2;
                break;
            case 0x4000:
                if (registers[x] != (opcode & 0xFF)) program_counter += 2;
                break;
            case 0x5000:
                if (registers[x] == registers[y]) program_counter += 2;
                break;
            case 0x6000:
                registers[x] = (byte) (opcode & 0xFF);
                break;
            case 0x7000:
                registers[x] += (byte) (opcode & 0xFF);
                break;
            case 0x8000:
                switch (opcode & 0xF) {
                    case 0x0:
                        registers[x] = registers[y];
                        break;
                    case 0x1:
                        registers[x] |= registers[y];
                        break;
                    case 0x2:
                        registers[x] &= registers[y];
                        break;
                    case 0x3:
                        registers[x] ^= registers[y];
                        break;
                    case 0x4:
                        int sum = registers[x] + registers[y];
                        registers[0xF] = (byte) ((sum > 255) ? 1 : 0);
                        registers[x] = (byte) (sum & 0xFF);
                        break;
                    case 0x5:
                        registers[0xF] = (byte) ((registers[x] > registers[y]) ? 1 : 0);
                        registers[x] -= registers[y];
                        break;
                    case 0x6:
                        registers[0xF] = (byte) (registers[x] & 0x1);
                        registers[x] >>= 1;
                        break;
                    case 0x7:
                        registers[0xF] = (byte) ((registers[y] > registers[x]) ? 1 : 0);
                        registers[y] -= registers[x];
                        break;
                    case 0xE:
                        registers[0xF] = (byte) ((registers[x] & 0x80) >> 7);
                        registers[x] <<= 1;
                        break;
                }
            case 0x9000:
                if (registers[x] != registers[y]) program_counter += 2;
                break;
            case 0xA000:
                index_register = (short) (opcode & 0xFFF);
                break;
            case 0xB000:
                program_counter = (short) (opcode & 0xFFF + registers[0]);
                break;
            case 0xC000:
                registers[x] = (byte)(Math.round(Math.random() * 0xFF) & (opcode & 0xFF));
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
            case 0xE000:
                switch (opcode & 0xFF) {
                    case 0x9E:
                        if (input.getPressedKeys()[registers[x]]) 
                            program_counter += 2;
                        break;
                    case 0xA1:
                        if (!input.getPressedKeys()[registers[x]]) 
                            program_counter += 2;
                        break;
                }
                break;
            case 0xF000:
                switch (opcode & 0xFF) {
                    case 0x07:
                        registers[x] = delay_timer;
                        break;
                    case 0x0A:
                        boolean isPressed = false;
                        for (boolean key : input.getPressedKeys()) {
                            if (key) {
                                isPressed = true;
                                break;
                            }
                        }

                        if (isPressed) registers[x] = (byte) input.getPressedKey();
                        else program_counter -= 2;
                        
                        break;
                    case 0x15:
                        delay_timer = registers[x];
                        break;
                    case 0x18:
                        sound_timer = registers[x];
                        break;
                    case 0x1E:
                        index_register += registers[x];
                        break;
                    case 0x29:
                        index_register = (short) (0x50 + registers[x] * 5);
                        break;
                    case 0x33:
                        int value_u = registers[x] & 0xFF;

                        memory[index_register + 2] = (byte) (value_u % 10);
    
                        value_u /= 10;
                        memory[index_register + 1] = (byte) (value_u % 10);

                        value_u /= 10;
                        memory[index_register] = (byte) (value_u % 10);
                        break;
                    case 0x55:
                        for (int i = 0; i <= x; i++) {
                            memory[index_register + i] = registers[i];                            
                        }
                        break;
                    case 0x65:
                        for (int i = 0; i < x + 1; i++) {
                            registers[i] = memory[index_register + i];                            
                        }
                        break;
                }
                break;
            default:
                System.out.println(Integer.toHexString(opcode));
                break;
        }
    }
}
