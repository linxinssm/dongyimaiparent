package com.dongyimai.user.controller;
import java.util.List;

import com.offcn.user.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.dongyimai.pojo.TbUser;

import com.dongyimai.entity.PageResult;
import com.dongyimai.entity.Result;
/**
 * 用户表controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;


	//验证码校验


	@RequestMapping("/sendCode")
	public Result sendCode(String phone){

		//一定要写明电话号
		if(phone == null || phone.length()<=0){
			return new Result(false,"请输入电话号");
		}

		try {
			userService.createSmsCode(phone);
			return new Result(true,"发送验证码成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"发送验证码失败");
		}


	}
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){
		return userService.findPage(page, rows);
	}

	/**
	 * 增加
	 * @param user
	 * @return
	 */
	//短信验证,和 用户注册
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String checkCode){
		try {

			//1、通过用户的电话号码
			//一定要写明电话号
			if(user.getPhone() == null || user.getPhone().length()<=0){
				return new Result(false,"请输入电话号");
			}
			//2、从redis中取得 以phone 为key 的验证码
			boolean flag = userService.checkCode(user.getPhone(),checkCode);

			//3、校验验证码
			if(!flag){
				return new Result(false,"验证码填写错误");
			}

			userService.add(user);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		try {
			userService.update(user);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbUser findOne(Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}

}
