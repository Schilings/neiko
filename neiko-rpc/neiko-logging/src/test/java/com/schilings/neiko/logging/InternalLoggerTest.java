/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.schilings.neiko.logging;

import com.schilings.neiko.logging.inner.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InternalLoggerTest {

	@Test
	public void testInternalLogger() {
		SysLogger.setQuietMode(false);
		SysLogger.setInternalDebugging(true);
		PrintStream out = System.out;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(byteArrayOutputStream));

		Appender consoleAppender = LoggingBuilder.newAppenderBuilder().withConsoleAppender(LoggingBuilder.SYSTEM_OUT)
				.withLayout(LoggingBuilder.newLayoutBuilder().withDefaultLayout().build()).build();

		Logger consoleLogger = Logger.getLogger("ConsoleLogger");
		consoleLogger.setAdditivity(false);
		consoleLogger.addAppender(consoleAppender);
		consoleLogger.setLevel(Level.INFO);

		Logger.getRootLogger().addAppender(consoleAppender);

		InternalLoggerFactory.setCurrentLoggerType(InternalLoggerFactory.LOGGER_INNER);
		InternalLogger logger = InternalLoggerFactory.getLogger(InternalLoggerTest.class);
		InternalLogger consoleLogger1 = InternalLoggerFactory.getLogger("ConsoleLogger");

		consoleLogger1.warn("simple warn {}", 14555);

		logger.info("testInternalLogger");
		consoleLogger1.info("consoleLogger1");

		System.setOut(out);
		consoleAppender.close();

		String result = new String(byteArrayOutputStream.toByteArray());
		Assert.assertTrue(result.contains("consoleLogger1"));
		Assert.assertTrue(result.contains("testInternalLogger"));
	}

}
