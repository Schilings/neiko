package com.schilings.neiko.samples.starter.datascope.datarule.service;

import com.schilings.neiko.common.datascope.annotation.DataPermission;
import com.schilings.neiko.samples.starter.datascope.datarule.datascope.ClassDataScope;
import com.schilings.neiko.samples.starter.datascope.datarule.datascope.SchoolDataScope;
import com.schilings.neiko.samples.starter.datascope.datarule.entity.Student;
import com.schilings.neiko.samples.starter.datascope.datarule.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentService {

	@Autowired
	private StudentMapper studentMapper;

	/**
	 * 默认使用全局数据权限
	 * @return
	 */
	public List<Student> listStudent() {
		return studentMapper.listStudent();
	}

	/**
	 * 指定数据权限维度
	 * @return
	 */
	@DataPermission(includeResources = ClassDataScope.RESOURCE_NAME)
	public List<Student> listStudentOnlyFilterClass() {
		return studentMapper.listStudent();
	}
	/**
	 * 指定数据权限维度
	 * @return
	 */
	@DataPermission(includeResources = SchoolDataScope.RESOURCE_NAME)
	public List<Student> listStudentOnlyFilterSchool() {
		return studentMapper.listStudent();
	}

	/**
	 * 忽略数据权限
	 * @return
	 */
	@DataPermission(ignore = true)
	public List<Student> listStudentWithoutDataPermission() {
		return studentMapper.listStudent();
	}

}
