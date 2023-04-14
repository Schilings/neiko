package com.schilings.neiko.remoting.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import com.schilings.neiko.remoting.annotation.CFNotNull;
import com.schilings.neiko.remoting.exception.RemotingCommandException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * <p>
 * RPC远程调用消息载体
 * </p>
 *
 * @author Schilings
 */
public class RemotingCommand {

	public static final String SERIALIZE_TYPE_PROPERTY = "neiko.serialize.type";

	public static final String SERIALIZE_TYPE_ENV = "NEIKO_SERIALIZE_TYPE";

	public static final String REMOTING_VERSION_KEY = "neiko.remoting.version";

	/*
	 * 类属性
	 */
	private static final int RPC_TYPE = 0; // 0, REQUEST_COMMAND

	private static final int RPC_ONEWAY = 1; // 0, RPC

	private static final Map<Class<? extends CommandHeaderCustomizer>, Field[]> CLASS_HASH_MAP = new HashMap<Class<? extends CommandHeaderCustomizer>, Field[]>();

	private static final Map<Class, String> CANONICAL_NAME_CACHE = new HashMap<Class, String>();

	// 1, Oneway
	// 1, RESPONSE_COMMAND
	private static final Map<Field, Boolean> NULLABLE_FIELD_CACHE = new HashMap<Field, Boolean>();

	private static final String STRING_CANONICAL_NAME = String.class.getCanonicalName();

	private static final String DOUBLE_CANONICAL_NAME_1 = Double.class.getCanonicalName();

	private static final String DOUBLE_CANONICAL_NAME_2 = double.class.getCanonicalName();

	private static final String INTEGER_CANONICAL_NAME_1 = Integer.class.getCanonicalName();

	private static final String INTEGER_CANONICAL_NAME_2 = int.class.getCanonicalName();

	private static final String LONG_CANONICAL_NAME_1 = Long.class.getCanonicalName();

	private static final String LONG_CANONICAL_NAME_2 = long.class.getCanonicalName();

	private static final String BOOLEAN_CANONICAL_NAME_1 = Boolean.class.getCanonicalName();

	private static final String BOOLEAN_CANONICAL_NAME_2 = boolean.class.getCanonicalName();

	private static volatile int configVersion = -1;

	private static AtomicInteger requestId = new AtomicInteger(0);

	private static SerializeType serializeTypeConfigInThisServer = SerializeType.JSON;

	/*
	 * 实例属性
	 */
	// 状态码
	private int code;

	// 编程语言代号
	private LanguageCode language = LanguageCode.JAVA;

	// 版本信息
	private int version = 0;

	// 唯一标识
	private int opaque = requestId.getAndIncrement();

	// 类型标记 Request or Response or OneWay
	private int flag = 0;

	// 备注信息
	private String remark;

	// 额外信息 请求头等
	private HashMap<String, String> extFields;

	// 请求头自定义
	protected transient CommandHeaderCustomizer customHeader;

	// 序列化方式
	private SerializeType serializeTypeCurrentRPC = serializeTypeConfigInThisServer;

	// 消息内容
	private transient byte[] body;

	public static int createNewRequestId() {
		return requestId.getAndIncrement();
	}

	public static SerializeType getSerializeTypeConfigInThisServer() {
		return serializeTypeConfigInThisServer;
	}

	/**
	 * 设置命令版本号
	 * @param cmd
	 */
	protected static void setCmdVersion(RemotingCommand cmd) {
		if (configVersion >= 0) {
			cmd.setVersion(configVersion);
		}
		else {
			String v = System.getProperty(REMOTING_VERSION_KEY);
			if (v != null) {
				int value = Integer.parseInt(v);
				cmd.setVersion(value);
				configVersion = value;
			}
		}
	}

	/**
	 * 标记flag表示响应命令
	 */
	public void markResponseType() {
		int bits = 1 << RPC_TYPE;
		this.flag |= bits;
	}

	/**
	 * 读取自定义的额外头部信息
	 * @return
	 */
	public CommandHeaderCustomizer readCustomHeader() {
		return customHeader;
	}

	/**
	 * 写入自定义的额外头部信息
	 * @param customHeader
	 */
	public void writeCustomHeader(CommandHeaderCustomizer customHeader) {
		this.customHeader = customHeader;
	}

