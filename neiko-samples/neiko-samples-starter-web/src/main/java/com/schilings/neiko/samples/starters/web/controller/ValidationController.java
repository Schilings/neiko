package com.schilings.neiko.samples.starters.web.controller;

import com.schilings.neiko.autoconfigure.web.validation.exception.ValidationExceptionHandler;
import com.schilings.neiko.samples.starters.web.dto.UserAddDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/valid")
@Slf4j
@Validated
// @ValidationExceptionHandler
public class ValidationController {

	@GetMapping("/get")
	public void get(@RequestParam("id") @Min(value = 1L, message = "编号必须大于 0") Integer id) {
		log.info("[get][id: {}]", id);
	}

	/**
	 * Post/
	 * @param addDTO
	 */
	@PostMapping("/add")
	public void add(@Valid @RequestBody UserAddDTO addDTO) {
		log.info("[add][addDTO: {}]", addDTO);
	}

	@GetMapping("/test")
	public void test(@Validated UserAddDTO addDTO) {
		log.info("[add][addDTO: {}]", addDTO);
	}

}
