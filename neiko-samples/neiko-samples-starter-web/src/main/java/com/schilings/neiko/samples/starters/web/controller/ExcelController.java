package com.schilings.neiko.samples.starters.web.controller;

import com.schilings.neiko.common.excel.annotation.RequestExcel;
import com.schilings.neiko.common.excel.annotation.ResponseExcel;
import com.schilings.neiko.common.excel.annotation.Sheet;
import com.schilings.neiko.common.excel.vo.ImprotErrorMessage;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.samples.starters.web.dto.UserAddDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/excel")
public class ExcelController {

	@PostMapping("/test1")
	public R test1(@RequestExcel("file") List<UserAddDTO> users, BindingResult bindingResult) {
		System.out.println(users);
		List<ImprotErrorMessage> errors = (List<ImprotErrorMessage>) bindingResult.getTarget();
		for (ImprotErrorMessage error : errors) {
			System.out.println(error.getLineNum() + "-" + error.getMessages());
		}
		return R.ok();
	}

	@GetMapping("/test2")
	@ResponseExcel(name = "test", sheets = { @Sheet(sheetNo = 1, sheetName = "sheetNO1") })
	public List<UserAddDTO> test2() {
		return Arrays.asList(new UserAddDTO("admin1", "admin1", BigDecimal.TEN));
	}

}