	/**
	 * extFields—>commandHeader
	 * @param classHeader
	 * @return
	 * @throws RemotingCommandException
	 */
	public CommandHeaderCustomizer decodeCommandHeaderCustomizer(Class<? extends CommandHeaderCustomizer> classHeader)
			throws RemotingCommandException {
		CommandHeaderCustomizer objectHeader;
		try {
			objectHeader = classHeader.newInstance();
		}
		catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
		if (this.extFields != null) {
			Field[] fields = getClazzFields(classHeader);
			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					String fieldName = field.getName();
					if (!fieldName.startsWith("this")) {
						try {
							String value = this.extFields.get(fieldName);
							if (null == value) {
								if (!isFieldNullable(field)) {
									throw new RemotingCommandException("the custom field <" + fieldName + "> is null");
								}
								continue;
							}

							field.setAccessible(true);
							String type = getCanonicalName(field.getType());
							Object valueParsed;

							if (type.equals(STRING_CANONICAL_NAME)) {
								valueParsed = value;
							}
							else if (type.equals(INTEGER_CANONICAL_NAME_1) || type.equals(INTEGER_CANONICAL_NAME_2)) {
								valueParsed = Integer.parseInt(value);
							}
							else if (type.equals(LONG_CANONICAL_NAME_1) || type.equals(LONG_CANONICAL_NAME_2)) {
								valueParsed = Long.parseLong(value);
							}
							else if (type.equals(BOOLEAN_CANONICAL_NAME_1) || type.equals(BOOLEAN_CANONICAL_NAME_2)) {
								valueParsed = Boolean.parseBoolean(value);
							}
							else if (type.equals(DOUBLE_CANONICAL_NAME_1) || type.equals(DOUBLE_CANONICAL_NAME_2)) {
								valueParsed = Double.parseDouble(value);
							}
							else {
								throw new RemotingCommandException(
										"the custom field <" + fieldName + "> type is not supported");
							}

							field.set(objectHeader, valueParsed);

						}
						catch (Throwable e) {
							// log.error("Failed field [{}] decoding", fieldName, e);
						}
					}
				}
			}

