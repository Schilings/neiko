package com.schilings.neiko.samples.starters.web.controller;

import com.schilings.neiko.common.model.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/sample")
public class SampleController {

	@PostMapping("/test1")
	public R test1(@RequestPart("file") MultipartFile file) {
		String msg = "Current value of RequestPart [MultipartFile] : name is [" + file.getOriginalFilename()
				+ "], file length is [" + file.getSize() + "]";
		log.info(msg);
		return R.ok(msg);
	}

	@PostMapping("/test2")
	public R test2(MultipartFile file) {
		String msg = "Current value of RequestPart [MultipartFile] : name is [" + file.getOriginalFilename()
				+ "], file length is [" + file.getSize() + "]";
		log.info(msg);
		return R.ok(msg);
	}

}
