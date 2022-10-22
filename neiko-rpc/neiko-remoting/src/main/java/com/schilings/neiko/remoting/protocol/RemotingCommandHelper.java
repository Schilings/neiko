package com.schilings.neiko.remoting.protocol;


import com.schilings.neiko.remoting.exception.RemotingCommandException;

import java.nio.ByteBuffer;

public class RemotingCommandHelper {


    /**
     * 创建一个请求命令
     * @param code
     * @param customHeader
     * @return
     */
    public static RemotingCommand createRequestCommand(int code, CommandHeaderCustomizer customHeader) {
        RemotingCommand cmd = new RemotingCommand();
        cmd.setCode(code);
        cmd.customHeader = customHeader;
        RemotingCommand.setCmdVersion(cmd);
        return cmd;
    }
    
    
    public static RemotingCommand createResponseCommand(Class<? extends CommandHeaderCustomizer> classHeader) {
        return createResponseCommand(RemotingSysResponseCode.SYSTEM_ERROR, "not set any response code", classHeader);
    }

    public static RemotingCommand createResponseCommand(int code, String remark) {
        return createResponseCommand(code, remark, null);
    }

    /**
     * 创建响应命令
     * @param code
     * @param remark
     * @param classHeader
     * @return
     */
    public static RemotingCommand createResponseCommand(int code, String remark, Class<? extends CommandHeaderCustomizer> classHeader) {
        RemotingCommand cmd = new RemotingCommand();
        cmd.markResponseType();
        cmd.setCode(code);
        cmd.setRemark(remark);
        RemotingCommand.setCmdVersion(cmd);

        if (classHeader != null) {
            try {
                CommandHeaderCustomizer objectHeader = classHeader.newInstance();
                cmd.customHeader = objectHeader;
            } catch (InstantiationException e) {
                return null;
            } catch (IllegalAccessException e) {
                return null;
            }
        }

        return cmd;
    }
    
    
    
    public static RemotingCommand decode(final ByteBuffer byteBuffer) throws RemotingCommandException {
        int length = byteBuffer.limit();
        int oriHeaderLen = byteBuffer.getInt();
        int headerLength = getHeaderLength(oriHeaderLen);

        byte[] headerData = new byte[headerLength];
        byteBuffer.get(headerData);

        RemotingCommand cmd = headerDecode(headerData, getProtocolType(oriHeaderLen));

        int bodyLength = length - 4 - headerLength;
        byte[] bodyData = null;
        if (bodyLength > 0) {
            bodyData = new byte[bodyLength];
            byteBuffer.get(bodyData);
        }
        cmd.setBody(bodyData);
        
        return cmd;
    }

    /**
     * 截取头报文部信息长度，前四个字节中的后三个字节
     * @param length
     * @return
     */
    public static int getHeaderLength(int length) {
        return length & 0xFFFFFF;
    }

    
    private static RemotingCommand headerDecode(byte[] headerData, SerializeType type) throws RemotingCommandException {
        switch (type) {
            case JSON:
                RemotingCommand resultJson = RemotingSerializable.decode(headerData, RemotingCommand.class);
                resultJson.setSerializeTypeCurrentRPC(type);
                return resultJson;
            default:
                break;
        }

        return null;
    }


    /**
     * 标记协议类型：第一个字节：序列化方式 剩余三个字节：记录
     * @param source
     * @param type
     * @return
     */
    public static byte[] markProtocolType(int source, SerializeType type) {
        byte[] result = new byte[4];

        result[0] = type.getCode();
        result[1] = (byte) ((source >> 16) & 0xFF);
        result[2] = (byte) ((source >> 8) & 0xFF);
        result[3] = (byte) (source & 0xFF);
        return result;
    }


    public static SerializeType getProtocolType(int source) {
        return SerializeType.valueOf((byte) ((source >> 24) & 0xFF));
    }


    private static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}
