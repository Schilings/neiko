package com.schilings.neiko.samples.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 原因： com.fasterxml.jackson.databind.ObjectMapper.readValue(String content, Class<T>
 * valueType)方法，传入的class对象没有无参构造器，具体原因是在该对象上同时使用了@Data和@AllArgsConstructor注解，@AllArgsConstructor阻止了@Data生成（无参）构造器，从而该对象只有一个全参构造器，没有无参构造器，导致反序列化失败。
 * <p>
 * 解决方法： 显式添加无参构造器或使用@NoArgsConstructor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DTO {

	private static final long serialVersionUID = 1L;

	private int id;

	private String username;

	private String password;

}
