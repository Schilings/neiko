package com.schilings.neiko.common.excel.handler.request;

import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.processor.AnalysisEventProcessor;
import com.schilings.neiko.common.excel.vo.ImprotErrorMessage;

import java.util.List;

/**
 * <pre>
 * <p>{@link AnalysisEventProcessor}</p>
 * </pre>
 *
 * @author Schilings
 */
public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

	public abstract List<T> getList();

	public abstract List<ImprotErrorMessage> getErrors();

}
