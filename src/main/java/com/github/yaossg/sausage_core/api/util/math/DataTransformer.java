package com.github.yaossg.sausage_core.api.util.math;

import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Deprecated
public class DataTransformer {
    public static int[] bytes2ints(byte[] bytes) {
        int[] ints = new int[(bytes.length + 3) / 4];
        try {
            for (int index = 0; index < ints.length; index++) {
                ints[index] |= (bytes[index * 4] & 0xFF);
                ints[index] |= ((bytes[index * 4 + 1] & 0xFF) << 8);
                ints[index] |= ((bytes[index * 4 + 2] & 0xFF) << 16);
                ints[index] |= ((bytes[index * 4 + 3] & 0xFF) << 24);
            }
        } catch (IndexOutOfBoundsException e) {
            // NO-OP
        }
        return ints;
    }

    public static byte[] ints2bytes(int[] ints) {
        byte[] bytes = new byte[ints.length * 4];
        for (int index = 0; index < ints.length; index++) {
            bytes[index * 4] = (byte) (ints[index] & 0xFF);
            bytes[index * 4 + 1] = (byte) (ints[index] >>> 8 & 0xFF);
            bytes[index * 4 + 2] = (byte) (ints[index] >>> 16 & 0xFF);
            bytes[index * 4 + 3] = (byte) (ints[index] >>> 24 & 0xFF);
        }
        return bytes;
    }

    public static int[] NBT2ints(NBTTagCompound compound) throws IOException {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        CompressedStreamTools.write(compound, new ByteBufOutputStream(buffer));
        return bytes2ints(buffer.array());
    }

    public static NBTTagCompound ints2NBT(int[] ints) throws IOException {
        return CompressedStreamTools.read(new ByteBufInputStream(
                new PacketBuffer(Unpooled.wrappedBuffer(ints2bytes(ints)))), NBTSizeTracker.INFINITE);
    }

    public static class DataSender {
        public Container container;
        public List<IContainerListener> listeners;

        public DataSender(Container container, List<IContainerListener> listeners) {
            this.container = container;
            this.listeners = listeners;
        }

        public static final int STATE_ID = Integer.MIN_VALUE;

        public void sendState(int state) {
            sendInt(STATE_ID, state);
        }

        public <E extends Enum<E>> void sendState(E e) {
            sendState(e.ordinal());
        }

        public void sendInt(int id, int data) {
            listeners.forEach(listener -> listener.sendWindowProperty(container, id, data));
        }

        public void sendFloat(int id, float data) {
            sendInt(id, Float.floatToIntBits(data));
        }

        public void sendString(String string) {
            sendArray(string.toCharArray());
        }

        public void sendArray(char[] array) {
            listeners.forEach(listener -> {
                listener.sendWindowProperty(container, -1, array.length);
                for (int i = 0; i < array.length; i++)
                    listener.sendWindowProperty(container, i, array[i]);
            });
        }

        public void sendArray(int[] array) {
            listeners.forEach(listener -> {
                listener.sendWindowProperty(container, -1, array.length);
                for (int i = 0; i < array.length; i++)
                    listener.sendWindowProperty(container, i, array[i]);
            });
        }

        public void sendNBT(NBTTagCompound compound) throws IOException {
            sendArray(NBT2ints(compound));
        }

    }

    public static float receiveFloat(int data) {
        return Float.intBitsToFloat(data);
    }

    @Nullable
    public static <E extends Enum<E>> E receiveState(int data, E[] values) {
        for (int i = 0; i < values.length; i++) {
            if(i == data)
                return values[i];
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    interface Receiver<T> {
        @SideOnly(Side.CLIENT)
        Optional<T> receive(int id, int data);
    }

    @SideOnly(Side.CLIENT)
    public static class StringReceiver implements Receiver<String> {
        @SideOnly(Side.CLIENT)
        public char[] cache;

        @SideOnly(Side.CLIENT)
        @Override
        public Optional<String> receive(int id, int data) {
            if(id == -1) cache = new char[data];
            else {
                cache[id] = (char) data;
                if(id == cache.length - 1)
                    return Optional.of(String.valueOf(cache));
            }
            return Optional.empty();
        }
    }

    @SideOnly(Side.CLIENT)
    public static class IntArrayReceiver implements Receiver<int[]> {
        @SideOnly(Side.CLIENT)
        public int[] cache;

        @SideOnly(Side.CLIENT)
        @Override
        public Optional<int[]> receive(int id, int data) {
            if(id == -1) cache = new int[data];
            else {
                cache[id] = data;
                if(id == cache.length - 1)
                    return Optional.of(cache);
            }
            return Optional.empty();
        }
    }

    @SideOnly(Side.CLIENT)
    public static class ByteArrayReceiver implements Receiver<byte[]> {
        @SideOnly(Side.CLIENT)
        IntArrayReceiver receiver;

        @SideOnly(Side.CLIENT)
        @Override
        public Optional<byte[]> receive(int id, int data) {
            return receiver.receive(id, data).map(DataTransformer::ints2bytes);
        }
    }
}
