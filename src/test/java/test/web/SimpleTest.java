package test.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import web.dao.mapper.UserMapper;
import web.domain.Detail;
import web.domain.User;
import web.springmvc.service.SpringContextService;
import web.util.SerializeUtil;
import web.util.SpringBeanContextUtil;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author lwz
 * @Description
 * @Date: 20:29 2019-03-19
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring-config.xml"})
@Transactional
public class SimpleTest {

	@Resource
	UserMapper userMapper;
//	@Resource
//	SpringContextService springContextService;
	@Autowired
	Environment evn;

	@Test
	public void testSelect() throws Exception{
		System.out.println(("----- selectAll method test ------"));

		User entity = userMapper.selectOne(new QueryWrapper<User>().eq("id", 1));
		entity.setDetailList(Lists.newArrayList(Detail.builder().id(32L).build()));

		Class[] objects = new Class[]{User.class, List.class};
		byte[] se = SerializeUtil.serialize(objects);
		Class[] clzs = (Class[]) SerializeUtil.deSerialize(se);

		Object[] args = new Object[]{ entity, Lists.newArrayList("a", "b")};
//		byte[] argsb = SerializeUtil.serialize(args);
//		Object[] args2 = (Object[]) SerializeUtil.deSerialize(argsb);
//		System.out.println(args2);
		JSONArray argss = JSONArray.parseArray(JSONArray.toJSONString(args));
		Object[] args2 = new Object[args.length];
		for (int i = 0; i < objects.length; i++) {
			args2[i] = JSONObject.parseObject(JSONObject.toJSONString(argss.get(i)), objects[i]);
		}

		Object o2 = SpringBeanContextUtil.get("userService");
		Class clazz2 = o2.getClass();
		Method m2 = clazz2.getDeclaredMethod("test", clzs);

		Object result2 = m2.invoke(o2, args2);
		System.out.println(result2);


//		List<HeadquartersDistribution> beanList = headquartersDistributionServiceImpl.list();

		System.out.println(JSONObject.toJSONString(evn.getActiveProfiles()));
		System.out.println(JSONObject.toJSONString(evn.getDefaultProfiles()));
		System.out.println(evn.getProperty("dmall.dmc.projectCode"));
		System.out.println(evn.getProperty("amp.serverAddress"));
		Assert.assertEquals(true, 1 == 1);
//		beanList.forEach(System.out::println);

	}
}
