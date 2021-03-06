package chatV2.common.transmission;

import java.io.IOException;

public final class Protocol {
    private static final int DATA_IN_BUFFER_SIZE = 4096;
    private SerializationUtils serializationUtils;
    private SocketTransmission transmission;

    public Protocol( SerializationUtils serializationUtils,  SocketTransmission transmission) {
        this.serializationUtils = serializationUtils;
        this.transmission = transmission;
    }

    public void sendObject(Object obj) throws IOException {
        transmission.sendBytes(serializationUtils.getBytes(obj));
    }

    public Object receiveObject(Class<?> objectClass) throws IOException {
        byte[] objectBytes = new byte[DATA_IN_BUFFER_SIZE];
        int realLength = transmission.receiveBytes(objectBytes, 0, objectBytes.length);
        if (realLength > 0) {
            byte[] realBytes = new byte[realLength];
            System.arraycopy(objectBytes, 0, realBytes, 0, realLength);
            return objectClass != null ? serializationUtils.getObject(realBytes, objectClass)
                    : serializationUtils.getObject(realBytes);
        }
        return null;
    }

    public Object receiveObject() throws IOException {
        return receiveObject(null);
    }
}
