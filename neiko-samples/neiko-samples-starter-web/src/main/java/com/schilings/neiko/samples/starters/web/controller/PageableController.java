package com.schilings.neiko.samples.starters.web.controller;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/page")
public class PageableController {

	@GetMapping("/get")
	public R get(PageParam pageParam) {
		return R.ok(pageParam);
	}

}