			objectHeader.checkFields();
		}

		return objectHeader;
	}

	private Field[] getClazzFields(Class<? extends CommandHeaderCustomizer> classHeader) {
		Field[] field = CLASS_HASH_MAP.get(classHeader);

		if (field == null) {
			field = classHeader.getDeclaredFields();
			synchronized (CLASS_HASH_MAP) {
				CLASS_HASH_MAP.put(classHeader, field);
			}
		}
		return field;
	}

	private boolean isFieldNullable(Field field) {
		if (!NULLABLE_FIELD_CACHE.containsKey(field)) {
			Annotation annotation = field.getAnnotation(CFNotNull.class);
			synchronized (NULLABLE_FIELD_CACHE) {
				NULLABLE_FIELD_CACHE.put(field, annotation == null);
			}
		}
		return NULLABLE_FIELD_CACHE.get(field);
	}

	private String getCanonicalName(Class clazz) {
		String name = CANONICAL_NAME_CACHE.get(clazz);

		if (name == null) {
			name = clazz.getCanonicalName();
			synchronized (CANONICAL_NAME_CACHE) {
				CANONICAL_NAME_CACHE.put(clazz, name);
			}
		}
		return name;
	}

	/**
	 * commandHeader —> extFields
	 */
	public void makeCustomHeaderToNet() {
		if (this.customHeader != null) {
			Field[] fields = getClazzFields(customHeader.getClass());
			if (null == this.extFields) {
				this.extFields = new HashMap<String, String>();
			}

			for (Field field : fields) {
				if (!Modifier.isStatic(field.getModifiers())) {
					String name = field.getName();
					if (!name.startsWith("this")) {
						Object value = null;
						try {
							field.setAccessible(true);
							value = field.get(this.customHeader);
						}
						catch (Exception e) {
							// log.error("Failed to access field [{}]", name, e);
						}

						if (value != null) {
							this.extFields.put(name, value.toString());
						}
					}
				}
			}
		}
	}

	/**
	 * 序列化报文头部信息 实际报文头部信息=指定序列化方式序列化RemotingCommand的结果
	 * @return
	 */
	private byte[] headerEncode() {
		// commandHeader —> extFields
		this.makeCustomHeaderToNet();
		//
		if (SerializeType.NEIKO == serializeTypeCurrentRPC) {
			return NeikoDefaultSerializable.neikoProtocolEncode(this);
		}
		else {
			return RemotingSerializable.encode(this);
		}
	}

	/**
	 * 序列化报文 整个报文组成：长度(4，头部长度+内容长度，不包括这4个字节) 报文头长度(4) 报文头内容(..) 报文内容(..)
	 * 注意：记录报文头部长度的4个字节。实际第一个用来记录序列化方式，所以实际只有三个字节记录报文头部长度
	 * @return
	 */
	public ByteBuffer encode() {
		// 1> header length size
		int length = 4;

		// 2> header data length
		byte[] headerData = this.headerEncode();
		length += headerData.length;

		// 3> body data length
		if (this.body != null) {
			length += body.length;
		}

		ByteBuffer result = ByteBuffer.allocate(4 + length);

		// length
		result.putInt(length);

		// header length
		result.put(RemotingCommandHelper.markProtocolType(headerData.length, serializeTypeCurrentRPC));

		// header data
		result.put(headerData);

		// body data;
		if (this.body != null) {
			result.put(this.body);
		}

		result.flip();

		return result;
	}

	/**
	 * 序列化报文，只序列化报文头部，不装入入body
	 * @return
	 */
	public ByteBuffer encodeHeader() {
		return encodeHeader(this.body != null ? this.body.length : 0);
	}

	public ByteBuffer encodeHeader(final int bodyLength) {
		// 1> header length size
		int length = 4;

		// 2> header data length
		byte[] headerData;
		headerData = this.headerEncode();

		length += headerData.length;

		// 3> body data length
		length += bodyLength;

		ByteBuffer result = ByteBuffer.allocate(4 + length - bodyLength);

		// length
		result.putInt(length);

		// header length
		result.put(RemotingCommandHelper.markProtocolType(headerData.length, serializeTypeCurrentRPC));

		// header data
		result.put(headerData);

		result.flip();

		return result;
	}

	/**
	 * 标记flag表示单向命令
	 */
	public void markOnewayRPC() {
		int bits = 1 << RPC_ONEWAY;
		this.flag |= bits;
	}

	@JSONField(serialize = false)
	public boolean isOnewayRPC() {
		int bits = 1 << RPC_ONEWAY;
		return (this.flag & bits) == bits;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@JSONField(serialize = false)
	public RemotingCommandType getType() {
		if (this.isResponseType()) {
			return RemotingCommandType.RESPONSE_COMMAND;
		}

		return RemotingCommandType.REQUEST_COMMAND;
	}

	@JSONField(serialize = false)
	public boolean isResponseType() {
		int bits = 1 << RPC_TYPE;
		return (this.flag & bits) == bits;
	}

	public LanguageCode getLanguage() {
		return language;
	}

	public void setLanguage(LanguageCode language) {
		this.language = language;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getOpaque() {
		return opaque;
	}

	public void setOpaque(int opaque) {
		this.opaque = opaque;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public HashMap<String, String> getExtFields() {
		return extFields;
	}

	public void setExtFields(HashMap<String, String> extFields) {
		this.extFields = extFields;
	}

	public void addExtField(String key, String value) {
		if (null == extFields) {
			extFields = new HashMap<String, String>();
		}
		extFields.put(key, value);
	}

	@Override
	public String toString() {
		return "RemotingCommand [code=" + code + ", language=" + language + ", version=" + version + ", opaque="
				+ opaque + ", flag(B)=" + Integer.toBinaryString(flag) + ", remark=" + remark + ", extFields="
				+ extFields + ", serializeTypeCurrentRPC=" + serializeTypeCurrentRPC + "]";
	}

	public SerializeType getSerializeTypeCurrentRPC() {
		return serializeTypeCurrentRPC;
	}

	public void setSerializeTypeCurrentRPC(SerializeType serializeTypeCurrentRPC) {
		this.serializeTypeCurrentRPC = serializeTypeCurrentRPC;
	}

}
