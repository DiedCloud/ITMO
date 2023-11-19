package modules;

import result.Result;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class RequestHandler {
    private final DatagramChannel channel;
    private final ByteBuffer buffer;
    private final int timeout;

    public RequestHandler(DatagramChannel channel, int bufferSize, int timeout) throws IOException {
        this.channel = channel;
        this.buffer = ByteBuffer.allocate(bufferSize);
        this.timeout = timeout;
        channel.configureBlocking(false);
    }

    public void closeChannel() throws IOException {
        channel.close();
    }

    public Result<DatagramPacket> receivePacketWithTimeout() {
        try {
            Selector selector = Selector.open();
            channel.register(selector, SelectionKey.OP_READ);

            while (true) {
                int readyChannels = selector.select(timeout);
                if (readyChannels == 0) {
                    throw new SocketTimeoutException("TIMEOUT_ERROR");
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()) {
                        DatagramChannel datagramChannel = (DatagramChannel) key.channel();
                        buffer.clear();
                        if (datagramChannel.receive(buffer) != null) {
                            buffer.flip();
                            DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.limit());
                            return Result.success(packet);
                        }
                    }
                    keyIterator.remove();
                }
            }
        } catch (IOException e) {
            return Result.failure(e);
        }
    }
}
