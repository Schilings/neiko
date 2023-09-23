package com.schilings.neiko.samples.application.web;

import cn.hutool.core.util.ByteUtil;
import com.schilings.neiko.common.model.result.R;
import com.schilings.neiko.system.remote.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.PathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
public class SystemRemoteController {

	private final SysUserRemote sysUserRemote;

	private final SysConfigRemote sysConfigRemote;

	private final SysDictRemote sysDictRemote;

	private final SysOrganizationRemote sysOrganizationRemote;

	private final SysMenuRemote sysMenuRemote;

	private final SysRoleRemote sysRoleRemote;

	@GetMapping("/user")
	public Object user() throws IOException {

		// BodyInserters.fromMultipartData("file",new FileSystemResource("E:\\PS
		// files\\pic\\普通图\\3-s.jpg"))

		File file = new File("E:\\PS files\\pic\\普通图\\3-s.jpg");

		FileSystemResource fileSystemResource = new FileSystemResource(file);
		// FileUrlResource fileUrlResource = new FileUrlResource("E:\\PS
		// files\\pic\\普通图\\3-s.jpg");
		PathResource pathResource = new PathResource(Paths.get("E:\\PS files\\pic\\普通图\\3-s.jpg"));

		R<String> result = null;

		// result = sysUserRemote.updateAvatar(fileUrlResource, 1L);
		result = sysUserRemote.updateAvatar(fileSystemResource, 1L);
		result = sysUserRemote.updateAvatar(pathResource, 1L);

		return sysUserRemote.getUserPage(null, null);
	}

}
